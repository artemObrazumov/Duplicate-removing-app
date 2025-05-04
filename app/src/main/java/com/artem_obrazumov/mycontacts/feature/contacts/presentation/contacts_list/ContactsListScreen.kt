package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.core.presentation.screens.LoadingScreen
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.ContactItem
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.LetterHeader
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing.DuplicateRemovingService
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing.DuplicateRemovingServiceCallback
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing.IDuplicateRemovingService
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ContactsListScreen(
    state: ContactsListState,
    modifier: Modifier = Modifier,
    onAction: (action: ContactsListAction) -> Unit,
    effectFlow: SharedFlow<ContactsListEffect>
) {
    val context = LocalContext.current
    var service by remember { mutableStateOf<IDuplicateRemovingService?>(null) }

    val callback = remember<DuplicateRemovingServiceCallback> {
        object : DuplicateRemovingServiceCallback.Stub() {
            override fun onCleanStarted(duplicates: Int) {
                println("Started $duplicates")
            }

            override fun onProgress(progress: Int) {
                println("Progress: $progress")
            }

            override fun onCleanCompleted(duplicatesRemoved: Int) {
                onAction(ContactsListAction.RefreshContacts)
            }
        }
    }

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                service = IDuplicateRemovingService.Stub.asInterface(binder)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }
    }

    DisposableEffect (Unit) {
        val intent = Intent(context, DuplicateRemovingService::class.java).apply {
            action = "com.artem_obrazumov.mycontacts.feature.contacts.DUPLICATE_REMOVING_SERVICE"
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        onDispose {
            context.unbindService(serviceConnection)
        }
    }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when(effect) {
                ContactsListEffect.StartDuplicatesCleaningService -> {
                    service?.startCleanContacts(callback)
                }
            }
        }
    }

    when(state) {
        ContactsListState.Loading -> {
            LoadingScreen(modifier = modifier)
        }
        is ContactsListState.Content -> {
            ContactsListScreenContent(
                groupedContacts = state.groupedContacts,
                modifier = modifier
            )
            Button(onClick = {onAction(ContactsListAction.RemoveDuplicates)}) { Text("start") }
        }
        ContactsListState.ContactsUnavailable -> {
            ContactsUnavailableScreen(
                modifier = modifier,
                onPermissionGranted = {
                    onAction(ContactsListAction.RefreshContacts)
                }
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsListScreenContent(
    groupedContacts: Map<Char, List<Contact>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        groupedContacts.forEach { (letter, contacts) ->
            stickyHeader {
                LetterHeader(letter = letter)
            }

            items(
                items = contacts,
                key = { it.rawId }
            ) { contact ->
                ContactItem(
                    contact = contact,
                    modifier = Modifier
                        .padding(start = 24.dp)
                )
            }
        }
    }
}

@Composable
fun ContactsUnavailableScreen(
    modifier: Modifier = Modifier,
    onPermissionGranted: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if (isGranted.values.all { it }) onPermissionGranted.invoke()
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.cant_read_write_contacts),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Button(
            onClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS))
            }
        ) {
            Text(
                text = stringResource(R.string.grant_access)
            )
        }
    }
}

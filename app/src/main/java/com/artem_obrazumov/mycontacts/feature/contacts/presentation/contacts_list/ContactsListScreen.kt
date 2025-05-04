package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.core.presentation.resources.resolve
import com.artem_obrazumov.mycontacts.core.presentation.screens.LoadingScreen
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.ContactItem
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.DuplicateCleaningServiceItem
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.LetterHeader
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.ServiceEffect
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.utils.toStringResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ContactsListScreen(
    state: ContactsListScreenState,
    modifier: Modifier = Modifier,
    onAction: (action: ContactsListScreenAction) -> Unit,
    effectFlow: SharedFlow<ContactsListScreenEffect>
) {
    val serviceEffectFlow = remember {
        MutableSharedFlow<ServiceEffect>()
    }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                ContactsListScreenEffect.StartDuplicatesCleaningService -> {
                    serviceEffectFlow.emit(ServiceEffect.StartService)
                }
            }
        }
    }

    when (state) {
        ContactsListScreenState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is ContactsListScreenState.Content -> {
            Box(
                modifier = modifier
            ) {
                var serviceItemHeight by remember { mutableStateOf(0.dp) }
                ContactsListScreenContent(
                    state = state.contactsListState,
                    bottomSpacingHeight = serviceItemHeight
                )
                DuplicateCleaningServiceItem(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    state = state.duplicatesCleanerServiceState,
                    onHeightCalculated = {
                        serviceItemHeight = it
                    },
                    serviceEffectFlow = serviceEffectFlow,
                    onServiceStartRequested = {
                        onAction(ContactsListScreenAction.RemoveDuplicates)
                    },
                    onServiceCancelled = { error ->
                        onAction(ContactsListScreenAction.ShowCleaningCancelled(error))
                    },
                    onServiceStarted = { duplicates ->
                        onAction(ContactsListScreenAction.ShowCleaningStarted(duplicates))
                    },
                    onServiceProgress = { progress, total ->
                        onAction(ContactsListScreenAction.ShowCleaningProgress(progress, total))
                    },
                    onServiceCompleted = { removed, total, errors ->
                        onAction(
                            ContactsListScreenAction.ShowCleaningFinished(removed, total, errors)
                        )
                    }
                )
            }
        }

        ContactsListScreenState.ContactsUnavailable -> {
            ContactsUnavailableScreen(
                modifier = modifier,
                onPermissionGranted = {
                    onAction(ContactsListScreenAction.RefreshContactsScreen)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsListScreenContent(
    state: ContactsListScreenState.ContactsListState,
    modifier: Modifier = Modifier,
    bottomSpacingHeight: Dp = 0.dp,
) {
    when (state) {
        is ContactsListScreenState.ContactsListState.Content -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                state.groupedContacts.forEach { (letter, contacts) ->
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
                item {
                    Spacer(
                        modifier = Modifier
                            .height(bottomSpacingHeight)
                    )
                }
            }
        }

        ContactsListScreenState.ContactsListState.Loading -> {
            LoadingScreen(
                modifier = modifier
            )
        }

        is ContactsListScreenState.ContactsListState.Failure -> {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    text = state.error.toStringResource().resolve(),
                    textAlign = TextAlign.Center
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

    Column(
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
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS
                    )
                )
            }
        ) {
            Text(
                text = stringResource(R.string.grant_access)
            )
        }
    }
}

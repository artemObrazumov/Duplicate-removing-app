package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import android.Manifest
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.core.presentation.screens.LoadingScreen
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.ContactItem
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.LetterHeader

@Composable
fun ContactsListScreen(
    state: ContactsListState,
    modifier: Modifier = Modifier,
    onAction: (action: ContactsListAction) -> Unit
) {

    when(state) {
        ContactsListState.Loading -> {
            LoadingScreen(modifier = modifier)
        }
        is ContactsListState.Content -> {
            ContactsListScreenContent(
                groupedContacts = state.groupedContacts,
                modifier = modifier
            )
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

            items(contacts) { contact ->
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
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onPermissionGranted.invoke()
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.cant_read_contacts),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        ) {
            Text(
                text = stringResource(R.string.grant_access)
            )
        }
    }
}

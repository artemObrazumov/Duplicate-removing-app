package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.artem_obrazumov.mycontacts.core.presentation.screens.LoadingScreen
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.ContactItem
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components.LetterHeader

@Composable
fun ContactsListScreen(
    state: ContactsListState,
    modifier: Modifier = Modifier
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
    ) {
        groupedContacts.forEach { (letter, contacts) ->
            stickyHeader {
                LetterHeader(letter = letter)
            }

            items(contacts) { contact ->
                ContactItem(contact = contact)
            }
        }
    }
}

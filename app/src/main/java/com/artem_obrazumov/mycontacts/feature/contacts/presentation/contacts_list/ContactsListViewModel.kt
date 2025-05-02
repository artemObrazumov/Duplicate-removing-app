package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycontacts.core.presentation.view_model.Action
import com.artem_obrazumov.mycontacts.core.presentation.view_model.Effect
import com.artem_obrazumov.mycontacts.core.presentation.view_model.State
import com.artem_obrazumov.mycontacts.core.presentation.view_model.StatefulViewModel
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.groupByFirstLetter
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetContactsUseCase
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val getContactsUseCase: GetContactsUseCase
) : StatefulViewModel<ContactsListState, ContactsListAction, ContactsListEffect>(
    ContactsListState.Loading
) {

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            val contacts = getContactsUseCase()
            updateState(ContactsListState.Content(contacts.groupByFirstLetter()))
        }
    }

    override fun onAction(action: ContactsListAction) {

    }
}

sealed class ContactsListState : State {

    data object Loading : ContactsListState()
    data class Content(
        val groupedContacts: Map<Char, List<Contact>>
    ) : ContactsListState()
}

sealed class ContactsListAction : Action {

    data object RemoveDuplicates : ContactsListAction()
}

sealed class ContactsListEffect : Effect {

}
package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list

import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycontacts.core.presentation.view_model.Action
import com.artem_obrazumov.mycontacts.core.presentation.view_model.Effect
import com.artem_obrazumov.mycontacts.core.presentation.view_model.State
import com.artem_obrazumov.mycontacts.core.presentation.view_model.StatefulViewModel
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.groupByFirstLetter
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetReadWriteContactsPermissionUseCase
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val getContactsUseCase: GetContactsUseCase,
    private val getReadWriteContactsPermissionUseCase: GetReadWriteContactsPermissionUseCase
) : StatefulViewModel<ContactsListState, ContactsListAction, ContactsListEffect>(
    ContactsListState.Loading
) {

    init {
        tryLoadContacts()
    }

    private fun tryLoadContacts() {
        viewModelScope.launch {
            updateState(ContactsListState.Loading)
            if (!getReadWriteContactsPermissionUseCase()) {
                updateState(ContactsListState.ContactsUnavailable)
                return@launch
            }
            val contacts = getContactsUseCase()
            updateState(ContactsListState.Content(contacts.groupByFirstLetter()))
        }
    }

    private fun startDuplicatesCleaningService() {
        viewModelScope.launch {
            updateEffect(ContactsListEffect.StartDuplicatesCleaningService)
        }
    }

    override fun onAction(action: ContactsListAction) {
        when (action) {
            ContactsListAction.RefreshContacts -> {
                tryLoadContacts()
            }

            ContactsListAction.RemoveDuplicates -> {
                startDuplicatesCleaningService()
            }
        }
    }
}

sealed class ContactsListState : State {

    data object Loading : ContactsListState()
    data class Content(
        val groupedContacts: Map<Char, List<Contact>>
    ) : ContactsListState()

    data object ContactsUnavailable : ContactsListState()
}

sealed class ContactsListAction : Action {

    data object RefreshContacts : ContactsListAction()
    data object RemoveDuplicates : ContactsListAction()
}

sealed class ContactsListEffect : Effect {

    data object StartDuplicatesCleaningService : ContactsListEffect()
}
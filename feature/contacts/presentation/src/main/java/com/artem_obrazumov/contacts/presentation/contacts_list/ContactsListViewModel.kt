package com.artem_obrazumov.contacts.presentation.contacts_list

import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.model.groupByFirstLetter
import com.artem_obrazumov.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.domain.usecase.GetContactsUseCaseResult
import com.artem_obrazumov.domain.usecase.GetReadWriteContactsPermissionUseCase
import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.presentation.view_model.Action
import com.artem_obrazumov.presentation.view_model.Effect
import com.artem_obrazumov.presentation.view_model.State
import com.artem_obrazumov.presentation.view_model.StatefulViewModel
import com.artem_obrazumov.contacts.presentation.model.CleaningResult
import kotlinx.coroutines.launch

class ContactsListViewModel(
    private val getContactsUseCase: GetContactsUseCase,
    private val getReadWriteContactsPermissionUseCase: GetReadWriteContactsPermissionUseCase
) : StatefulViewModel<ContactsListScreenState, ContactsListScreenAction, ContactsListScreenEffect>(
    ContactsListScreenState.Loading
) {

    private var contactsListState: ContactsListScreenState.ContactsListState =
        ContactsListScreenState.ContactsListState.Loading
    private var duplicatesCleanerServiceState: ContactsListScreenState.DuplicatesCleanerServiceState =
        ContactsListScreenState.DuplicatesCleanerServiceState.Hidden

    init {
        tryLoadContacts()
    }

    private fun updateContentState() {
        updateState(
            ContactsListScreenState.Content(
                contactsListState,
                duplicatesCleanerServiceState
            )
        )
    }

    private fun tryLoadContacts() {
        viewModelScope.launch {
            contactsListState = ContactsListScreenState.ContactsListState.Loading
            updateContentState()
            if (!getReadWriteContactsPermissionUseCase()) {
                updateState(ContactsListScreenState.ContactsUnavailable)
                return@launch
            }
            contactsListState = when(val result = getContactsUseCase()) {
                is GetContactsUseCaseResult.Failure -> {
                    ContactsListScreenState.ContactsListState.Failure(result.error)
                }

                is GetContactsUseCaseResult.Success -> {
                    showServiceItemIfHidden()
                    ContactsListScreenState.ContactsListState.Content(result.contacts.groupByFirstLetter())
                }
            }
            updateContentState()
        }
    }

    private fun showServiceItemIfHidden() {
        viewModelScope.launch {
            if (duplicatesCleanerServiceState is
                        ContactsListScreenState.DuplicatesCleanerServiceState.Hidden
            ) {
                duplicatesCleanerServiceState =
                    ContactsListScreenState.DuplicatesCleanerServiceState.Idle()
            }
        }
    }

    private fun startDuplicatesCleaningService() {
        viewModelScope.launch {
            updateEffect(ContactsListScreenEffect.StartDuplicatesCleaningService)
        }
    }

    override fun onAction(action: ContactsListScreenAction) {
        when (action) {
            ContactsListScreenAction.RefreshContactsScreen -> {
                tryLoadContacts()
            }

            ContactsListScreenAction.RemoveDuplicates -> {
                startDuplicatesCleaningService()
            }

            is ContactsListScreenAction.ShowCleaningStarted -> {
                showCleaningStarted(action.duplicates)
            }

            is ContactsListScreenAction.ShowCleaningProgress -> {
                updateCleaningProgress(action.progress, action.total)
            }

            is ContactsListScreenAction.ShowCleaningFinished -> {
                showCleaningIdle(
                    cleaningResult = CleaningResult(
                        removed = action.removed,
                        total = action.total
                    ),
                    errors = action.errors
                )
                tryLoadContacts()
            }

            is ContactsListScreenAction.ShowCleaningCancelled -> {
                showCleaningIdle(
                    errors = listOf(action.error)
                )
            }
        }
    }

    private fun updateCleaningProgress(progress: Int, total: Int) {
        viewModelScope.launch {
            duplicatesCleanerServiceState =
                ContactsListScreenState.DuplicatesCleanerServiceState.Process(progress, total)
            updateContentState()
        }
    }

    private fun showCleaningStarted(duplicates: Int) {
        viewModelScope.launch {
            duplicatesCleanerServiceState =
                ContactsListScreenState.DuplicatesCleanerServiceState.CleaningStarted(duplicates)
            updateContentState()
        }
    }

    private fun showCleaningIdle(
        cleaningResult: CleaningResult? = null,
        errors: List<ContactsError> = emptyList()
    ) {
        viewModelScope.launch {
            duplicatesCleanerServiceState =
                ContactsListScreenState.DuplicatesCleanerServiceState.Idle(cleaningResult, errors)
            updateContentState()
        }
    }
}

sealed class ContactsListScreenState : State {

    data object Loading : ContactsListScreenState()
    data class Content(
        val contactsListState: ContactsListState,
        val duplicatesCleanerServiceState: DuplicatesCleanerServiceState
    ) : ContactsListScreenState()

    data object ContactsUnavailable : ContactsListScreenState()

    sealed class ContactsListState {

        data object Loading : ContactsListState()
        data class Content(
            val groupedContacts: Map<Char, List<Contact>>
        ) : ContactsListState()
        data class Failure(
            val error: ContactsError
        ) : ContactsListState()
    }

    sealed class DuplicatesCleanerServiceState {

        data object Hidden : DuplicatesCleanerServiceState()
        data class Idle(
            val cleaningResult: CleaningResult? = null,
            val errors: List<ContactsError> = emptyList()
        ) : DuplicatesCleanerServiceState()
        data class CleaningStarted(
            val duplicates: Int
        ) : DuplicatesCleanerServiceState()
        data class Process(
            val contactsDeleted: Int,
            val total: Int
        ) : DuplicatesCleanerServiceState()
    }
}

sealed class ContactsListScreenAction : Action {

    data object RefreshContactsScreen : ContactsListScreenAction()
    data object RemoveDuplicates : ContactsListScreenAction()
    data class ShowCleaningStarted(
        val duplicates: Int
    ) : ContactsListScreenAction()
    data class ShowCleaningProgress(
        val progress: Int,
        val total: Int
    ) : ContactsListScreenAction()
    data class ShowCleaningFinished(
        val removed: Int,
        val total: Int,
        val errors: List<ContactsError>
    ) : ContactsListScreenAction()
    data class ShowCleaningCancelled(
        val error: ContactsError
    ) : ContactsListScreenAction()
}

sealed class ContactsListScreenEffect : Effect {

    data object StartDuplicatesCleaningService : ContactsListScreenEffect()
}
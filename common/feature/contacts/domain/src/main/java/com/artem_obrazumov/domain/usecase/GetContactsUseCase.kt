package com.artem_obrazumov.domain.usecase

import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.repository.ContactsRepository
import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.domain.Result

class GetContactsUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): GetContactsUseCaseResult {
        return when(val result = contactsRepository.getContacts()) {
            is Result.Failure -> {
                GetContactsUseCaseResult.Failure(result.error)
            }
            is Result.Success -> {
                GetContactsUseCaseResult.Success(result.data)
            }
        }
    }
}

sealed class GetContactsUseCaseResult {

    data class Success(
        val contacts: List<Contact>
    ) : GetContactsUseCaseResult()
    data class Failure(
        val error: ContactsError
    ) : GetContactsUseCaseResult()
}
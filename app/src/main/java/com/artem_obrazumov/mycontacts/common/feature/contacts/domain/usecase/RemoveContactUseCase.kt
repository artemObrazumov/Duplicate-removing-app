package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ContactsError
import com.artem_obrazumov.mycontacts.core.domain.Result

class RemoveContactUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(id: Long): RemoveContactUseCaseResult {
        return when(val result = contactsRepository.removeContact(id)) {
            is Result.Failure -> {
                RemoveContactUseCaseResult.Failure(result.error)
            }
            is Result.Success -> {
                RemoveContactUseCaseResult.Success
            }
        }
    }
}

sealed class RemoveContactUseCaseResult {

    data class Failure(
        val error: ContactsError
    ): RemoveContactUseCaseResult()
    data object Success : RemoveContactUseCaseResult()
}
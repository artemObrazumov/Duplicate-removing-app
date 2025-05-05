package com.artem_obrazumov.domain.usecase

import com.artem_obrazumov.domain.repository.ContactsRepository

class GetReadWriteContactsPermissionUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): Boolean {
        return contactsRepository.getReadWriteContactsPermission()
    }
}
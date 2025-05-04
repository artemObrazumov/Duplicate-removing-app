package com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository

class GetReadWriteContactsPermissionUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): Boolean {
        return contactsRepository.getReadContactsPermission()
    }
}
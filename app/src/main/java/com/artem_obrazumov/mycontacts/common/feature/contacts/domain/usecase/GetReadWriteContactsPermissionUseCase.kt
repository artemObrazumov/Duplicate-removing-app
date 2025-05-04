package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository

class GetReadWriteContactsPermissionUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): Boolean {
        return contactsRepository.getReadWriteContactsPermission()
    }
}
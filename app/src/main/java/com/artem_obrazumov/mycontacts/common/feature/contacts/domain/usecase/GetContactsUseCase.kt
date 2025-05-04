package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository

class GetContactsUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): List<Contact> {
        return contactsRepository.getContacts()
    }
}
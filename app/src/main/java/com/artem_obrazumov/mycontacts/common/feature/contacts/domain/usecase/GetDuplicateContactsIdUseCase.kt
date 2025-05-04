package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository

class GetDuplicateContactsIdUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): List<Long> {
        val contacts = contactsRepository.getContacts()
        val ids = mutableListOf<Long>()
        contacts.forEach { contact ->
            if (contact.isDuplicate) {
                ids.add(contact.rawId)
            }
        }
        return ids
    }
}
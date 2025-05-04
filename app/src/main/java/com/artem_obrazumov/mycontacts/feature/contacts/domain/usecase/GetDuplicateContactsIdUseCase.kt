package com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository

class GetDuplicateContactsIdUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): List<Long> {
        val contacts = contactsRepository.getContacts()
        val ids = mutableListOf<Long>()
        val contactsSet = mutableSetOf<Contact>()
        contacts.forEach { contact ->
            if (contact in contactsSet) {
                ids.add(contact.rawId)
            } else {
                contactsSet.add(contact)
            }
        }
        return ids
    }
}
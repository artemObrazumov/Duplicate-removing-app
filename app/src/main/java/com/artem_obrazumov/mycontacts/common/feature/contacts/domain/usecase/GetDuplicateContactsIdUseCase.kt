package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.core.domain.Result

class GetDuplicateContactsIdUseCase(
    private val contactsRepository: ContactsRepository
) {

    suspend operator fun invoke(): List<Long> {
        val result = contactsRepository.getContacts()
        if (result is Result.Success) {
            val contacts = result.data
            val ids = mutableListOf<Long>()
            contacts.forEach { contact ->
                if (contact.isDuplicate) {
                    ids.add(contact.rawId)
                }
            }
            return ids
        }
        return emptyList()
    }
}
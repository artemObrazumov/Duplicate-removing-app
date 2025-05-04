package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ContactsError
import com.artem_obrazumov.mycontacts.core.domain.Result

interface ContactsRepository {

    suspend fun getReadWriteContactsPermission(): Boolean
    suspend fun getContacts(): Result<List<Contact>, ContactsError>
    suspend fun removeContact(id: Long): Result<Unit, ContactsError>
}
package com.artem_obrazumov.domain.repository

import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.Result
import com.artem_obrazumov.domain.utils.ContactsError

interface ContactsRepository {

    suspend fun getReadWriteContactsPermission(): Boolean
    suspend fun getContacts(): Result<List<Contact>, ContactsError>
    suspend fun removeContact(id: Long): Result<Unit, ContactsError>
}
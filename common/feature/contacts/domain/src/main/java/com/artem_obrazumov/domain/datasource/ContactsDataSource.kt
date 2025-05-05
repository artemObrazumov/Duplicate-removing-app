package com.artem_obrazumov.domain.datasource

import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.domain.Result

interface ContactsDataSource {

    suspend fun getContacts(): Result<List<Contact>, ContactsError>
    suspend fun removeContact(id: Long): Result<Unit, ContactsError>
}
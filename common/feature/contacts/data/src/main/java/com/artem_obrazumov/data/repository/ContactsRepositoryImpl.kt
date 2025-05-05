package com.artem_obrazumov.data.repository

import com.artem_obrazumov.domain.datasource.ContactsDataSource
import com.artem_obrazumov.domain.datasource.ContactsPermissionDataSource
import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.repository.ContactsRepository
import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.domain.Result

class ContactsRepositoryImpl(
    private val contactsPermissionDataSource: ContactsPermissionDataSource,
    private val contactsDataSource: ContactsDataSource
): ContactsRepository {

    override suspend fun getContacts(): Result<List<Contact>, ContactsError> {
        return contactsDataSource.getContacts()
    }

    override suspend fun getReadWriteContactsPermission(): Boolean {
        return contactsPermissionDataSource.getReadWriteContactsPermission()
    }

    override suspend fun removeContact(id: Long): Result<Unit, ContactsError> {
        return contactsDataSource.removeContact(id)
    }
}
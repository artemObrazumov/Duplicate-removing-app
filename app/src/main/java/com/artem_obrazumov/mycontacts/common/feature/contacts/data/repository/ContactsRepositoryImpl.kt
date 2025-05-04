package com.artem_obrazumov.mycontacts.common.feature.contacts.data.repository

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource.ContactsDataSource
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource.ContactsPermissionDataSource
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ContactsError
import com.artem_obrazumov.mycontacts.core.domain.Result

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
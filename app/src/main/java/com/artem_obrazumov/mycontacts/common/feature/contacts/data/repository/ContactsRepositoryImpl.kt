package com.artem_obrazumov.mycontacts.common.feature.contacts.data.repository

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource.ContactsDataSource
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource.ContactsPermissionDataSource
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository

class ContactsRepositoryImpl(
    private val contactsPermissionDataSource: ContactsPermissionDataSource,
    private val contactsDataSource: ContactsDataSource
): ContactsRepository {

    override suspend fun getContacts(): List<Contact> {
        return contactsDataSource.getContacts()
    }

    override suspend fun getReadContactsPermission(): Boolean {
        return contactsPermissionDataSource.getReadContactsPermission()
    }

    override suspend fun getWriteContactsPermission(): Boolean {
        return contactsPermissionDataSource.getWriteContactsPermission()
    }
}
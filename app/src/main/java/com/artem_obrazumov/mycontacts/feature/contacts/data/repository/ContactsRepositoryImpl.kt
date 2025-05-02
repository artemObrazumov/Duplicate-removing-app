package com.artem_obrazumov.mycontacts.feature.contacts.data.repository

import com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource.ContactsDataSource
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository

class ContactsRepositoryImpl(
    private val contactsDataSource: ContactsDataSource
): ContactsRepository {

    override suspend fun getContacts(): List<Contact> {
        return contactsDataSource.getContacts()
    }
}
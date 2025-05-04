package com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource

import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact

interface ContactsDataSource {

    suspend fun getContacts(): List<Contact>
    suspend fun getDuplicateContactIds(): List<Long>
}
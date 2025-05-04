package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact

interface ContactsRepository {

    suspend fun getReadContactsPermission(): Boolean
    suspend fun getWriteContactsPermission(): Boolean
    suspend fun getContacts(): List<Contact>
}
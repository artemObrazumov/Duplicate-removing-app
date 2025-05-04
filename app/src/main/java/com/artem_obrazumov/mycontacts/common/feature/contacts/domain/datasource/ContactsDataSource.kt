package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.model.Contact

interface ContactsDataSource {

    suspend fun getContacts(): List<Contact>
}
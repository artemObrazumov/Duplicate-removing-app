package com.artem_obrazumov.mycontacts.feature.contacts.domain.repository

import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact

interface ContactsRepository {

    suspend fun getContacts(): List<Contact>
}
package com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource

interface ContactsPermissionDataSource {

    suspend fun getReadContactsPermission(): Boolean
    suspend fun getWriteContactsPermission(): Boolean
}
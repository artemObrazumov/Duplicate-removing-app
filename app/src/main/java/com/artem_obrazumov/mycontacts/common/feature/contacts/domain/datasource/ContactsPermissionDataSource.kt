package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.datasource

interface ContactsPermissionDataSource {

    suspend fun getReadContactsPermission(): Boolean
    suspend fun getWriteContactsPermission(): Boolean
}
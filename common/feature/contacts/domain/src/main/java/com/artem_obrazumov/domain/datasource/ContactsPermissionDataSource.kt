package com.artem_obrazumov.domain.datasource

interface ContactsPermissionDataSource {

    suspend fun getReadWriteContactsPermission(): Boolean
}
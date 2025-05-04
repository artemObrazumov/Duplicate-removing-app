package com.artem_obrazumov.mycontacts.feature.contacts.data.datasource

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource.ContactsPermissionDataSource

class ContactsPermissionDataSourceImpl(
    private val context: Context
) : ContactsPermissionDataSource {

    override suspend fun getReadContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun getWriteContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
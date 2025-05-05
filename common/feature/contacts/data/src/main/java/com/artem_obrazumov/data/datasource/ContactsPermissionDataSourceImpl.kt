package com.artem_obrazumov.data.datasource

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.artem_obrazumov.domain.datasource.ContactsPermissionDataSource

class ContactsPermissionDataSourceImpl(
    private val context: Context
) : ContactsPermissionDataSource {

    override suspend fun getReadWriteContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
package com.artem_obrazumov.mycontacts.feature.contacts.data.datasource

import android.content.Context
import android.provider.ContactsContract
import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.core.domain.StringResource
import com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource.ContactsDataSource
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsDataSourceImpl(
    private val context: Context
): ContactsDataSource {

    private fun getPhoneNumbersForContact(id: Long): List<Contact.PhoneNumber> {
        val numbers = mutableListOf<Contact.PhoneNumber>()

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID} = ?",
            arrayOf(id.toString()),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val number = cursor.getString(
                    cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )

                if (!number.isNullOrBlank()) {
                    numbers.add(Contact.PhoneNumber(number = number))
                }
            }
        }

        return numbers
    }

    override suspend fun getContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val contactsSet = mutableSetOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.TYPE
        )
        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val contentResolver = context.contentResolver
        contentResolver.query(uri, projection, null, null,
            sortOrder)?.use { cursor ->
            while (cursor.moveToNext()) {
                try {
                    val id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        )
                    )
                    val name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        )
                    )
                    val photoUri = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
                        )
                    )
                    val phoneNumbers = getPhoneNumbersForContact(id)
                    val contact = Contact(
                        id = id,
                        name = if (name == null) {
                            StringResource.FromResource(R.string.no_name)
                        } else {
                            StringResource.FromString(name)
                        },
                        photoUri = photoUri,
                        phoneNumbers = phoneNumbers
                    )
                    if (contact in contactsSet) {
                        contact.isDuplicate = true
                    } else {
                        contactsSet.add(contact)
                    }

                    contacts.add(contact)
                } catch (e: Exception) {
                    // TODO: proper error logging
                    e.printStackTrace()
                }
            }
        }
        contacts
    }
}
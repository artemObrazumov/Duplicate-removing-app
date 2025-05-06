package com.artem_obrazumov.data.datasource

import android.content.Context
import android.provider.ContactsContract
import com.artem_obrazumov.contacts.data.R
import com.artem_obrazumov.domain.datasource.ContactsDataSource
import com.artem_obrazumov.domain.model.Contact
import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.domain.Result
import com.artem_obrazumov.domain.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsDataSourceImpl(
    private val context: Context
) : ContactsDataSource {

    private fun getAllContacts(): Result<List<Contact>, ContactsError> {
        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
        )
        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val contentResolver = context.contentResolver
        contentResolver.query(
            uri, projection, null, null,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                try {
                    val rawId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID
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
                    val phoneNumbers = getPhoneNumbersForContact(rawId)
                    val contact = Contact(
                        rawId = rawId,
                        name = if (name == null) {
                            StringResource.FromResource(R.string.no_name)
                        } else {
                            StringResource.FromString(name)
                        },
                        photoUri = photoUri,
                        phoneNumbers = phoneNumbers
                    )
                    contacts.add(contact)
                } catch (e: Exception) {
                    return Result.Failure(ContactsError.GettingContactsError)
                }
            }
        }
        return Result.Success(contacts)
    }

    private fun markDuplicates(contacts: List<Contact>) {
        val contactsSet = mutableSetOf<Contact>()
        contacts.forEach { contact ->
            if (contact in contactsSet) {
                contact.isDuplicate = true
            } else {
                contactsSet.add(contact)
            }
        }
    }

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

    override suspend fun getContacts(): Result<List<Contact>, ContactsError> {
        return withContext(Dispatchers.IO) {
            when (val getContactsResult = getAllContacts()) {
                is Result.Failure -> {
                    Result.Failure(getContactsResult.error)
                }
                is Result.Success -> {
                    Result.Success(getContactsResult.data.apply { markDuplicates(this) })
                }
            }
        }
    }

    override suspend fun removeContact(id: Long): Result<Unit, ContactsError> {
        val rows = context.contentResolver.delete(
            ContactsContract.RawContacts.CONTENT_URI,
            "${ContactsContract.RawContacts._ID} = ?",
            arrayOf(id.toString())
        )
        return if (rows == 0) {
            Result.Failure(ContactsError.RemovingError)
        } else {
            Result.Success(Unit)
        }
    }
}
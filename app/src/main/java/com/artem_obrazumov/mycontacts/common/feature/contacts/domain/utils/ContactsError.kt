package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils

import com.artem_obrazumov.mycontacts.core.domain.Error

sealed class ContactsError : Error {

    data object RemovingError : ContactsError()
    data object DuplicatesNotFoundError : ContactsError()
    data object GettingContactsError : ContactsError()
}
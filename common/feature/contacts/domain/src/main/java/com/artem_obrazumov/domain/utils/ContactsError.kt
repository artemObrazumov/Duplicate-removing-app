package com.artem_obrazumov.domain.utils

import com.artem_obrazumov.domain.Error

sealed class ContactsError : Error {

    data object RemovingError : ContactsError()
    data object DuplicatesNotFoundError : ContactsError()
    data object GettingContactsError : ContactsError()
}
package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ErrorCodes.DUPLICATES_NOT_FOUND_ERROR
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ErrorCodes.REMOVING_ERROR

fun ErrorCode.toContactsError(): ContactsError {
    return when (this) {
        REMOVING_ERROR -> ContactsError.RemovingError
        DUPLICATES_NOT_FOUND_ERROR -> ContactsError.DuplicatesNotFoundError
        else -> throw IllegalStateException("No such error")
    }
}
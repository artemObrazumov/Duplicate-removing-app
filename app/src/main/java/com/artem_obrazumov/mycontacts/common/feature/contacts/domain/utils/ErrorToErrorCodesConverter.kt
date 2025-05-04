package com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ErrorCodes.DUPLICATES_NOT_FOUND_ERROR
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ErrorCodes.REMOVING_ERROR

fun ContactsError.toErrorCode(): ErrorCode {
    return when (this) {
        ContactsError.DuplicatesNotFoundError -> DUPLICATES_NOT_FOUND_ERROR
        ContactsError.RemovingError -> REMOVING_ERROR
        else -> throw IllegalStateException("No such error")
    }
}
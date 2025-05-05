package com.artem_obrazumov.contacts.presentation.utils

import com.artem_obrazumov.domain.utils.ContactsError
import com.artem_obrazumov.domain.StringResource
import com.artem_obrazumov.contacts.presentation.R

fun ContactsError.RemovingError.toStringResource(): StringResource {
    return StringResource.FromResource(R.string.contact_removing_error)
}

fun ContactsError.DuplicatesNotFoundError.toStringResource(): StringResource {
    return StringResource.FromResource(R.string.duplicates_not_found_error)
}

fun ContactsError.GettingContactsError.toStringResource(): StringResource {
    return StringResource.FromResource(R.string.getting_contacts_error)
}

fun ContactsError.toStringResource(): StringResource {
    return when(this) {
        is ContactsError.DuplicatesNotFoundError -> this.toStringResource()
        is ContactsError.GettingContactsError -> this.toStringResource()
        is ContactsError.RemovingError -> this.toStringResource()
    }
}
package com.artem_obrazumov.mycontacts.feature.contacts.presentation.utils

import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ContactsError
import com.artem_obrazumov.mycontacts.core.domain.StringResource

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
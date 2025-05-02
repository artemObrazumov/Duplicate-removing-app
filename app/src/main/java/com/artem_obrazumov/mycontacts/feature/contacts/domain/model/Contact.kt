package com.artem_obrazumov.mycontacts.feature.contacts.domain.model

import com.artem_obrazumov.mycontacts.core.domain.StringResource

data class Contact(
    val id: Long,
    val name: StringResource,
    val photoUri: String?,
    val phoneNumbers: List<PhoneNumber>
) {
    @JvmInline
    value class PhoneNumber(
        val number: String
    )
}

fun List<Contact>.groupByFirstLetter(): Map<Char, List<Contact>> {
    return groupBy {
        when (it.name) {
            is StringResource.FromResource -> '-'
            is StringResource.FromString -> it.name.string.first().uppercaseChar()
        }
    }.toSortedMap()
}

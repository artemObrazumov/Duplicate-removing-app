package com.artem_obrazumov.mycontacts.feature.contacts.domain.model

import com.artem_obrazumov.mycontacts.core.domain.StringResource

class Contact(
    var id: Long,
    var rawId: Long,
    val name: StringResource,
    val photoUri: String?,
    val phoneNumbers: List<PhoneNumber>
) {
    var isDuplicate: Boolean = false

    @JvmInline
    value class PhoneNumber(
        val number: String
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (name != other.name) return false
        if (photoUri != other.photoUri) return false
        if (phoneNumbers != other.phoneNumbers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (photoUri?.hashCode() ?: 0)
        result = 31 * result + phoneNumbers.hashCode()
        return result
    }
}

fun List<Contact>.groupByFirstLetter(): Map<Char, List<Contact>> {
    return groupBy {
        when (it.name) {
            is StringResource.FromResource -> '-'
            is StringResource.FromString -> it.name.string.first().uppercaseChar()
        }
    }.toSortedMap()
}

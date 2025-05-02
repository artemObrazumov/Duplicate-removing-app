package com.artem_obrazumov.mycontacts.core.domain

import androidx.annotation.StringRes

sealed class StringResource {

    data class FromResource(
        @StringRes val reference: Int
    ): StringResource()

    data class FromString(
        val string: String
    ): StringResource()
}
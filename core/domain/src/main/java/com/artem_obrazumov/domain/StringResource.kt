package com.artem_obrazumov.domain

sealed class StringResource {

    data class FromResource(
        val reference: Int
    ): StringResource()

    data class FromString(
        val string: String
    ): StringResource()
}
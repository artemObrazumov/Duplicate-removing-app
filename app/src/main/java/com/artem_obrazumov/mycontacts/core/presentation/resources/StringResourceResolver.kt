package com.artem_obrazumov.mycontacts.core.presentation.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.artem_obrazumov.mycontacts.core.domain.StringResource

@Composable
internal fun StringResource.FromResource.resolve(): String {
    return stringResource(this.reference)
}

internal fun StringResource.FromString.resolve(): String {
    return this.string
}

@Composable
fun StringResource.resolve(): String {
    return when(this) {
        is StringResource.FromResource -> this.resolve()
        is StringResource.FromString -> this.resolve()
    }
}

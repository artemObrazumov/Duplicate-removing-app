package com.artem_obrazumov.contacts.presentation.contacts_list.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LetterHeader(
    letter: Char,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = letter.toString(),
        style = MaterialTheme.typography.headlineLarge
    )
}
package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.artem_obrazumov.mycontacts.core.presentation.resources.resolve
import com.artem_obrazumov.mycontacts.feature.contacts.domain.model.Contact

@Composable
fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(contact.photoUri)
                .crossfade(true)
                .build(),
            contentDescription = contact.name.resolve(),
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )

        Column {
            val mainPhoneNumber = remember { contact.phoneNumbers.first().toString() }
            Text(text = contact.name.resolve(), style = MaterialTheme.typography.bodyLarge)
            Text(text = mainPhoneNumber, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
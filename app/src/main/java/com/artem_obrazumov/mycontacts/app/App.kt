package com.artem_obrazumov.mycontacts.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.navigation.ContactList
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.navigation.contactsGraph

@Composable
fun App(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ContactList,
        modifier = modifier
    ) {
        contactsGraph(navController)
    }
}
package com.artem_obrazumov.contacts.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.artem_obrazumov.contacts.presentation.contacts_list.ContactsListScreenEffect
import com.artem_obrazumov.contacts.presentation.contacts_list.ContactsListScreen
import com.artem_obrazumov.contacts.presentation.contacts_list.ContactsListViewModel
import com.artem_obrazumov.contacts.presentation.di.ContactsFeatureComponentProvider
import com.artem_obrazumov.presentation.di.daggerViewModel
import kotlinx.coroutines.flow.SharedFlow

fun NavGraphBuilder.contactsGraph(
    navController: NavController
) {
    val applicationContext = navController.context.applicationContext
    val contactsFeatureComponent = (applicationContext as ContactsFeatureComponentProvider)
        .provideContactsFeatureComponent()
    val contactsListScreenComponent = contactsFeatureComponent
        .contactsListScreenComponentFactory.create()

    composable<ContactList> {

        val viewModel: ContactsListViewModel = daggerViewModel {
            contactsListScreenComponent.contactsListViewModel
        }
        val state by viewModel.state.collectAsState()

        @Suppress("UNCHECKED_CAST")
        ContactsListScreen(
            state = state,
            onAction = viewModel::onAction,
            effectFlow = viewModel.effect as SharedFlow<ContactsListScreenEffect>
        )
    }
}
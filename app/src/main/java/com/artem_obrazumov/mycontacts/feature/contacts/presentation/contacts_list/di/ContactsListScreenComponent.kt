package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.di

import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.ContactsListViewModel
import dagger.Subcomponent

@ContactsListScreenScope
@Subcomponent(modules = [ContactsListScreenModule::class])
interface ContactsListScreenComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ContactsListScreenComponent
    }

    val contactsListViewModel: ContactsListViewModel
}
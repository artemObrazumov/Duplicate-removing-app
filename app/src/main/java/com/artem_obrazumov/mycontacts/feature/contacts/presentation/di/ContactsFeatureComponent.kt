package com.artem_obrazumov.mycontacts.feature.contacts.presentation.di

import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.di.ContactsListScreenComponent
import dagger.Subcomponent

@ContactsFeatureScope
@Subcomponent(modules = [ContactsFeatureModule::class])
interface ContactsFeatureComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): ContactsFeatureComponent
    }

    val contactsListScreenComponentFactory: ContactsListScreenComponent.Factory
}

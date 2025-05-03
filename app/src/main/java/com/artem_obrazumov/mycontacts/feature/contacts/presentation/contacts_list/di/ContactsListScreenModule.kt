package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.di

import com.artem_obrazumov.mycontacts.feature.contacts.data.di.UseCaseModule
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.ContactsListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [UseCaseModule::class])
class ContactsListScreenModule {

    @ContactsListScreenScope
    @Provides
    fun provideContactsListScreenViewModel(
        getContactsUseCase: GetContactsUseCase
    ): ContactsListViewModel = ContactsListViewModel(getContactsUseCase)
}

package com.artem_obrazumov.contacts.presentation.contacts_list.di

import com.artem_obrazumov.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.domain.usecase.GetReadWriteContactsPermissionUseCase
import com.artem_obrazumov.contacts.presentation.contacts_list.ContactsListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [UseCaseModule::class])
class ContactsListScreenModule {

    @ContactsListScreenScope
    @Provides
    fun provideContactsListScreenViewModel(
        getContactsUseCase: GetContactsUseCase,
        getReadWriteContactsPermissionUseCase: GetReadWriteContactsPermissionUseCase
    ): ContactsListViewModel =
        ContactsListViewModel(getContactsUseCase, getReadWriteContactsPermissionUseCase)
}

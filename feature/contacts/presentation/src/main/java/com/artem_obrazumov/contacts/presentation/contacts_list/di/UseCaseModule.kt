package com.artem_obrazumov.contacts.presentation.contacts_list.di

import com.artem_obrazumov.domain.repository.ContactsRepository
import com.artem_obrazumov.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.domain.usecase.GetReadWriteContactsPermissionUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @ContactsListScreenScope
    @Provides
    fun provideGetContactsUseCase(contactsRepository: ContactsRepository): GetContactsUseCase =
        GetContactsUseCase(contactsRepository)

    @ContactsListScreenScope
    @Provides
    fun provideGetReadContactsPermissionUseCase(
        contactsRepository: ContactsRepository
    ): GetReadWriteContactsPermissionUseCase =
        GetReadWriteContactsPermissionUseCase(
            contactsRepository
        )
}
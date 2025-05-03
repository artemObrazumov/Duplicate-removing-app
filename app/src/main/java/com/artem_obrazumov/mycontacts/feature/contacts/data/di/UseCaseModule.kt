package com.artem_obrazumov.mycontacts.feature.contacts.data.di

import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetContactsUseCase
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.di.ContactsListScreenScope
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @ContactsListScreenScope
    @Provides
    fun provideGetContactsUseCase(contactsRepository: ContactsRepository): GetContactsUseCase =
        GetContactsUseCase(contactsRepository)
}
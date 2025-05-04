package com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing.di

import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.feature.contacts.domain.usecase.GetDuplicateContactsIdUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @DuplicatesRemovingServiceScope
    @Provides
    fun provideGetContactsUseCase(
        contactsRepository: ContactsRepository
    ): GetDuplicateContactsIdUseCase = GetDuplicateContactsIdUseCase(contactsRepository)
}
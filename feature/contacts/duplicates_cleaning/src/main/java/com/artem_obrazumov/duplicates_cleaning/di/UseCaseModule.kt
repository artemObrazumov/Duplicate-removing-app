package com.artem_obrazumov.duplicates_cleaning.di

import com.artem_obrazumov.domain.repository.ContactsRepository
import com.artem_obrazumov.domain.usecase.GetDuplicateContactsIdUseCase
import com.artem_obrazumov.domain.usecase.RemoveContactUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @DuplicatesRemovingServiceScope
    @Provides
    fun provideGetContactsUseCase(
        contactsRepository: ContactsRepository
    ): GetDuplicateContactsIdUseCase =
        GetDuplicateContactsIdUseCase(contactsRepository)

    @DuplicatesRemovingServiceScope
    @Provides
    fun provideRemoveContactUseCase(
        contactsRepository: ContactsRepository
    ): RemoveContactUseCase =
        RemoveContactUseCase(contactsRepository)
}
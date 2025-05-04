package com.artem_obrazumov.mycontacts.feature.contacts.duplicates_removing.di

import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase.GetDuplicateContactsIdUseCase
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase.RemoveContactUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @DuplicatesRemovingServiceScope
    @Provides
    fun provideGetContactsUseCase(
        contactsRepository: ContactsRepository
    ): GetDuplicateContactsIdUseCase = GetDuplicateContactsIdUseCase(contactsRepository)

    @DuplicatesRemovingServiceScope
    @Provides
    fun provideRemoveContactUseCase(
        contactsRepository: ContactsRepository
    ): RemoveContactUseCase = RemoveContactUseCase(contactsRepository)
}
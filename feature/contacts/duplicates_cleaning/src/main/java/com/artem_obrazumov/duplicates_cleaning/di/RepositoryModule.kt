package com.artem_obrazumov.duplicates_cleaning.di

import android.content.Context
import com.artem_obrazumov.data.datasource.ContactsDataSourceImpl
import com.artem_obrazumov.data.datasource.ContactsPermissionDataSourceImpl
import com.artem_obrazumov.data.repository.ContactsRepositoryImpl
import com.artem_obrazumov.domain.datasource.ContactsDataSource
import com.artem_obrazumov.domain.datasource.ContactsPermissionDataSource
import com.artem_obrazumov.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @DuplicatesRemovingServiceScope
    fun provideContactsDataSource(
        context: Context
    ): ContactsDataSource =
        ContactsDataSourceImpl(context)

    @Provides
    @DuplicatesRemovingServiceScope
    fun provideContactsPermissionDataSource(
        context: Context
    ): ContactsPermissionDataSource =
        ContactsPermissionDataSourceImpl(context)

    @Provides
    @DuplicatesRemovingServiceScope
    fun provideContactsRepository(
        contactsPermissionDataSource: ContactsPermissionDataSource,
        contactsDataSource: ContactsDataSource,
    ): ContactsRepository =
        ContactsRepositoryImpl(
            contactsPermissionDataSource,
            contactsDataSource
        )
}
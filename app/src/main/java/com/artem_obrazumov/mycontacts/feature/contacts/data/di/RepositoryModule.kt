package com.artem_obrazumov.mycontacts.feature.contacts.data.di

import android.content.Context
import com.artem_obrazumov.mycontacts.feature.contacts.data.datasource.ContactsDataSourceImpl
import com.artem_obrazumov.mycontacts.feature.contacts.data.datasource.ContactsPermissionDataSourceImpl
import com.artem_obrazumov.mycontacts.feature.contacts.data.repository.ContactsRepositoryImpl
import com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource.ContactsDataSource
import com.artem_obrazumov.mycontacts.feature.contacts.domain.datasource.ContactsPermissionDataSource
import com.artem_obrazumov.mycontacts.feature.contacts.domain.repository.ContactsRepository
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureScope
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @ContactsFeatureScope
    fun provideContactsDataSource(
        context: Context
    ): ContactsDataSource = ContactsDataSourceImpl(context)

    @Provides
    @ContactsFeatureScope
    fun provideContactsPermissionDataSource(
        context: Context
    ): ContactsPermissionDataSource = ContactsPermissionDataSourceImpl(context)

    @Provides
    @ContactsFeatureScope
    fun provideContactsRepository(
        contactsPermissionDataSource: ContactsPermissionDataSource,
        contactsDataSource: ContactsDataSource,
    ): ContactsRepository = ContactsRepositoryImpl(contactsPermissionDataSource, contactsDataSource)
}
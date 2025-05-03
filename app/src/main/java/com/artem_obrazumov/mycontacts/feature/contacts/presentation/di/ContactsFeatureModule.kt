package com.artem_obrazumov.mycontacts.feature.contacts.presentation.di

import com.artem_obrazumov.mycontacts.feature.contacts.data.di.RepositoryModule
import dagger.Module

@Module(includes = [RepositoryModule::class])
class ContactsFeatureModule

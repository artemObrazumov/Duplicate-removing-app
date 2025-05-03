package com.artem_obrazumov.mycontacts.app.di

import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureComponent
import dagger.Module

@Module(subcomponents = [ContactsFeatureComponent::class])
class AppModule

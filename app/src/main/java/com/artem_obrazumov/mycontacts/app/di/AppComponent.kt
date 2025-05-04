package com.artem_obrazumov.mycontacts.app.di

import android.content.Context
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    val contactsFeatureComponentFactory: ContactsFeatureComponent.Factory
}
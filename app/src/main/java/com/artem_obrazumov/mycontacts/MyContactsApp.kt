package com.artem_obrazumov.mycontacts

import android.app.Application
import com.artem_obrazumov.duplicates_cleaning.di.DuplicatesRemovingServiceComponent
import com.artem_obrazumov.mycontacts.di.AppComponent
import com.artem_obrazumov.duplicates_cleaning.di.DuplicatesRemovingServiceComponentProvider
import com.artem_obrazumov.mycontacts.di.DaggerAppComponent
import com.artem_obrazumov.contacts.presentation.di.ContactsFeatureComponent
import com.artem_obrazumov.contacts.presentation.di.ContactsFeatureComponentProvider

class MyContactsApp: Application(),
    DuplicatesRemovingServiceComponentProvider,
    ContactsFeatureComponentProvider {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = requireNotNull(_appComponent) {
        "AppComponent not initialized"
    }

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun provideDuplicateRemovingServiceComponent(): DuplicatesRemovingServiceComponent {
        return appComponent.duplicatesRemovingServiceComponentFactory.create()
    }

    override fun provideContactsFeatureComponent(): ContactsFeatureComponent {
        return appComponent.contactsFeatureComponentFactory.create()
    }
}
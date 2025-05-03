package com.artem_obrazumov.mycontacts.app

import android.app.Application
import com.artem_obrazumov.mycontacts.app.di.AppComponent
import com.artem_obrazumov.mycontacts.app.di.DaggerAppComponent
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureComponent
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.di.ContactsFeatureComponentProvider

class MyContactsApp: Application(), ContactsFeatureComponentProvider {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent get() = requireNotNull(_appComponent) {
        "AppComponent not initialized"
    }

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun provideContactsFeatureComponent(): ContactsFeatureComponent {
        return appComponent.contactsFeatureComponentFactory.create()
    }
}
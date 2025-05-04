package com.artem_obrazumov.mycontacts.feature.contacts.duplicates_removing.di

import com.artem_obrazumov.mycontacts.feature.contacts.duplicates_removing.DuplicateCleaningService
import dagger.Subcomponent

@DuplicatesRemovingServiceScope
@Subcomponent(modules = [DuplicatesRemovingServiceModule::class])
interface DuplicatesRemovingServiceComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DuplicatesRemovingServiceComponent
    }

    fun inject(duplicateCleaningService: DuplicateCleaningService)
}
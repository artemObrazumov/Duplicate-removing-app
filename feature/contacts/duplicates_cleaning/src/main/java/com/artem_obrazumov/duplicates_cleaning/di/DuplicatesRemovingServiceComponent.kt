package com.artem_obrazumov.duplicates_cleaning.di

import com.artem_obrazumov.duplicates_cleaning.DuplicateCleaningService
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
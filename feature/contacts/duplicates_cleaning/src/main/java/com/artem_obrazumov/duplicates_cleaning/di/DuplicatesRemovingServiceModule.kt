package com.artem_obrazumov.duplicates_cleaning.di

import dagger.Module

@Module(includes = [RepositoryModule::class, UseCaseModule::class])
class DuplicatesRemovingServiceModule
package com.rs.myapps.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppRepoModule {
    @Binds
    abstract fun bindAppRepo(appRepo: AppRepositoryImpl) : IAppRepository
}
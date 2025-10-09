package com.rs.myapps.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CheckSumModule {

    @Provides
    fun provideCheckSumUseCases(): ICheckSumUseCases {
        return object : ICheckSumUseCases{
            override val checkSum: CheckSumUseCase
                get() = CheckSumUseCase()
            override val checkSumFormat: CheckSumFormatUseCase
                get() = CheckSumFormatUseCase()

        }
    }
}
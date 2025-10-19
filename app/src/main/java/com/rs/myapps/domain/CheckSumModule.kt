package com.rs.myapps.domain

import com.rs.myapps.data.IAppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CheckSumModule {

    @Provides
    fun provideCheckSumUseCases(appRepo: IAppRepository): ICheckSumUseCases {
        return object : ICheckSumUseCases{
            override val checkSum: CheckSumUseCase
                get() = CheckSumUseCase(appRepo)

            override val checkSumFormat: CheckSumFormatUseCase
                get() = CheckSumFormatUseCase()
        }
    }
}
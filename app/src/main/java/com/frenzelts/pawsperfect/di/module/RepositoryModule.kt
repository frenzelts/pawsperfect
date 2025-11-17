package com.frenzelts.pawsperfect.di.module

import com.frenzelts.pawsperfect.data.repository.DogRepositoryImpl
import com.frenzelts.pawsperfect.domain.repository.DogRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDogRepository(impl: DogRepositoryImpl): DogRepository

}
package com.frenzelts.dogguesser.di.module

import com.frenzelts.dogguesser.data.repository.DogRepositoryImpl
import com.frenzelts.dogguesser.domain.repository.DogRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDogRepository(impl: DogRepositoryImpl): DogRepository

}
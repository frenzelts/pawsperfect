package com.frenzelts.dogguesser.di

import com.frenzelts.dogguesser.MainActivity
import com.frenzelts.dogguesser.di.module.NetworkModule
import com.frenzelts.dogguesser.di.module.RepositoryModule
import com.frenzelts.dogguesser.di.viewmodel.ViewModelModule
import com.frenzelts.dogguesser.di.viewmodel.DaggerViewModelFactory
import com.frenzelts.dogguesser.presentation.quiz.QuizViewController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun viewModelFactory(): DaggerViewModelFactory

    fun inject(activity: MainActivity)
    fun inject(controller: QuizViewController)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}
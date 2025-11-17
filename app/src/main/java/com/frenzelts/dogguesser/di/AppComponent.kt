package com.frenzelts.dogguesser.di

import android.app.Application
import com.frenzelts.dogguesser.presentation.MainActivity
import com.frenzelts.dogguesser.di.module.NetworkModule
import com.frenzelts.dogguesser.di.module.PreferenceModule
import com.frenzelts.dogguesser.di.module.RepositoryModule
import com.frenzelts.dogguesser.di.viewmodel.ViewModelModule
import com.frenzelts.dogguesser.di.viewmodel.DaggerViewModelFactory
import com.frenzelts.dogguesser.presentation.home.HomeViewController
import com.frenzelts.dogguesser.presentation.quiz.QuizViewController
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        PreferenceModule::class,
    ]
)
interface AppComponent {

    fun viewModelFactory(): DaggerViewModelFactory

    fun inject(activity: MainActivity)
    fun inject(controller: HomeViewController)
    fun inject(controller: QuizViewController)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}
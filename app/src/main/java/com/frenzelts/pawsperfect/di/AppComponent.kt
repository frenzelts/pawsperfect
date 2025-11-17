package com.frenzelts.pawsperfect.di

import android.app.Application
import com.frenzelts.pawsperfect.presentation.MainActivity
import com.frenzelts.pawsperfect.di.module.NetworkModule
import com.frenzelts.pawsperfect.di.module.PreferenceModule
import com.frenzelts.pawsperfect.di.module.RepositoryModule
import com.frenzelts.pawsperfect.di.viewmodel.ViewModelModule
import com.frenzelts.pawsperfect.di.viewmodel.DaggerViewModelFactory
import com.frenzelts.pawsperfect.presentation.home.HomeViewController
import com.frenzelts.pawsperfect.presentation.quiz.QuizViewController
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
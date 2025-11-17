package com.frenzelts.pawsperfect.di.module

import android.app.Application
import com.frenzelts.pawsperfect.data.local.ScoreDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule {

    @Provides
    @Singleton
    fun provideScoreDataStore(app: Application): ScoreDataStore {
        return ScoreDataStore(app)
    }
}

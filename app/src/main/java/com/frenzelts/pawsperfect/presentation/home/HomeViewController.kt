package com.frenzelts.pawsperfect.presentation.home

import androidx.lifecycle.ViewModelProvider
import com.frenzelts.pawsperfect.data.local.ScoreDataStore
import com.frenzelts.pawsperfect.di.DIManager
import com.frenzelts.pawsperfect.presentation.common.BaseViewController
import javax.inject.Inject

class HomeViewController : BaseViewController<HomeViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var scoreStore: ScoreDataStore

    override fun injectComponent() {
        DIManager.createAppComponent(activity.application).inject(this)
    }

    override fun createViewModel(factory: ViewModelProvider.Factory) {
        viewModel = ViewModelProvider(activity, viewModelFactory)[HomeViewModel::class.java]
    }
}
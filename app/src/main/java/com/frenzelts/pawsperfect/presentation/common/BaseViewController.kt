package com.frenzelts.pawsperfect.presentation.common

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frenzelts.pawsperfect.di.DIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewController<VM: ViewModel>: DefaultLifecycleObserver {

    lateinit var activity: ComponentActivity
        private set

    var viewModel: VM? = null
        protected set

    private val controllerJob = SupervisorJob()
    val viewControllerScope = CoroutineScope(Dispatchers.Main + controllerJob)

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events

    fun init(activity: ComponentActivity) {
        this.activity = activity
        injectComponent()

        activity.lifecycle.addObserver(this)

        val factory = DIManager.createAppComponent(activity.application).viewModelFactory()
        createViewModel(factory)
    }

    open fun injectComponent() { }

    open fun createViewModel(factory: ViewModelProvider.Factory) { }

    fun sendEvent(event: UiEvent) {
        viewControllerScope.launch { _events.emit(event) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onScreenDestroyed()
        viewModel = null
        controllerJob.cancel()
        viewControllerScope.cancel()
        owner.lifecycle.removeObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onScreenStopped()
    }

    open fun onScreenStopped() { }
    open fun onScreenDestroyed() { }

    sealed class UiEvent {
        object HapticSuccess : UiEvent()
        object HapticError : UiEvent()
    }
}

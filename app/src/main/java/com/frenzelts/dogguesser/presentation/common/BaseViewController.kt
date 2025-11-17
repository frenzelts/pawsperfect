package com.frenzelts.dogguesser.presentation.common

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.frenzelts.dogguesser.di.DIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewController<VM> {

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

        // Create VM after injection
        val factory = DIManager.getAppComponent().viewModelFactory()
        createViewModel(factory)
    }

    open fun injectComponent() { }

    open fun createViewModel(factory: ViewModelProvider.Factory) { }

    open fun onDestroy() {
        viewModel = null
        controllerJob.cancel()
        viewControllerScope.cancel()
    }

    fun sendEvent(event: UiEvent) {
        viewControllerScope.launch { _events.emit(event) }
    }

    sealed class UiEvent {
        data class Snackbar(val message: String) : UiEvent()
        object HapticSuccess : UiEvent()
        object HapticError : UiEvent()
        data class Navigate(val route: String) : UiEvent()
    }
}

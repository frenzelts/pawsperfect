package com.frenzelts.dogguesser.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.frenzelts.dogguesser.presentation.common.BaseViewController

object ViewControllerUtil {

    @Composable
    inline fun <reified VM: ViewModel, VC: BaseViewController<VM>> rememberViewController(
        vmKey: String? = "",
        crossinline factory: @DisallowComposableCalls (ComponentActivity) -> VC
    ): VC? {
        val activity = rememberFindActivity() ?: return null
        val viewController = remember(vmKey) {
            factory(activity).apply {
                init(activity)
            }
        }
        return viewController
    }

    @Composable
    fun rememberFindActivity(): ComponentActivity? {
        val context = LocalContext.current
        return LocalActivity.current ?: remember(context) {
            findActivity(context)
        }
    }

    private fun findActivity(context: Context): ComponentActivity? {
        var tempContext = context
        while (tempContext !is ComponentActivity && tempContext is ContextWrapper) tempContext = tempContext.baseContext
        return context as? ComponentActivity
    }
}
package com.frenzelts.pawsperfect.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.frenzelts.pawsperfect.R
import java.util.Locale.getDefault

object StringUtil {

    @Composable
    fun getAppName() = stringResource(R.string.app_name)

    fun String.capitalize(): String = replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(getDefault())
        else it.toString()
    }
}
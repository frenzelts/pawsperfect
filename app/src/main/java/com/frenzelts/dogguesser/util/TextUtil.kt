package com.frenzelts.dogguesser.util

import java.util.Locale.getDefault

object TextUtil {

    fun String.capitalize(): String = replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(getDefault())
        else it.toString()
    }
}
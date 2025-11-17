package com.frenzelts.pawsperfect.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.scoreDataStore by preferencesDataStore("score_prefs")

class ScoreDataStore(private val context: Context) {

    companion object {
        private val KEY_HIGH_SCORE = intPreferencesKey("high_score")
        private val KEY_HIGH_STREAK = intPreferencesKey("high_streak")
    }

    val highestScore: Flow<Int> = context.scoreDataStore.data
        .map { it[KEY_HIGH_SCORE] ?: 0 }

    val highestStreak: Flow<Int> = context.scoreDataStore.data
        .map { it[KEY_HIGH_STREAK] ?: 0 }

    suspend fun saveHighScore(score: Int) {
        context.scoreDataStore.edit { prefs ->
            val current = prefs[KEY_HIGH_SCORE] ?: 0
            if (score > current) prefs[KEY_HIGH_SCORE] = score
        }
    }

    suspend fun saveHighStreak(streak: Int) {
        context.scoreDataStore.edit { prefs ->
            val current = prefs[KEY_HIGH_STREAK] ?: 0
            if (streak > current) prefs[KEY_HIGH_STREAK] = streak
        }
    }
}

package com.zurui.flotilla.global

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

/**
 * PlayerData persists game data across instances of game (for continuing).
 * Ingame data usage is also maintained here.
 *
 * Windows: %UserProfile%/.prefs/My Preferences
 * Linux and OSX: ~/.prefs/My Preferences
 */
object PlayerData {
    val prefs: Preferences = Gdx.app.getPreferences("flotilla_player_data")

    init {
        prefs.flush()
    }


    fun clear() {
        prefs.clear()
    }
}

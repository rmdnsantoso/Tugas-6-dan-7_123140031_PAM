package pam.tugas5.romadhon.database

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class SettingsManager(private val settings: Settings) {

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_SORT = "sort_order"
    }

    var theme: String
        get() = settings[KEY_THEME, "System"]
        set(value) { settings[KEY_THEME] = value }

    var sortOrder: String
        get() = settings[KEY_SORT, "Terbaru"]
        set(value) { settings[KEY_SORT] = value }
}
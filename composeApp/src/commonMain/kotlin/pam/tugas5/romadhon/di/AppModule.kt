package pam.tugas5.romadhon.di

import com.russhwolf.settings.Settings
import org.koin.dsl.module
import pam.tugas5.romadhon.BatteryInfo
import pam.tugas5.romadhon.DeviceInfo
import pam.tugas5.romadhon.NetworkMonitor
import pam.tugas5.romadhon.database.DatabaseDriverFactory
import pam.tugas5.romadhon.database.DatabaseProvider
import pam.tugas5.romadhon.database.SettingsManager
import pam.tugas5.romadhon.repository.NoteRepository
import pam.tugas5.romadhon.screens.NotesViewModel

fun appModule(driverFactory: DatabaseDriverFactory) = module {
    single { driverFactory }
    single { DatabaseProvider.getDatabase(get()) }
    single { NoteRepository(get()) }
    single { SettingsManager(Settings()) }
    single { NotesViewModel(get()) }
    single { DeviceInfo() }
    single { NetworkMonitor() }
    single { BatteryInfo() }
}
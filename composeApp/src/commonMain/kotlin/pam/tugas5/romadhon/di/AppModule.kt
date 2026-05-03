package pam.tugas5.romadhon.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import pam.tugas5.romadhon.BatteryInfo
import pam.tugas5.romadhon.DeviceInfo
import pam.tugas5.romadhon.NetworkMonitor
import pam.tugas5.romadhon.ai.GeminiService
import pam.tugas5.romadhon.database.DatabaseDriverFactory
import pam.tugas5.romadhon.database.DatabaseProvider
import pam.tugas5.romadhon.database.SettingsManager
import pam.tugas5.romadhon.repository.NoteRepository
import pam.tugas5.romadhon.screens.NotesViewModel

fun appModule(driverFactory: DatabaseDriverFactory) = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
        }
    }

    single { driverFactory }
    single { DatabaseProvider.getDatabase(get()) }
    single { NoteRepository(get()) }
    single { SettingsManager(Settings()) }
    single { DeviceInfo() }
    single { NetworkMonitor() }
    single { BatteryInfo() }
    single { GeminiService(get()) }
    single { NotesViewModel(get(), get()) }
}
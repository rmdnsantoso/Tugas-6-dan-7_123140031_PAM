package pam.tugas5.romadhon

import androidx.compose.ui.window.ComposeUIViewController
import pam.tugas5.romadhon.database.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { App(DatabaseDriverFactory()) }
package pam.tugas6dan7.romadhon

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tugas 6 dan 7_123140031_PAM",
    ) {
        App()
    }
}
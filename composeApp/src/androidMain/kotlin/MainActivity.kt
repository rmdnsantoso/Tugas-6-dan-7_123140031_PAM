package pam.tugas6dan7.romadhon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pam.tugas6dan7.romadhon.database.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(driverFactory = DatabaseDriverFactory(applicationContext))
        }
    }
}
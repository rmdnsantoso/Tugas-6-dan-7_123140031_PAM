package pam.tugas5.romadhon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pam.tugas5.romadhon.database.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(DatabaseDriverFactory(this))
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(driverFactory = DatabaseDriverFactory(androidx.compose.ui.platform.LocalContext.current))
}
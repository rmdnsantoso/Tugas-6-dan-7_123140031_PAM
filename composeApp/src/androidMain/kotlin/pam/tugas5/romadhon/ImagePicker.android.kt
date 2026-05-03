package pam.tugas5.romadhon

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
actual fun rememberImagePickerLauncher(onResult: (String?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val base64 = uriToBase64(context, uri)
            onResult(base64)
        } else {
            onResult(null)
        }
    }
    return { launcher.launch("image/*") }
}

@Composable
actual fun rememberCameraLauncher(onResult: (String?) -> Unit): () -> Unit {
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            val base64 = uriToBase64(context, tempUri!!)
            onResult(base64)
        } else {
            onResult(null)
        }
    }

    return {
        val tempFile = File.createTempFile("photo_", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
        tempUri = uri
        launcher.launch(uri)
    }
}
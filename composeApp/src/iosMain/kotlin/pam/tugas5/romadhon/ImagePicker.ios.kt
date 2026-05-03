package pam.tugas5.romadhon

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(onResult: (String?) -> Unit): () -> Unit {
    return { onResult(null) }
}

@Composable
actual fun rememberCameraLauncher(onResult: (String?) -> Unit): () -> Unit {
    return { onResult(null) }
}
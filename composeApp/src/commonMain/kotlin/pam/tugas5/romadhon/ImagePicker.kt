package pam.tugas5.romadhon

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onResult: (String?) -> Unit): () -> Unit

@Composable
expect fun rememberCameraLauncher(onResult: (String?) -> Unit): () -> Unit
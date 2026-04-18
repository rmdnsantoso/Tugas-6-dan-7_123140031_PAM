package pam.tugas5.romadhon.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")

    object NoteDetail : Screen("note_detail/{noteId}") {
        // Ubah tipe parameter dari Int menjadi Long
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        // Ubah tipe parameter dari Int menjadi Long
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }

    object Settings : Screen("settings")
}

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Notes : BottomNavItem("notes_tab", Icons.Default.Home, "Notes")
    object Favorites : BottomNavItem("favorites_tab", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile_tab", Icons.Default.Person, "Profile")
}
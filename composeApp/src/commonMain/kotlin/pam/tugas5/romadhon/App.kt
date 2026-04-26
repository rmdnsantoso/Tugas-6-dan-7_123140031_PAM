package pam.tugas5.romadhon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import pam.tugas5.romadhon.components.BottomNavBar
import pam.tugas5.romadhon.database.DatabaseDriverFactory
import pam.tugas5.romadhon.database.SettingsManager
import pam.tugas5.romadhon.di.appModule
import pam.tugas5.romadhon.navigation.BottomNavItem
import pam.tugas5.romadhon.navigation.Screen
import pam.tugas5.romadhon.screens.AddNoteScreen
import pam.tugas5.romadhon.screens.EditNoteScreen
import pam.tugas5.romadhon.screens.FavoritesScreen
import pam.tugas5.romadhon.screens.NoteDetailScreen
import pam.tugas5.romadhon.screens.NoteListScreen
import pam.tugas5.romadhon.screens.NotesViewModel
import pam.tugas5.romadhon.screens.ProfileScreen
import pam.tugas5.romadhon.screens.SettingsScreen

val TemaHijau = Color(0xFF2E7D32)

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    // 1. Bungkus seluruh aplikasi dengan KoinApplication untuk menyalakan mesin DI
    KoinApplication(application = {
        modules(appModule(driverFactory))
    }) {
        // 2. Ambil ViewModel & SettingsManager secara otomatis menggunakan koinInject()
        val viewModel: NotesViewModel = koinInject()
        val settingsManager: SettingsManager = koinInject()

        var appTheme by remember { mutableStateOf(settingsManager.theme) }
        var appSortOrder by remember { mutableStateOf(settingsManager.sortOrder) }

        val isDark = when (appTheme) {
            "Light" -> false
            "Dark" -> true
            else -> isSystemInDarkTheme()
        }

        val colorScheme = if (isDark) {
            darkColorScheme(
                primary = TemaHijau,
                background = Color(0xFF121212),
                surface = Color(0xFF1E1E1E),
                onBackground = Color.White,
                onSurface = Color.White
            )
        } else {
            lightColorScheme(
                primary = TemaHijau,
                background = Color(0xFFF5F5F5),
                surface = Color.White,
                onBackground = Color.Black,
                onSurface = Color.Black
            )
        }

        MaterialTheme(colorScheme = colorScheme) {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.surface
                    ) {
                        Text("Menu Utama", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface)
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                        NavigationDrawerItem(
                            label = { Text("List Catatan") },
                            selected = false,
                            onClick = {
                                navController.navigate(BottomNavItem.Notes.route)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )

                        NavigationDrawerItem(
                            label = { Text("Profil Pengguna") },
                            selected = false,
                            onClick = {
                                navController.navigate(BottomNavItem.Profile.route)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )

                        NavigationDrawerItem(
                            label = { Text("Pengaturan") },
                            selected = false,
                            onClick = {
                                navController.navigate(Screen.Settings.route)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            ) {
                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Notes.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(BottomNavItem.Notes.route) {
                            NoteListScreen(
                                viewModel = viewModel,
                                currentSortOrder = appSortOrder,
                                onNavigateToDetail = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                                onNavigateToAdd = { navController.navigate(Screen.AddNote.route) }
                            )
                        }

                        composable(BottomNavItem.Favorites.route) {
                            FavoritesScreen(viewModel = viewModel)
                        }

                        composable(BottomNavItem.Profile.route) {
                            ProfileScreen()
                        }

                        composable(Screen.Settings.route) {
                            SettingsScreen(
                                settingsManager = settingsManager,
                                onThemeChanged = { appTheme = it },
                                onSortChanged = { appSortOrder = it },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.NoteDetail.route,
                            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                            NoteDetailScreen(
                                noteId = noteId,
                                viewModel = viewModel,
                                onNavigateToEdit = { id -> navController.navigate(Screen.EditNote.createRoute(id)) },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.AddNote.route) {
                            AddNoteScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                        }

                        composable(
                            route = Screen.EditNote.route,
                            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                            EditNoteScreen(
                                noteId = noteId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
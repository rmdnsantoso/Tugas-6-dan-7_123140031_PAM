package pam.tugas6dan7.romadhon

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pam.tugas6dan7.romadhon.data.HttpClientFactory
import pam.tugas6dan7.romadhon.data.NewsRepository
import pam.tugas6dan7.romadhon.database.DatabaseDriverFactory
import pam.tugas6dan7.romadhon.database.NewsDatabase
import pam.tugas6dan7.romadhon.screens.BookmarkScreen
import pam.tugas6dan7.romadhon.screens.NewsDetailScreen
import pam.tugas6dan7.romadhon.screens.NewsScreen
import pam.tugas6dan7.romadhon.viewmodel.NewsViewModel

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    val database = remember { NewsDatabase(driverFactory.createDriver()) }
    val client = remember { HttpClientFactory.create() }
    val repository = remember { NewsRepository(client, database) }
    val viewModel = remember { NewsViewModel(repository) }
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController, startDestination = "home") {
            composable("home") {
                NewsScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { navController.navigate("detail") },
                    onNavigateToBookmark = { navController.navigate("bookmark") }
                )
            }
            composable("detail") {
                NewsDetailScreen(viewModel) { navController.popBackStack() }
            }
            composable("bookmark") {
                BookmarkScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { navController.navigate("detail") },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
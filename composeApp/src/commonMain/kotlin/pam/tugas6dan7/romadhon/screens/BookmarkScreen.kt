package pam.tugas6dan7.romadhon.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pam.tugas6dan7.romadhon.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(viewModel: NewsViewModel, onNavigateToDetail: () -> Unit, onBack: () -> Unit) {
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchBookmarks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Berita Tersimpan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (bookmarkedArticles.isEmpty()) {
                Text(
                    "Belum ada berita yang disimpan.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookmarkedArticles) { article ->
                        ArticleItem(article = article) {
                            viewModel.selectArticle(article)
                            onNavigateToDetail()
                        }
                    }
                }
            }
        }
    }
}
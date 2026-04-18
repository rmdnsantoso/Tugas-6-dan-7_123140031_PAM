package pam.tugas6dan7.romadhon.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import pam.tugas6dan7.romadhon.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(viewModel: NewsViewModel, onBack: () -> Unit) {
    val article by viewModel.selectedArticle.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Berita") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = { article?.let { viewModel.toggleBookmark(it) } }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        article?.let { item ->
            Column(Modifier.padding(padding).verticalScroll(rememberScrollState())) {
                item.urlToImage?.let {
                    KamelImage(
                        asyncPainterResource(it),
                        null,
                        Modifier.height(250.dp).fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(Modifier.padding(16.dp)) {
                    Text(item.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Published: ${item.publishedAt.take(10)}", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(16.dp))
                    Text(item.description ?: "Tidak ada deskripsi tersedia untuk berita ini.")
                }
            }
        }
    }
}
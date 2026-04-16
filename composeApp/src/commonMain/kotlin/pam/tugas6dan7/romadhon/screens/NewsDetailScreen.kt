package pam.tugas6dan7.romadhon.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
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
                    Text("Published: ${item.publishedAt}", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(16.dp))
                    Text(item.description ?: "No Description")
                }
            }
        }
    }
}
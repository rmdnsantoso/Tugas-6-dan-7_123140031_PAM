package pam.tugas6dan7.romadhon.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import pam.tugas6dan7.romadhon.data.Article
import pam.tugas6dan7.romadhon.viewmodel.NewsUiState
import pam.tugas6dan7.romadhon.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel, onNavigateToDetail: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("World in Bytes", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.fetchNews() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is NewsUiState.Error -> {
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = Color.Red)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchNews() }) { Text("Coba Lagi") }
                    }
                }
                is NewsUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (state.articles.isNotEmpty()) {
                            // 1. HERO LAYOUT (Artikel Pertama / Index 0)
                            item {
                                HeroArticleItem(article = state.articles[0]) {
                                    viewModel.selectArticle(state.articles[0])
                                    onNavigateToDetail()
                                }
                            }

                            // 2. REGULAR LAYOUT (Artikel sisanya)
                            items(state.articles.drop(1)) { article ->
                                ArticleItem(article) {
                                    viewModel.selectArticle(article)
                                    onNavigateToDetail()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Komponen Khusus Berita Utama (Hero)
@Composable
fun HeroArticleItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            // Gambar Background Penuh
            article.urlToImage?.let {
                KamelImage(
                    resource = asyncPainterResource(it),
                    contentDescription = article.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Efek Gradasi Hitam di bagian bawah agar teks terbaca jelas
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                            startY = 100f
                        )
                    )
            )

            // Teks Berita Utama di pojok kiri bawah
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text("HOT NEWS", color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.publishedAt.take(10), // Ambil tanggalnya saja
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.LightGray
                )
            }
        }
    }
}

// Komponen Berita Biasa
@Composable
fun ArticleItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            article.urlToImage?.let {
                KamelImage(
                    resource = asyncPainterResource(it),
                    contentDescription = article.title,
                    modifier = Modifier.height(180.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(12.dp))
            }
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = article.description ?: "Tidak ada deskripsi",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
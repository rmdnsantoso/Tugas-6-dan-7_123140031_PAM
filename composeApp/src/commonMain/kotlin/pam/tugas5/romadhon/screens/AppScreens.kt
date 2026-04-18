package pam.tugas5.romadhon.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import tugas5_pam_123140031.composeapp.generated.resources.Res
import tugas5_pam_123140031.composeapp.generated.resources.foto_romadhon
import pam.tugas5.romadhon.database.NoteEntity
import pam.tugas5.romadhon.database.SettingsManager

val TemaHijau = Color(0xFF2E7D32)
val CardColors = listOf(0xFFE8F5E9, 0xFFC8E6C9, 0xFFA5D6A7, 0xFF81C784)

@Composable
fun NoteListScreen(
    viewModel: NotesViewModel,
    currentSortOrder: String,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    val rawNotes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val sortedNotes = when (currentSortOrder) {
        "Terlama" -> rawNotes.sortedBy { it.id }
        "A-Z" -> rawNotes.sortedBy { it.title.lowercase() }
        else -> rawNotes.sortedByDescending { it.id }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = TemaHijau,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, bottom = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Catatan Romadhon",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TemaHijau
                )

                IconButton(
                    onClick = { viewModel.syncNotesFromApi() },
                    enabled = !isSyncing
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TemaHijau)
                    } else {
                        Icon(Icons.Filled.Refresh, contentDescription = null, tint = TemaHijau)
                    }
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Cari catatan kamu...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TemaHijau) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = TemaHijau,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sortedNotes) { note ->
                    val isFavorite = note.is_favorite == 1L

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToDetail(note.id) },
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(note.color.toInt()))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.DarkGray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = note.date, fontSize = 12.sp, color = TemaHijau)
                            }
                            IconButton(
                                onClick = { viewModel.toggleFavorite(note.id, note.is_favorite) }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFavorite) Color.Red else TemaHijau
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(viewModel: NotesViewModel) {
    val favoriteNotes by viewModel.favoriteNotes.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Text(
            text = "Koleksi Favorit",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TemaHijau,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
        )

        if (favoriteNotes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f), modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Belum ada favorit nih", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontSize = 18.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteNotes) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(note.color.toInt()))
                    ) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = note.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = note.content, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.DarkGray)
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(note.id, note.is_favorite) }) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(140.dp).clip(CircleShape).background(TemaHijau.copy(alpha = 0.1f))
        ) {
            Image(
                painter = painterResource(Res.drawable.foto_romadhon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("M. Romadhon Santoso", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TemaHijau)
        Text("123140031 | Teknik Informatika ITERA", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = TemaHijau.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfoRow(Icons.Filled.AccountTree, "Student in Teknik Informatika ITERA")
        ProfileInfoRow(Icons.Filled.Code, "Web Developer")
        ProfileInfoRow(Icons.Filled.School, "Institut Teknologi Sumatera")
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = TemaHijau, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun NoteDetailScreen(noteId: Long, viewModel: NotesViewModel, onNavigateToEdit: (Long) -> Unit, onBack: () -> Unit) {
    val notes by viewModel.notes.collectAsState()
    val note = notes.find { it.id == noteId }

    if (note == null) {
        onBack()
        return
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Detail", color = Color(0xFF1B5E20)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color(0xFF1B5E20)) } },
                actions = {
                    IconButton(onClick = { viewModel.deleteNote(noteId); onBack() }) { Icon(Icons.Filled.Delete, null, tint = Color.Red) }
                    IconButton(onClick = { onNavigateToEdit(noteId) }) { Icon(Icons.Filled.Edit, null, tint = Color(0xFF1B5E20)) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(note.color.toInt()))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(Color(note.color.toInt())).padding(padding).padding(20.dp)) {
            Text(text = note.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text(text = note.date, color = TemaHijau, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = note.content, fontSize = 17.sp, lineHeight = 26.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun AddNoteScreen(viewModel: NotesViewModel, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Baru", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TemaHijau)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TemaHijau,
                    focusedLabelColor = TemaHijau,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content, onValueChange = { content = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TemaHijau,
                    focusedLabelColor = TemaHijau,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val randomColor = CardColors.random()
                    viewModel.addNote(title, content, "April 2026", randomColor)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TemaHijau),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Simpan", color = Color.White)
            }
        }
    }
}

@Composable
fun EditNoteScreen(noteId: Long, viewModel: NotesViewModel, onBack: () -> Unit) {
    val notes by viewModel.notes.collectAsState()
    val note = notes.find { it.id == noteId }

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Edit", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TemaHijau)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TemaHijau,
                    focusedLabelColor = TemaHijau,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content, onValueChange = { content = it },
                label = { Text("Isi") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TemaHijau,
                    focusedLabelColor = TemaHijau,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateNote(noteId, title, content)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TemaHijau),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Update", color = Color.White)
            }
        }
    }
}

@Composable
fun SettingsScreen(
    settingsManager: SettingsManager,
    onThemeChanged: (String) -> Unit, // Tambahan parameter dari App.kt
    onSortChanged: (String) -> Unit,  // Tambahan parameter dari App.kt
    onBack: () -> Unit
) {
    var selectedTheme by remember { mutableStateOf(settingsManager.theme) }
    var selectedSort by remember { mutableStateOf(settingsManager.sortOrder) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Pengaturan", color = Color.White) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TemaHijau)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text("Tema Aplikasi", fontWeight = FontWeight.Bold, color = TemaHijau)
            listOf("Light", "Dark", "System").forEach { theme ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().clickable {
                        selectedTheme = theme
                        settingsManager.theme = theme
                        onThemeChanged(theme) // Langsung beri tahu layar utama!
                    }.padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedTheme == theme),
                        onClick = {
                            selectedTheme = theme
                            settingsManager.theme = theme
                            onThemeChanged(theme) // Langsung beri tahu layar utama!
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = TemaHijau)
                    )
                    Text(theme, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Urutan Catatan", fontWeight = FontWeight.Bold, color = TemaHijau)
            listOf("Terbaru", "Terlama", "A-Z").forEach { sort ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().clickable {
                        selectedSort = sort
                        settingsManager.sortOrder = sort
                        onSortChanged(sort) // Langsung urutkan layar utama!
                    }.padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedSort == sort),
                        onClick = {
                            selectedSort = sort
                            settingsManager.sortOrder = sort
                            onSortChanged(sort) // Langsung urutkan layar utama!
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = TemaHijau)
                    )
                    Text(sort, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}
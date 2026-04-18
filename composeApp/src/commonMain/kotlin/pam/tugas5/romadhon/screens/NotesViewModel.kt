package pam.tugas5.romadhon.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pam.tugas5.romadhon.database.NoteEntity
import pam.tugas5.romadhon.repository.NoteRepository

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val notes: StateFlow<List<NoteEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllNotes()
            } else {
                repository.searchNotes(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteNotes: StateFlow<List<NoteEntity>> = repository.getFavoriteNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    fun syncNotesFromApi() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.syncWithRemoteApi()
            _isSyncing.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, content: String, date: String, color: Long) {
        viewModelScope.launch {
            repository.insertNote(title, content, date, color, 0)
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNote(id, title, content)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun toggleFavorite(id: Long, currentStatus: Long) {
        viewModelScope.launch {
            val newStatus = if (currentStatus == 1L) 0L else 1L
            repository.toggleFavorite(id, newStatus)
        }
    }
}
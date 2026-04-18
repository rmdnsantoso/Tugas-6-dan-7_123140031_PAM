package pam.tugas6dan7.romadhon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pam.tugas6dan7.romadhon.data.Article
import pam.tugas6dan7.romadhon.data.NewsRepository

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked = _isBookmarked.asStateFlow()

    private val _bookmarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    val bookmarkedArticles = _bookmarkedArticles.asStateFlow()

    init { fetchNews() }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getTopHeadlines()
                .onSuccess { _uiState.value = NewsUiState.Success(it) }
                .onFailure { _uiState.value = NewsUiState.Error(it.message ?: "Error Connection") }
        }
    }

    fun selectArticle(article: Article?) {
        _selectedArticle.value = article
        if (article != null) {
            _isBookmarked.value = repository.isBookmarked(article.url)
        }
    }

    fun toggleBookmark(article: Article) {
        if (_isBookmarked.value) {
            repository.removeBookmark(article.url)
            _isBookmarked.value = false
        } else {
            repository.saveBookmark(article)
            _isBookmarked.value = true
        }
        fetchBookmarks()
    }

    fun fetchBookmarks() {
        _bookmarkedArticles.value = repository.getAllBookmarks()
    }
}
package pam.tugas5.romadhon.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import pam.tugas5.romadhon.database.NoteEntity
import pam.tugas5.romadhon.database.NotesDatabase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class NoteRepository(database: NotesDatabase) {
    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<NoteEntity>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun getFavoriteNotes(): Flow<List<NoteEntity>> {
        return queries.selectFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun searchNotes(query: String): Flow<List<NoteEntity>> {
        return queries.searchNotes(query)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun getNoteById(id: Long): Flow<NoteEntity?> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
    }

    fun insertNote(title: String, content: String, date: String, color: Long, isFavorite: Long = 0) {
        queries.insertNote(
            title = title,
            content = content,
            date = date,
            color = color,
            is_favorite = isFavorite
        )
    }

    fun updateNote(id: Long, title: String, content: String) {
        queries.updateNote(
            title = title,
            content = content,
            id = id
        )
    }

    fun toggleFavorite(id: Long, isFavorite: Long) {
        queries.toggleFavorite(
            is_favorite = isFavorite,
            id = id
        )
    }

    fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    suspend fun syncWithRemoteApi() {
        try {
            val client = HttpClient()
            val response = client.get("https://jsonplaceholder.typicode.com/posts/1")
            val jsonString = response.bodyAsText()
            val titleMatch = "\"title\": \"(.*?)\"".toRegex().find(jsonString)
            val bodyMatch = "\"body\": \"(.*?)\"".toRegex().find(jsonString)

            if (titleMatch != null && bodyMatch != null) {
                val remoteTitle = titleMatch.groupValues[1]
                val remoteBody = bodyMatch.groupValues[1].replace("\\n", " ")
                queries.insertNote(
                    title = "[API] ${remoteTitle.take(20)}...",
                    content = remoteBody,
                    date = "Synced Data",
                    color = 0xFFC8E6C9,
                    is_favorite = 0
                )
            }
            client.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
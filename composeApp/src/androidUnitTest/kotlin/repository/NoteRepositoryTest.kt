package pam.tugas5.romadhon.repository

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import pam.tugas5.romadhon.database.NoteQueries
import pam.tugas5.romadhon.database.NotesDatabase
import kotlin.test.BeforeTest
import kotlin.test.Test

class NoteRepositoryTest {

    private lateinit var database: NotesDatabase
    private lateinit var queries: NoteQueries
    private lateinit var repository: NoteRepository

    @BeforeTest
    fun setup() {
        // 1. Membuat "objek palsu" (mock) dari Database dan Queries
        database = mockk(relaxed = true)
        queries = mockk(relaxed = true)

        // 2. Mengatur agar saat repository meminta noteQueries, berikan queries palsu kita
        every { database.noteQueries } returns queries

        // 3. Memasukkan database palsu ke dalam Repository
        repository = NoteRepository(database)
    }

    @Test
    fun insertNote_calls_insertNote_query() {
        every { queries.insertNote(any(), any(), any(), any(), any()) } just Runs

        repository.insertNote("Judul Keren", "Isi Catatan", "Mei 2026", 0L, 1L)

        verify { queries.insertNote("Judul Keren", "Isi Catatan", "Mei 2026", 0L, 1L) }
    }

    @Test
    fun updateNote_calls_updateNote_query() {
        every { queries.updateNote(any(), any(), any()) } just Runs

        repository.updateNote(1L, "Judul Baru", "Isi Baru")

        verify { queries.updateNote("Judul Baru", "Isi Baru", 1L) }
    }

    @Test
    fun deleteNote_calls_deleteNote_query() {
        every { queries.deleteNote(any()) } just Runs

        repository.deleteNote(99L)

        verify { queries.deleteNote(99L) }
    }

    @Test
    fun toggleFavorite_calls_toggleFavorite_query() {
        every { queries.toggleFavorite(any(), any()) } just Runs

        repository.toggleFavorite(1L, 1L)

        verify { queries.toggleFavorite(1L, 1L) }
    }

    @Test
    fun syncWithRemoteApi_executes_without_crashing() = runTest {
        repository.syncWithRemoteApi()
    }
}
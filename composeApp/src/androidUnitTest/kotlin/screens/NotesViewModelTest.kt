package pam.tugas5.romadhon.screens

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pam.tugas5.romadhon.ai.GeminiService
import pam.tugas5.romadhon.repository.NoteRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private lateinit var repository: NoteRepository
    private lateinit var geminiService: GeminiService
    private lateinit var viewModel: NotesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        geminiService = mockk(relaxed = true)

        every { repository.getAllNotes() } returns MutableStateFlow(emptyList())
        every { repository.getFavoriteNotes() } returns MutableStateFlow(emptyList())

        viewModel = NotesViewModel(repository, geminiService)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addNote_calls_repository_insertNote() = runTest {
        every { repository.insertNote(any(), any(), any(), any(), any()) } just Runs

        viewModel.addNote("Catatan Baru", "Isi Catatan", "Mei 2026", 0L)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { repository.insertNote("Catatan Baru", "Isi Catatan", "Mei 2026", 0L, 0) }
    }

    @Test
    fun deleteNote_calls_repository_deleteNote() = runTest {
        every { repository.deleteNote(any()) } just Runs

        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { repository.deleteNote(1L) }
    }

    @Test
    fun toggleFavorite_calls_repository_toggleFavorite() = runTest {
        every { repository.toggleFavorite(any(), any()) } just Runs

        viewModel.toggleFavorite(1L, 0L)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { repository.toggleFavorite(1L, 1L) }
    }

    @Test
    fun updateSearchQuery_updates_searchQuery_state_via_turbine() = runTest {
        viewModel.searchQuery.test {
            assertEquals("", awaitItem())

            viewModel.updateSearchQuery("Kotlin Multiplatform")
            assertEquals("Kotlin Multiplatform", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun syncNotesFromApi_updates_isSyncing_state_via_turbine() = runTest {
        coEvery { repository.syncWithRemoteApi() } just Runs

        viewModel.isSyncing.test {
            assertEquals(false, awaitItem())

            viewModel.syncNotesFromApi()

            assertEquals(true, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(false, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
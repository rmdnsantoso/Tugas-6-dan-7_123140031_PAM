package pam.tugas5.romadhon.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import pam.tugas5.romadhon.NetworkMonitor
import pam.tugas5.romadhon.ai.GeminiService
import pam.tugas5.romadhon.database.NoteEntity
import pam.tugas5.romadhon.repository.NoteRepository

class NotesScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // Companion object ini akan menjalankan Koin 1x saja untuk seluruh file test
    companion object {
        @JvmStatic
        @BeforeClass
        fun initKoin() {
            stopKoin() // Bersihkan sisa-sisa Koin jika ada
            startKoin {
                modules(module {
                    single<NetworkMonitor> { mockk(relaxed = true) }
                })
            }
        }

        @JvmStatic
        @AfterClass
        fun teardownKoin() {
            stopKoin() // Matikan dengan tenang setelah semua test selesai
        }
    }

    private lateinit var mockRepo: NoteRepository
    private lateinit var mockGemini: GeminiService
    private lateinit var viewModel: NotesViewModel

    private fun setupViewModel(initialNotes: List<NoteEntity> = emptyList()) {
        mockRepo = mockk(relaxed = true)
        mockGemini = mockk(relaxed = true)
        every { mockRepo.getAllNotes() } returns MutableStateFlow(initialNotes)
        every { mockRepo.getFavoriteNotes() } returns MutableStateFlow(emptyList())
        viewModel = NotesViewModel(mockRepo, mockGemini)
    }

    @Test
    fun emptyState_showsMessage() {
        setupViewModel(emptyList())

        rule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                currentSortOrder = "Terbaru",
                onNavigateToDetail = {},
                onNavigateToAdd = {}
            )
        }

        rule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
    }

    @Test
    fun notesList_showsNotes() {
        val fakeNote = NoteEntity(1L, "Tugas KMP", "Selesaikan UI Testing", "Mei 2026", 0xFFE8F5E9, 0L)
        setupViewModel(listOf(fakeNote))

        rule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                currentSortOrder = "Terbaru",
                onNavigateToDetail = {},
                onNavigateToAdd = {}
            )
        }

        rule.onNodeWithText("Tugas KMP").assertIsDisplayed()
        rule.onNodeWithTag(TestTags.NOTE_ITEM).assertIsDisplayed()
    }

    @Test
    fun searchInput_exists_and_acceptsText() {
        setupViewModel(emptyList())

        rule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                currentSortOrder = "Terbaru",
                onNavigateToDetail = {},
                onNavigateToAdd = {}
            )
        }

        rule.onNodeWithTag(TestTags.SEARCH_INPUT)
            .assertIsDisplayed()
            .performTextInput("Kotlin")

        rule.onNodeWithTag(TestTags.SEARCH_INPUT)
            .assertTextContains("Kotlin")
    }

    @Test
    fun addNote_screen_displays_inputs_and_button() {
        setupViewModel(emptyList())

        rule.setContent {
            AddNoteScreen(
                viewModel = viewModel,
                onBack = {}
            )
        }

        rule.onNodeWithTag(TestTags.TITLE_INPUT).assertIsDisplayed().performTextInput("Judul Baru")
        rule.onNodeWithTag(TestTags.CONTENT_INPUT).assertIsDisplayed().performTextInput("Isi Baru")
        rule.onNodeWithTag(TestTags.ADD_BUTTON).assertIsDisplayed().assertIsEnabled()
    }
}
package com.vitorfg8.quizia.feature.settings

import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val mockSettingsRepository = mockk<SettingsRepository>(relaxed = true)
    private val mockApiKeyRepository = mockk<ApiKeyRepository>(relaxed = true)
    private val mockOnDeviceModelRepository = mockk<OnDeviceModelRepository>()
    private val settingsFlow = MutableStateFlow(AppSettings(isFirstRun = false))

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { mockSettingsRepository.observeSettings() } returns settingsFlow
        coEvery { mockOnDeviceModelRepository.isAvailable() } returns false
        every { mockApiKeyRepository.getApiKey(any()) } returns null
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the stored settings and masked key are shown on open`() {
        settingsFlow.value = AppSettings(
            selectedProvider = LlmProviderType.OPENAI,
            theme = AppTheme.DARK,
            questionCount = 10,
            isFirstRun = false,
        )
        every { mockApiKeyRepository.getApiKey(LlmProviderType.OPENAI) } returns INPUT_STORED_KEY
        val actual = createViewModel().uiState.value
        assertEquals(LlmProviderType.OPENAI, actual.selectedProvider)
        assertEquals(AppTheme.DARK, actual.theme)
        assertEquals(10, actual.questionCount)
        assertEquals(maskApiKey(INPUT_STORED_KEY), actual.apiKeyMasked)
        assertFalse(actual.isEditingApiKey)
        assertEquals(
            listOf(LlmProviderType.GEMINI_API, LlmProviderType.OPENAI, LlmProviderType.CLAUDE),
            actual.availableProviders,
        )
    }

    @Test
    fun `gemini nano is offered when the on-device model is available`() {
        coEvery { mockOnDeviceModelRepository.isAvailable() } returns true
        val actual = createViewModel().uiState.value.availableProviders
        assertEquals(LlmProviderType.entries, actual)
    }

    @Test
    fun `selecting gemini nano hides the api key field`() {
        coEvery { mockOnDeviceModelRepository.isAvailable() } returns true
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.GEMINI_NANO)
        val actual = viewModel.uiState.value
        assertFalse(actual.isApiKeyRequired)
        assertFalse(actual.isEditingApiKey)
        coVerify { mockSettingsRepository.saveSelectedProvider(LlmProviderType.GEMINI_NANO) }
    }

    @Test
    fun `selecting a remote provider without a stored key opens the editor`() {
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.CLAUDE)
        val actual = viewModel.uiState.value
        assertTrue(actual.isApiKeyRequired)
        assertTrue(actual.isEditingApiKey)
        coVerify { mockSettingsRepository.saveSelectedProvider(LlmProviderType.CLAUDE) }
    }

    @Test
    fun `selecting a theme is persisted immediately`() {
        val viewModel = createViewModel()
        viewModel.selectTheme(AppTheme.LIGHT)
        assertEquals(AppTheme.LIGHT, viewModel.uiState.value.theme)
        coVerify { mockSettingsRepository.saveTheme(AppTheme.LIGHT) }
    }

    @Test
    fun `selecting a question count is persisted immediately`() {
        val viewModel = createViewModel()
        viewModel.selectQuestionCount(15)
        assertEquals(15, viewModel.uiState.value.questionCount)
        coVerify { mockSettingsRepository.saveQuestionCount(15) }
    }

    @Test
    fun `changing the api key clears the draft and opens the editor`() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_STORED_KEY
        val viewModel = createViewModel()
        viewModel.onChangeApiKeyClick()
        val actual = viewModel.uiState.value
        assertTrue(actual.isEditingApiKey)
        assertEquals("", actual.draftApiKey)
    }

    @Test
    fun `saving a new api key stores it and shows the masked value`() {
        val viewModel = createViewModel()
        viewModel.updateDraftApiKey(INPUT_NEW_KEY)
        viewModel.saveApiKey()
        val actual = viewModel.uiState.value
        verify { mockApiKeyRepository.saveApiKey(LlmProviderType.GEMINI_API, INPUT_NEW_KEY) }
        assertEquals(maskApiKey(INPUT_NEW_KEY), actual.apiKeyMasked)
        assertFalse(actual.isEditingApiKey)
        assertFalse(actual.isSaving)
    }

    @Test
    fun `saving does nothing while the draft key is blank`() {
        val viewModel = createViewModel()
        viewModel.saveApiKey()
        verify(exactly = 0) { mockApiKeyRepository.saveApiKey(any(), any()) }
    }

    @Test
    fun `a later settings emission does not wipe an in-progress key edit`() {
        val viewModel = createViewModel()
        viewModel.updateDraftApiKey(INPUT_NEW_KEY)
        settingsFlow.value = settingsFlow.value.copy(theme = AppTheme.LIGHT)
        val actual = viewModel.uiState.value
        assertEquals(INPUT_NEW_KEY, actual.draftApiKey)
        assertTrue(actual.isEditingApiKey)
        assertEquals(AppTheme.LIGHT, actual.theme)
    }

    @Test
    fun `delete is ignored when no key is stored`() {
        val viewModel = createViewModel()
        viewModel.onDeleteApiKeyClick()
        assertFalse(viewModel.uiState.value.isDeleteKeyDialogVisible)
        verify(exactly = 0) { mockApiKeyRepository.clearApiKey(any()) }
    }

    @Test
    fun `delete asks for confirmation when a key is stored`() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_STORED_KEY
        val viewModel = createViewModel()
        viewModel.onDeleteApiKeyClick()
        assertTrue(viewModel.uiState.value.isDeleteKeyDialogVisible)
        verify(exactly = 0) { mockApiKeyRepository.clearApiKey(any()) }
    }

    @Test
    fun `dismissing the delete dialog keeps the stored key`() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_STORED_KEY
        val viewModel = createViewModel()
        viewModel.onDeleteApiKeyClick()
        viewModel.dismissDeleteApiKeyDialog()
        val actual = viewModel.uiState.value
        assertFalse(actual.isDeleteKeyDialogVisible)
        assertEquals(maskApiKey(INPUT_STORED_KEY), actual.apiKeyMasked)
        verify(exactly = 0) { mockApiKeyRepository.clearApiKey(any()) }
    }

    @Test
    fun `confirming delete clears the key and opens the editor`() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_STORED_KEY
        val viewModel = createViewModel()
        viewModel.onDeleteApiKeyClick()
        viewModel.confirmDeleteApiKey()
        val actual = viewModel.uiState.value
        verify { mockApiKeyRepository.clearApiKey(LlmProviderType.GEMINI_API) }
        assertEquals("", actual.apiKeyMasked)
        assertTrue(actual.isEditingApiKey)
        assertFalse(actual.isDeleteKeyDialogVisible)
    }

    @Test
    fun `back emits the navigation effect`() = runTest {
        val viewModel = createViewModel()
        viewModel.onBackClick()
        val actual = viewModel.sideEffect.first()
        assertEquals(SettingsSideEffect.NavigateBack, actual)
    }

    private fun createViewModel() = SettingsViewModel(
        settingsRepository = mockSettingsRepository,
        apiKeyRepository = mockApiKeyRepository,
        onDeviceModelRepository = mockOnDeviceModelRepository,
    )

    private companion object {
        const val INPUT_STORED_KEY = "sk-abcdefghijklmnop1234"
        const val INPUT_NEW_KEY = "sk-new-secret-key-5678"
    }
}

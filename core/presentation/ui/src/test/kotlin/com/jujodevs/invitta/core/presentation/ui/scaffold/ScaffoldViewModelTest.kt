package com.jujodevs.invitta.core.presentation.ui.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import com.jujodevs.invitta.core.testing.coVerifyOnce
import com.jujodevs.invitta.core.testing.verifyNever
import com.jujodevs.invitta.core.testing.verifyOnce
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScaffoldViewModelTest {
    private val snackbarHostState = mockk<SnackbarHostState>()
    private lateinit var viewModel: ScaffoldViewModel

    @BeforeEach
    fun setup() {
        viewModel = ScaffoldViewModel(snackbarHostState)
    }

    @Test
    fun `GIVEN new scaffold state WHEN updateScaffoldState THEN state is updated`() {
        val newState =
            ScaffoldState(
                topBar = { Text("Top Bar") },
                floatingActionButton = { Text("Floating Action Button") },
            )

        viewModel.onEvent(ScaffoldEvent.UpdateScaffoldState(newState))

        viewModel.state shouldBe newState
    }

    @Test
    fun `GIVEN message and action WHEN showSnackbar THEN callback is called`() =
        runTest {
            val message = "Test Message"
            val actionLabel = "Action"
            val snackbarResult = SnackbarResult.ActionPerformed
            val onAction: () -> Unit = mockk()
            every { onAction() } just Runs
            coEvery { snackbarHostState.showSnackbar(any(), any()) } returns snackbarResult

            viewModel.onEvent(ScaffoldEvent.ShowSnackbar(message, actionLabel, onAction))

            coVerifyOnce { snackbarHostState.showSnackbar(message, actionLabel) }
            verifyOnce { onAction() }
        }

    @Test
    fun `GIVEN message without action WHEN showSnackbar THEN no callback is called`() =
        runTest {
            val message = "Test Message"
            val snackbarResult = SnackbarResult.Dismissed
            val onAction: () -> Unit = mockk()
            coEvery { snackbarHostState.showSnackbar(any(), any(), any(), any()) } returns snackbarResult

            viewModel.onEvent(ScaffoldEvent.ShowSnackbar(message, null, onAction))

            coVerifyOnce { snackbarHostState.showSnackbar(message, null) }
            verifyNever { onAction() }
        }
}

package com.jujodevs.invitta.core.presentation.ui.scaffold

import app.cash.turbine.test
import com.jujodevs.invitta.core.presentation.ui.UiText
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class ScaffoldViewModelTest {
    private lateinit var viewModel: ScaffoldViewModel

    @Test
    fun `GIVEN a new ScaffoldState WHEN UpdateScaffoldState event is received THEN state is updated`() =
        runTest {
            viewModel = ScaffoldViewModel()
            val newState = ScaffoldState()

            viewModel.onEvent(ScaffoldEvent.UpdateScaffoldState(newState))

            viewModel.state shouldBeEqualTo newState
        }

    @Test
    fun `GIVEN a ShowSnackbar event WHEN it is received THEN the correct effect is sent`() =
        runTest {
            viewModel = ScaffoldViewModel()
            val message = UiText.DynamicString("Test Message")
            val actionLabel = UiText.DynamicString("Retry")
            val onAction: () -> Unit = mockk()
            every { onAction() } just Runs

            viewModel.effect.test {
                viewModel.onEvent(
                    ScaffoldEvent.ShowSnackbar(
                        message = message,
                        actionLabel = actionLabel,
                        onAction = onAction,
                    ),
                )

                awaitItem() shouldBeEqualTo ScaffoldEffect.ShowSnackbar(message, actionLabel, onAction)
                cancelAndIgnoreRemainingEvents()
            }
        }
}

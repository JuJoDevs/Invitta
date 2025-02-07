package com.jujodevs.invitta.feature.home.impl.presentation

import app.cash.turbine.test
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.extension.CoroutineTestExtension
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.feature.home.impl.common.defaultEvent
import com.jujodevs.invitta.feature.home.impl.domain.GetEventsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class HomeViewModelTest {
    @RegisterExtension
    val coroutineTestExtension = CoroutineTestExtension()

    private lateinit var viewModel: HomeViewModel
    private val getEventsUseCase: GetEventsUseCase = mockk()
    private val testDispatcher = coroutineTestExtension.testDispatcher

    @BeforeEach
    fun setup() {
        viewModel = HomeViewModel(getEventsUseCase, testDispatcher)
    }

    @Test
    fun `GIVEN successful event data WHEN initialize THEN updates state correctly`() =
        runTest {
            val expectedEvents = listOf(defaultEvent, defaultEvent.copy(id = "event_2"))
            every { getEventsUseCase() } returns flowOf(Result.Success(expectedEvents))

            viewModel.initialize()

            viewModel.state shouldBeEqualTo HomeState(isLoading = false, events = expectedEvents)
            verifyOnce { getEventsUseCase() }
        }

    @Test
    fun `GIVEN error WHEN initialize THEN sets error effect`() =
        runTest {
            val error = DataError.RemoteDatabase.NO_INTERNET
            every { getEventsUseCase() } returns flowOf(Result.Error(error))

            viewModel.effect.test {
                viewModel.initialize()

                viewModel.state shouldBeEqualTo HomeState(isLoading = false)
                awaitItem() shouldBeEqualTo HomeEffect.ShowError(error)
                cancelAndIgnoreRemainingEvents()
            }

            verifyOnce { getEventsUseCase() }
        }

    @Test
    fun `WHEN initialize THEN state shows loading`() =
        runTest {
            every { getEventsUseCase() } returns emptyFlow()

            viewModel.initialize()

            viewModel.state.isLoading shouldBeEqualTo true
            verifyOnce { getEventsUseCase() }
        }
}

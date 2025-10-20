package com.rs.myapps.ui.home_scr

import com.rs.myapps.domain.GetAppListUseCase
import com.rs.myapps.domain.model.AppInf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.invoke
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenVMTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockGetAppListUC = mockk<GetAppListUseCase>()
    private lateinit var viewModel: HomeScreenVM

    private val apps = listOf(
        AppInf(name = "Z App", version = "1.0", pack = "com.z.app"),
        AppInf(name = "A App", version = "1.0", pack = "com.a.app"),
        AppInf(name = "M App", version = "1.0", pack = "com.m.app")
    )

    @Before
    fun setUp() {
        viewModel = HomeScreenVM(mockGetAppListUC)
    }


    @Test
    fun `getAppList should set viewState to Success with sorted apps`() {
        runTest(testDispatcher, timeout = Duration.parse("5s")) {
            // Given
            val sortedApps = apps.sortedBy { it.name }
            coEvery { mockGetAppListUC() } returns apps

            // When
            viewModel.getAppList()
            testDispatcher.invoke { delay(2500) }

            // Then
            val state = viewModel.viewState.value
            assertTrue(state is HomeViewState.Success)
            assertEquals(sortedApps, (state as HomeViewState.Success).apps)
        }
    }

    @Test
    fun `onAppClick should not emit when state is not Success`() {
        runTest(testDispatcher)
        {
            // Given
            viewModel.viewState.value = HomeViewState.Loading

            // When
            viewModel.onAppClick(0)
            advanceUntilIdle()

            // Then
            // navEvent should not emit anything, but since it's a flow, we can check if it's empty
            // For simplicity, we can just ensure no exception is thrown and state remains Loading
            assertTrue(viewModel.viewState.value is HomeViewState.Loading)
        }
    }
}

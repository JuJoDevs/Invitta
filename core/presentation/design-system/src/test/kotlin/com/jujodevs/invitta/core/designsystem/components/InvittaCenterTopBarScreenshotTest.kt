package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

data class InvittaCenterTopBarTestCase(
    val title: String,
    val hasNavigationIcon: Boolean,
    val hasActions: Boolean,
)

@RunWith(Parameterized::class)
class InvittaCenterTopBarScreenshotTest(
    private val description: String,
    private val testCase: InvittaCenterTopBarTestCase,
) {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_3,
            theme = "Theme.App",
            renderingMode = SessionParams.RenderingMode.SHRINK,
            showSystemUi = false,
        )

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(
                    "TopBar with navigation icon and actions",
                    InvittaCenterTopBarTestCase(
                        title = "Details",
                        hasNavigationIcon = true,
                        hasActions = true,
                    ),
                ),
                arrayOf(
                    "TopBar with actions only",
                    InvittaCenterTopBarTestCase(
                        title = "Settings",
                        hasNavigationIcon = false,
                        hasActions = true,
                    ),
                ),
                arrayOf(
                    "TopBar with navigation icon only",
                    InvittaCenterTopBarTestCase(
                        title = "Profile",
                        hasNavigationIcon = true,
                        hasActions = false,
                    ),
                ),
                arrayOf(
                    "TopBar with no icons or actions",
                    InvittaCenterTopBarTestCase(
                        title = "Empty",
                        hasNavigationIcon = false,
                        hasActions = false,
                    ),
                ),
            )
        }
    }

    @Test
    fun captureScreenshot() {
        paparazzi.snapshot {
            InvittaTheme {
                InvittaCenterTopBar(
                    title = testCase.title,
                    navigationIcon =
                        if (testCase.hasNavigationIcon) {
                            { NavigationIcon() }
                        } else {
                            {}
                        },
                    actions =
                        if (testCase.hasActions) {
                            {
                                Actions()
                            }
                        } else {
                            {}
                        },
                )
            }
        }
    }

    @Composable
    private fun NavigationIcon() {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
    }

    @Composable fun Actions() {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

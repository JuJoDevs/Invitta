package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.designsystem.theme.dimens
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

data class InvittaTopBarTestCase(
    val title: String,
    val hasNavigationIcon: Boolean,
    val hasActions: Boolean,
)

@RunWith(Parameterized::class)
class InvitttaTopBarScreenshotTest(
    private val description: String,
    private val testCase: InvittaTopBarTestCase,
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
                    InvittaTopBarTestCase(
                        title = "Home",
                        hasNavigationIcon = true,
                        hasActions = true,
                    ),
                ),
                arrayOf(
                    "TopBar with actions only",
                    InvittaTopBarTestCase(
                        title = "Settings",
                        hasNavigationIcon = false,
                        hasActions = true,
                    ),
                ),
                arrayOf(
                    "TopBar with navigation icon only",
                    InvittaTopBarTestCase(
                        title = "Profile",
                        hasNavigationIcon = true,
                        hasActions = false,
                    ),
                ),
                arrayOf(
                    "TopBar with no icons or actions",
                    InvittaTopBarTestCase(
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
                InvittaTopBar(
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }

    @Composable fun Actions() {
        IconButton(onClick = {}) {
            InvittaButton(label = "Edit", onClick = {})
            InvittaButton(
                label = "Delete",
                onClick = {},
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium),
            )
        }
    }
}

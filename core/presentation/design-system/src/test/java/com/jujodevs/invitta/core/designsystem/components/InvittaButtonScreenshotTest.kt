package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class InvittaButtonScreenshotTest(
    private val description: String,
    private val testCase: InvittaButtonTestCase,
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
                    "Button with icon",
                    InvittaButtonTestCase(
                        label = "Accept",
                        hasIcon = true,
                    ),
                ),
                arrayOf(
                    "Button with no icon",
                    InvittaButtonTestCase(
                        label = "Decline",
                        hasIcon = false,
                    ),
                ),
            )
        }
    }

    @Test
    fun captureScreenshot() {
        paparazzi.snapshot {
            InvittaTheme {
                InvittaButton(
                    label = testCase.label,
                    onClick = {},
                    icon =
                        if (testCase.hasIcon) {
                            { Icon(Icons.Default.Add, contentDescription = "Add") }
                        } else {
                            {}
                        },
                )
            }
        }
    }
}

data class InvittaButtonTestCase(
    val label: String,
    val hasIcon: Boolean,
)

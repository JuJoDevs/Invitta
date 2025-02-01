package com.jujodevs.invitta.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = PurplePrimaryDark,
        onPrimary = PurpleOnPrimaryDark,
        primaryContainer = PurpleContainerDark,
        onPrimaryContainer = PurpleOnContainerDark,
        secondary = YellowSecondaryDark,
        onSecondary = YellowOnSecondaryDark,
        secondaryContainer = YellowContainerDark,
        onSecondaryContainer = YellowOnContainerDark,
        tertiary = TealTertiaryDark,
        onTertiary = TealOnTertiaryDark,
        tertiaryContainer = TealContainerDark,
        onTertiaryContainer = TealOnContainerDark,
        background = BlackBackgroundDark,
        onBackground = GrayOnBackgroundDark,
        surface = BlackSurfaceDark,
        onSurface = GrayOnSurfaceDark,
        surfaceVariant = GrayVariantSurfaceDark,
        onSurfaceVariant = GrayOnVariantSurfaceDark,
        outline = GrayOutlineDark,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = PurplePrimaryLight,
        onPrimary = PurpleOnPrimaryLight,
        primaryContainer = PurpleContainerLight,
        onPrimaryContainer = PurpleOnContainerLight,
        secondary = YellowSecondaryLight,
        onSecondary = YellowOnSecondaryLight,
        secondaryContainer = YellowContainerLight,
        onSecondaryContainer = YellowOnContainerLight,
        tertiary = TealTertiaryLight,
        onTertiary = TealOnTertiaryLight,
        tertiaryContainer = TealContainerLight,
        onTertiaryContainer = TealOnContainerLight,
        background = WhiteBackgroundLight,
        onBackground = GrayOnBackgroundLight,
        surface = WhiteSurfaceLight,
        onSurface = GrayOnSurfaceLight,
        surfaceVariant = GrayVariantSurfaceLight,
        onSurfaceVariant = GrayOnVariantSurfaceLight,
        outline = GrayOutlineLight,
    )

@Composable
fun InvittaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    CompositionLocalProvider(
        LocalDimens provides Dimens,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

val MaterialTheme.dimens: Dimens
    @Composable @ReadOnlyComposable
    get() = LocalDimens.current

internal val LocalDimens = staticCompositionLocalOf { Dimens }

package com.laioffer.netflix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NetflixDarkColorScheme = darkColorScheme(
    primary = NetflixRed,
    tertiary = NetflixRed,
    background = NetflixBlack,
    surface = NetflixDarkGray,
    onPrimary = NetflixWhite,
    onSecondary = NetflixLightGray,
    onTertiary = NetflixWhite,
    onBackground = NetflixWhite,
    onSurface = NetflixWhite,
    onSurfaceVariant = NetflixLightGray
)

@Composable
fun NetflixTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NetflixDarkColorScheme,
        typography = Typography,
        content = content
    )
}

package com.vitorfg8.quizia.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

private const val GOOGLE_FONTS_AUTHORITY = "com.google.android.gms.fonts"
private const val GOOGLE_FONTS_PACKAGE = "com.google.android.gms"
private const val INTER_FONT_NAME = "Inter"

private val InterFontProvider = GoogleFont.Provider(
    providerAuthority = GOOGLE_FONTS_AUTHORITY,
    providerPackage = GOOGLE_FONTS_PACKAGE,
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val InterFont = GoogleFont(name = INTER_FONT_NAME)

private val InterFontFamily = FontFamily(
    Font(googleFont = InterFont, fontProvider = InterFontProvider, weight = FontWeight.Normal),
    Font(googleFont = InterFont, fontProvider = InterFontProvider, weight = FontWeight.Medium),
    Font(googleFont = InterFont, fontProvider = InterFontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = InterFont, fontProvider = InterFontProvider, weight = FontWeight.Bold),
)

private val BaseTypography = Typography()

internal val QuiziaTypography = Typography(
    displayLarge = BaseTypography.displayLarge.withInter(),
    displayMedium = BaseTypography.displayMedium.withInter(),
    displaySmall = BaseTypography.displaySmall.withInter(),
    headlineLarge = BaseTypography.headlineLarge.withInter(FontWeight.Bold),
    headlineMedium = BaseTypography.headlineMedium.withInter(FontWeight.SemiBold),
    headlineSmall = BaseTypography.headlineSmall.withInter(FontWeight.SemiBold),
    titleLarge = BaseTypography.titleLarge.withInter(FontWeight.SemiBold),
    titleMedium = BaseTypography.titleMedium.withInter(),
    titleSmall = BaseTypography.titleSmall.withInter(),
    bodyLarge = BaseTypography.bodyLarge.withInter(),
    bodyMedium = BaseTypography.bodyMedium.withInter(),
    bodySmall = BaseTypography.bodySmall.withInter(),
    labelLarge = BaseTypography.labelLarge.withInter(FontWeight.SemiBold),
    labelMedium = BaseTypography.labelMedium.withInter(),
    labelSmall = BaseTypography.labelSmall.withInter(),
)

private fun TextStyle.withInter(weight: FontWeight? = null): TextStyle = copy(
    fontFamily = InterFontFamily,
    fontWeight = weight ?: fontWeight,
)

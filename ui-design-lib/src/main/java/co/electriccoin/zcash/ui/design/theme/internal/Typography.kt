package co.electriccoin.zcash.ui.design.theme.internal

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import co.electriccoin.zcash.ui.design.R

private val provider =
    GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

// We use bestEffort here to be able to get the closest font weight, if accidentally use
// an unspecified font weight and not the default one.
private val InterFont = GoogleFont(name = "Inter", bestEffort = true)
private val ArchivoFont = GoogleFont(name = "Archivo", bestEffort = true)

private val InterFontFamily =
    FontFamily(
        // W400
        Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Normal),
        // W500
        Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Medium),
        // W600
        Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.SemiBold),
        // W700
        Font(googleFont = InterFont, fontProvider = provider, weight = FontWeight.Bold)
    )
private val ArchivoFontFamily =
    FontFamily(
        // W400
        Font(googleFont = ArchivoFont, fontProvider = provider, weight = FontWeight.Normal),
        // W500
        Font(googleFont = ArchivoFont, fontProvider = provider, weight = FontWeight.Medium),
        // W600
        Font(googleFont = ArchivoFont, fontProvider = provider, weight = FontWeight.SemiBold),
        // W700
        Font(googleFont = ArchivoFont, fontProvider = provider, weight = FontWeight.Bold)
    )

private val Zboto =
    FontFamily(
        Font(R.font.zboto, FontWeight.Normal)
    )

// If you change this definition of our Typography, don't forget to check if you use only
// the defined font weights above, otherwise the closest one will be used.
internal val PrimaryTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            ),
        titleSmall =
            TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
        bodyLarge =
            TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        bodySmall =
            TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            ),
        labelLarge =
            TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
    )

internal val SecondaryTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            ),
        headlineMedium =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            ),
        headlineSmall =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
        bodyLarge =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        bodySmall =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            ),
        labelLarge =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
        labelMedium =
            TextStyle(
                fontFamily = ArchivoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
    )

@Immutable
data class Typography(
    val primary: Typography,
    val secondary: Typography
)

@Immutable
data class ExtendedTypography(
    val listItem: TextStyle,
    val zecBalance: TextStyle,
    val aboutText: TextStyle,
    val buttonText: TextStyle,
    val checkboxText: TextStyle,
    val securityWarningText: TextStyle,
    val textFieldHint: TextStyle,
    val textFieldValue: TextStyle,
    val textFieldBirthday: TextStyle,
    val textNavTab: TextStyle,
)

@Suppress("CompositionLocalAllowlist")
val LocalTypographies =
    staticCompositionLocalOf {
        Typography(
            primary = PrimaryTypography,
            secondary = SecondaryTypography
        )
    }

@Suppress("CompositionLocalAllowlist")
val LocalExtendedTypography =
    staticCompositionLocalOf {
        ExtendedTypography(
            listItem =
                PrimaryTypography.bodyLarge.copy(
                    fontSize = 24.sp
                ),
            zecBalance =
                TextStyle(
                    fontFamily = Zboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp
                ),
            aboutText =
                PrimaryTypography.bodyLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
            buttonText =
                PrimaryTypography.bodySmall.copy(
                    fontSize = 14.sp
                ),
            checkboxText =
                PrimaryTypography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
            securityWarningText =
                PrimaryTypography.bodySmall.copy(
                    lineHeight = 22.32.sp
                ),
            textFieldHint =
                PrimaryTypography.bodySmall.copy(
                    fontSize = 13.sp,
                    lineHeight = 15.73.sp,
                    fontWeight = FontWeight.Normal
                ),
            textFieldValue =
                PrimaryTypography.bodyLarge.copy(
                    fontSize = 17.sp,
                ),
            textFieldBirthday = SecondaryTypography.headlineMedium.copy(),
            textNavTab =
                SecondaryTypography.labelSmall.copy(
                    fontSize = 13.sp
                ),
        )
    }

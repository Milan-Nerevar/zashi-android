package co.electriccoin.zcash.ui.screen.receive.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cash.z.ecc.android.sdk.fixture.WalletAddressesFixture
import cash.z.ecc.android.sdk.model.WalletAddress
import cash.z.ecc.android.sdk.model.WalletAddresses
import co.electriccoin.zcash.ui.R
import co.electriccoin.zcash.ui.common.BrightenScreen
import co.electriccoin.zcash.ui.common.DisableScreenTimeout
import co.electriccoin.zcash.ui.common.model.VersionInfo
import co.electriccoin.zcash.ui.common.test.CommonTag
import co.electriccoin.zcash.ui.design.component.CircularScreenProgressIndicator
import co.electriccoin.zcash.ui.design.component.GradientSurface
import co.electriccoin.zcash.ui.design.component.Reference
import co.electriccoin.zcash.ui.design.component.SmallTopAppBar
import co.electriccoin.zcash.ui.design.component.SubHeader
import co.electriccoin.zcash.ui.design.theme.ZcashTheme
import co.electriccoin.zcash.ui.fixture.VersionInfoFixture
import co.electriccoin.zcash.ui.screen.receive.util.AndroidQrCodeImageGenerator
import co.electriccoin.zcash.ui.screen.receive.util.JvmQrCodeGenerator
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@Preview("Receive")
@Composable
private fun ComposablePreview() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            Receive(
                walletAddress = runBlocking { WalletAddressesFixture.new() },
                snackbarHostState = SnackbarHostState(),
                onSettings = {},
                onAdjustBrightness = {},
                onAddrCopyToClipboard = {},
                onQrImageShare = {},
                versionInfo = VersionInfoFixture.new()
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
fun Receive(
    walletAddress: WalletAddresses?,
    snackbarHostState: SnackbarHostState,
    onSettings: () -> Unit,
    onAdjustBrightness: (Boolean) -> Unit,
    onAddrCopyToClipboard: (String) -> Unit,
    onQrImageShare: (ImageBitmap) -> Unit,
    versionInfo: VersionInfo,
) {
    val (brightness, setBrightness) = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ReceiveTopAppBar(
                adjustBrightness = brightness,
                onSettings = onSettings,
                onBrightness = {
                    onAdjustBrightness(!brightness)
                    setBrightness(!brightness)
                },
                versionInfo = versionInfo,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        if (null == walletAddress) {
            CircularScreenProgressIndicator()
        } else {
            ReceiveContents(
                walletAddress = walletAddress,
                onAddressCopyToClipboard = onAddrCopyToClipboard,
                onQrImageShare = onQrImageShare,
                adjustBrightness = brightness,
                versionInfo = versionInfo,
                modifier =
                    Modifier.padding(
                        top = paddingValues.calculateTopPadding() + ZcashTheme.dimens.spacingDefault,
                        bottom = paddingValues.calculateBottomPadding() + ZcashTheme.dimens.spacingDefault,
                        start = ZcashTheme.dimens.screenHorizontalSpacingRegular,
                        end = ZcashTheme.dimens.screenHorizontalSpacingRegular
                    )
            )
        }
    }
}

@Composable
private fun ReceiveTopAppBar(
    adjustBrightness: Boolean,
    onSettings: () -> Unit,
    onBrightness: () -> Unit,
    versionInfo: VersionInfo
) {
    SmallTopAppBar(
        titleText = stringResource(id = R.string.receive_title),
        regularActions = {
            if (versionInfo.isDebuggable) {
                IconButton(
                    onClick = onBrightness
                ) {
                    Icon(
                        imageVector =
                            if (adjustBrightness) {
                                Icons.Default.BrightnessLow
                            } else {
                                Icons.Default.BrightnessHigh
                            },
                        contentDescription = stringResource(R.string.receive_brightness_content_description)
                    )
                }
            }
        },
        hamburgerMenuActions = {
            IconButton(
                onClick = onSettings,
                modifier = Modifier.testTag(CommonTag.SETTINGS_TOP_BAR_BUTTON)
            ) {
                Icon(
                    painter = painterResource(id = co.electriccoin.zcash.ui.design.R.drawable.hamburger_menu_icon),
                    contentDescription = stringResource(id = R.string.settings_menu_content_description)
                )
            }
        }
    )
}

@Suppress("LongParameterList")
@Composable
private fun ReceiveContents(
    walletAddress: WalletAddresses,
    onAddressCopyToClipboard: (String) -> Unit,
    onQrImageShare: (ImageBitmap) -> Unit,
    adjustBrightness: Boolean,
    versionInfo: VersionInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (adjustBrightness) {
            BrightenScreen()
            DisableScreenTimeout()
        }

        Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingSmall))

        Address(
            walletAddress = walletAddress.unified,
            onAddressCopyToClipboard = onAddressCopyToClipboard,
            onQrImageShare = onQrImageShare,
        )

        if (versionInfo.isTestnet) {
            Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingHuge))

            Address(
                walletAddress = walletAddress.sapling,
                onAddressCopyToClipboard = onAddressCopyToClipboard,
                onQrImageShare = onQrImageShare,
            )
        }

        Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingHuge))

        Address(
            walletAddress = walletAddress.transparent,
            onAddressCopyToClipboard = onAddressCopyToClipboard,
            onQrImageShare = onQrImageShare,
        )
    }
}

private val DEFAULT_QR_CODE_SIZE = 320.dp

@Suppress("LongMethod")
@Composable
private fun Address(
    walletAddress: WalletAddress,
    onAddressCopyToClipboard: (String) -> Unit,
    onQrImageShare: (ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SubHeader(
            text =
                stringResource(
                    id =
                        when (walletAddress) {
                            is WalletAddress.Unified -> R.string.receive_wallet_address_unified
                            is WalletAddress.Sapling -> R.string.receive_wallet_address_sapling
                            is WalletAddress.Transparent -> R.string.receive_wallet_address_transparent
                        }
                ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingTiny))

        val sizePixels = with(LocalDensity.current) { DEFAULT_QR_CODE_SIZE.toPx() }.roundToInt()
        val qrCodeImage =
            remember {
                qrCodeForAddress(
                    address = walletAddress.address,
                    size = sizePixels
                )
            }

        QrCode(
            qrCodeImage = qrCodeImage,
            onQrImageBitmapShare = onQrImageShare,
            contentDescription =
                stringResource(
                    id =
                        when (walletAddress) {
                            is WalletAddress.Unified -> R.string.receive_unified_content_description
                            is WalletAddress.Sapling -> R.string.receive_sapling_content_description
                            is WalletAddress.Transparent -> R.string.receive_transparent_content_description
                        }
                ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingTiny))

        // TODO [#163]: Ellipsize center of the string
        // TODO [#163]: https://github.com/Electric-Coin-Company/zashi-android/issues/163
        Text(
            text = walletAddress.address,
            style = ZcashTheme.typography.primary.bodyLarge,
            color = ZcashTheme.colors.textDescription,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onAddressCopyToClipboard(walletAddress.address) }
                    .padding(horizontal = ZcashTheme.dimens.spacingLarge)
                    .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(ZcashTheme.dimens.spacingSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Reference(
                text = stringResource(id = R.string.receive_copy),
                onClick = { onAddressCopyToClipboard(walletAddress.address) },
                textAlign = TextAlign.Center,
                imageVector = ImageVector.vectorResource(R.drawable.copy),
                imageContentDescription = null,
                modifier = Modifier.wrapContentSize(),
            )
            Reference(
                text = stringResource(id = R.string.receive_share),
                onClick = { onQrImageShare(qrCodeImage) },
                textAlign = TextAlign.Center,
                imageVector = ImageVector.vectorResource(R.drawable.share),
                imageContentDescription = null,
                modifier = Modifier.wrapContentSize(),
            )
        }
    }
}

private fun qrCodeForAddress(
    address: String,
    size: Int,
): ImageBitmap {
    // In the future, use actual/expect to switch QR code generator implementations for multiplatform

    // Note that our implementation has an extra array copy to BooleanArray, which is a cross-platform
    // representation.  This should have minimal performance impact since the QR code is relatively
    // small and we only generate QR codes infrequently.

    val qrCodePixelArray = JvmQrCodeGenerator.generate(address, size)

    return AndroidQrCodeImageGenerator.generate(qrCodePixelArray, size)
}

@Composable
private fun QrCode(
    contentDescription: String,
    qrCodeImage: ImageBitmap,
    onQrImageBitmapShare: (ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        bitmap = qrCodeImage,
        contentDescription = contentDescription,
        modifier =
            Modifier
                .clickable { onQrImageBitmapShare(qrCodeImage) }
                .then(modifier)
    )
}

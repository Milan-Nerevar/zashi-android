package co.electriccoin.zcash.ui.screen.advancedsettings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import co.electriccoin.zcash.ui.R
import co.electriccoin.zcash.ui.design.component.GradientSurface
import co.electriccoin.zcash.ui.design.component.PrimaryButton
import co.electriccoin.zcash.ui.design.component.SmallTopAppBar
import co.electriccoin.zcash.ui.design.theme.ZcashTheme
import co.electriccoin.zcash.ui.design.theme.ZcashTheme.dimens
import co.electriccoin.zcash.ui.screen.advancedsettings.AdvancedSettingsTag

// TODO [#1271]: Add AdvancedSettingsView Tests
// TODO [#1271]: https://github.com/Electric-Coin-Company/zashi-android/issues/1271

@Preview("Advanced Settings")
@Composable
private fun PreviewAdvancedSettings() {
    ZcashTheme(forceDarkMode = false) {
        GradientSurface {
            AdvancedSettings(
                onBack = {},
                onExportPrivateData = {},
                onSeedRecovery = {},
                onChooseServer = {},
            )
        }
    }
}

@Composable
fun AdvancedSettings(
    onBack: () -> Unit,
    onExportPrivateData: () -> Unit,
    onChooseServer: () -> Unit,
    onSeedRecovery: () -> Unit,
) {
    Scaffold(topBar = {
        AdvancedSettingsTopAppBar(
            onBack = onBack,
        )
    }) { paddingValues ->
        AdvancedSettingsMainContent(
            modifier =
                Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        top = paddingValues.calculateTopPadding() + dimens.spacingHuge,
                        bottom = paddingValues.calculateBottomPadding(),
                        start = dimens.screenHorizontalSpacingBig,
                        end = dimens.screenHorizontalSpacingBig
                    ),
            onExportPrivateData = onExportPrivateData,
            onSeedRecovery = onSeedRecovery,
            onChooseServer = onChooseServer,
        )
    }
}

@Composable
private fun AdvancedSettingsTopAppBar(onBack: () -> Unit) {
    SmallTopAppBar(
        backText = stringResource(id = R.string.advanced_settings_back).uppercase(),
        backContentDescriptionText = stringResource(R.string.advanced_settings_back_content_description),
        onBack = onBack,
        showTitleLogo = true,
        modifier = Modifier.testTag(AdvancedSettingsTag.ADVANCED_SETTINGS_TOP_APP_BAR)
    )
}

@Composable
private fun AdvancedSettingsMainContent(
    onSeedRecovery: () -> Unit,
    onExportPrivateData: () -> Unit,
    onChooseServer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            onClick = onSeedRecovery,
            text = stringResource(R.string.advanced_settings_backup_wallet)
        )

        Spacer(modifier = Modifier.height(dimens.spacingDefault))

        PrimaryButton(
            onClick = onExportPrivateData,
            text = stringResource(R.string.advanced_settings_export_private_data)
        )

        Spacer(modifier = Modifier.height(dimens.spacingDefault))

        PrimaryButton(
            onClick = onChooseServer,
            text = stringResource(R.string.advanced_settings_choose_server)
        )

        Spacer(modifier = Modifier.height(dimens.spacingHuge))
    }
}

package com.feverlog.app

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Proves that JVM Compose UI tests and screenshots work without an emulator. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = RobolectricDeviceQualifiers.Pixel7)
class ToolchainSmokeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersAndCapturesScreenshot() {
        composeRule.setContent { Text("toolchain smoke test") }
        composeRule.onRoot().captureRoboImage("build/outputs/roborazzi/smoke.png")
    }
}

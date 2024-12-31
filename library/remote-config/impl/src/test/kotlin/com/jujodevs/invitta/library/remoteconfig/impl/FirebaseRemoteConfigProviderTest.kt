package com.jujodevs.invitta.library.remoteconfig.impl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigName
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigType
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirebaseRemoteConfigProviderTest {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var remoteConfigProvider: FirebaseRemoteConfigProvider

    @BeforeEach
    fun setUp() {
        firebaseRemoteConfig = mockk()
        every { firebaseRemoteConfig.setConfigSettingsAsync(any<FirebaseRemoteConfigSettings>()) } returns mockk()
        every { firebaseRemoteConfig.fetchAndActivate() } returns mockk()
        remoteConfigProvider = FirebaseRemoteConfigProvider(firebaseRemoteConfig)
    }

    @Test
    fun `GIVEN boolean parameter WHEN getBooleanParameter THEN return expected boolean value`() {
        val expectedValue = true
        every { firebaseRemoteConfig.getBoolean(RemoteConfigName.SHOW_SHARE_APP.name) } returns expectedValue

        val result: Boolean = remoteConfigProvider.getBooleanParameter(RemoteConfigName.SHOW_SHARE_APP)

        result shouldBeEqualTo expectedValue
        verifyOnce { firebaseRemoteConfig.getBoolean(RemoteConfigName.SHOW_SHARE_APP.name) }
    }

    @Test
    fun `GIVEN string parameter WHEN getStringParameter THEN return expected string value`() {
        val testName = "TEST_STRING"
        val expectedValue = "test_string"
        val configName: RemoteConfigName =
            mockk {
                every { name } returns testName
                every { type } returns RemoteConfigType.STRING
            }
        every { firebaseRemoteConfig.getString(testName) } returns expectedValue

        val result: String = remoteConfigProvider.getStringParameter(configName)

        result shouldBeEqualTo expectedValue
        verifyOnce { firebaseRemoteConfig.getString(testName) }
    }
}

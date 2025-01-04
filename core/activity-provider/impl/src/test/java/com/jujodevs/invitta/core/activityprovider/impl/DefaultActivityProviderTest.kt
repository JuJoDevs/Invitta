package com.jujodevs.invitta.core.activityprovider.impl

import android.app.Activity
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultActivityProviderTest {

    private lateinit var application: Application
    private lateinit var activityProvider: DefaultActivityProvider

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()
        activityProvider = DefaultActivityProvider(application)
    }

    @Test
    fun `GIVEN an activity is started WHEN currentActivity is called THEN it should return the correct activity`() {
        val activityController = Robolectric.buildActivity(Activity::class.java).create()
        val activity = activityController.start().resume().get()

        val result = activityProvider.currentActivity()

        result shouldBeEqualTo activity
    }

    @Test
    fun `GIVEN an activity is stopped WHEN currentActivity is called THEN it should return null`() {
        val activityController = Robolectric.buildActivity(Activity::class.java).create()
        activityController.start().resume().get()
        activityController.stop()

        val result = activityProvider.currentActivity()

        result shouldBe null
    }

    @Test
    fun `GIVEN an activity is destroyed WHEN currentActivity is called THEN it should return null`() {
        val activityController = Robolectric.buildActivity(Activity::class.java).create()
        activityController.start().resume().get()
        activityController.destroy()

        val result = activityProvider.currentActivity()

        result shouldBe null
    }

    @Test
    fun `GIVEN multiple activities are started WHEN currentActivity is called THEN it should return the last started activity`() {
        val firstActivityController = Robolectric.buildActivity(Activity::class.java).create()
        firstActivityController.start().resume().get()

        val secondActivityController = Robolectric.buildActivity(Activity::class.java).create()
        val secondActivity = secondActivityController.start().resume().get()

        val result = activityProvider.currentActivity()

        result shouldBeEqualTo secondActivity
    }

    @Test
    fun `GIVEN an activity is finished WHEN another activity is resumed THEN it should return the resumed activity`() {
        val firstActivityController = Robolectric.buildActivity(Activity::class.java).create()
        firstActivityController.start().resume().get()
        firstActivityController.stop()

        val secondActivityController = Robolectric.buildActivity(Activity::class.java).create()
        val secondActivity = secondActivityController.start().resume().get()

        val result = activityProvider.currentActivity()

        result shouldBeEqualTo secondActivity
    }
}

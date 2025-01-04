package com.jujodevs.invitta.core.activityprovider.impl

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.jujodevs.invitta.core.activityprovider.api.ActivityProvider
import java.lang.ref.WeakReference

class DefaultActivityProvider(
    application: Application,
) : ActivityProvider {
    private var currentActivity: WeakReference<Activity>? = null

    init {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    bundle: Bundle?,
                ) {
                    // Do nothing
                }
                override fun onActivityStarted(activity: Activity) {
                    currentActivity = WeakReference(activity)
                }

                override fun onActivityResumed(activity: Activity) {
                    currentActivity = WeakReference(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    // Do nothing
                }

                override fun onActivityStopped(activity: Activity) {
                    if (currentActivity?.get() === activity) {
                        currentActivity = null
                    }
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    bundle: Bundle,
                ) {
                    // Do nothing
                }

                override fun onActivityDestroyed(activity: Activity) {
                    if (currentActivity?.get() === activity) {
                        currentActivity = null
                    }
                }
            },
        )
    }

    override fun currentActivity(): Activity? = currentActivity?.get()
}

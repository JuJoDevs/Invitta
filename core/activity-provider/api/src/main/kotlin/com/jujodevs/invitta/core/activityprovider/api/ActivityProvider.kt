package com.jujodevs.invitta.core.activityprovider.api

import android.app.Activity

interface ActivityProvider {
    fun currentActivity(): Activity?
}

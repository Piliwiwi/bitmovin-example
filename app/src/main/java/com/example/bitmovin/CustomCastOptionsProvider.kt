package com.example.bitmovin

import android.annotation.SuppressLint
import android.content.Context
import com.bitmovin.player.casting.BitmovinCastManager
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions

class CustomCastOptionsProvider : OptionsProvider {
    @SuppressLint("VisibleForTests")
    override fun getCastOptions(context: Context): CastOptions {
        val expandedControllerActivity: Class<*>? =
            BitmovinCastManager.getInstance().castControllerActivityClass
        val expandedControllerActivityName = expandedControllerActivity?.name

        val launchOptions = LaunchOptions.Builder().setRelaunchIfRunning(true).build()

        return CastOptions.Builder()
            //.setReceiverApplicationId(BitmovinCastManager.getInstance().applicationId)
            //.setReceiverApplicationId("FB34B13F")
            .setReceiverApplicationId("A889FC57")
            .setCastMediaOptions(CastMediaOptions.Builder().setMediaSessionEnabled(false).build())
            .setLaunchOptions(launchOptions).build()
    }

    override fun getAdditionalSessionProviders(appContext: Context): List<SessionProvider>? {
        return null
    }
}
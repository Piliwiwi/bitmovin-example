package com.example.bitmovin.navigator

import android.content.Context
import com.example.bitmovin.PlayerActivity

class FirstNavigator {
    fun goToPlayer(context: Context?) = context?.apply {
        val intent = PlayerActivity.makeIntent(this)
        startActivity(intent)
    }
}
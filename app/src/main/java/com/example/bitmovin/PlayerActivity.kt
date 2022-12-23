package com.example.bitmovin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitmovin.player.casting.BitmovinCastManager
import com.example.bitmovin.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityPlayerBinding? = null

    private var castManager: BitmovinCastManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initializeCastManager()
    }

    private fun initializeCastManager() {
        /* getInstance cast manager initialize internally,
        * don't need to use BitmovinCastManager.initialize() */
        BitmovinCastManager.initialize()
        castManager = BitmovinCastManager.getInstance()
        castManager?.updateContext(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        /* In this part we don't have access to dealloc cast manager internal instance */
        castManager?.disconnect()
        castManager = null
        binding = null
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, PlayerActivity::class.java)
    }
}
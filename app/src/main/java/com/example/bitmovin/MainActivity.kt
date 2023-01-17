package com.example.bitmovin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.bitmovin.player.casting.BitmovinCastManager
import com.example.bitmovin.databinding.ActivityMainBinding
import com.example.bitmovin.fragments.FirstFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var castManager: BitmovinCastManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) binding = ActivityMainBinding.inflate(layoutInflater)
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
        binding = null
        castManager = null
    }
}
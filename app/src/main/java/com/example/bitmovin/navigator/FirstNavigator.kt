package com.example.bitmovin.navigator

import androidx.fragment.app.Fragment
import com.example.bitmovin.fragments.PlayerFragment
import com.example.bitmovin.utils.navigateTo

class FirstNavigator(private val fragment: Fragment) {
    fun goToPlayer() = fragment.context?.apply {
        val playerFragment = PlayerFragment()
        fragment.navigateTo(playerFragment, PlayerFragment.TAG)
    }
}
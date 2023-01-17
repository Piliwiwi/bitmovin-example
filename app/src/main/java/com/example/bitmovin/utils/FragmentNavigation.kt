package com.example.bitmovin.utils

import androidx.fragment.app.Fragment
import com.example.bitmovin.R

fun Fragment.navigateTo(fragment: Fragment, tag: String) {
    parentFragmentManager.beginTransaction().also { transaction ->
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }
}
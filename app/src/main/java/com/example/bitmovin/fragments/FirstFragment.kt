package com.example.bitmovin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bitmovin.databinding.FragmentFirstBinding
import com.example.bitmovin.util.navigateTo

class FirstFragment: Fragment() {
    private var binding: FragmentFirstBinding? = null
    private var next: PlayerFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next = PlayerFragment()
        setupButton()
    }

    private fun setupButton() = binding?.apply {
        navigationBtn.setOnClickListener {
            next?.let { navigateTo(it, "VCP") }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        next = null
        binding?.navigationBtn?.setOnClickListener(null)
        binding = null
    }

    companion object {
        fun makeInstance() = FirstFragment()
    }
}
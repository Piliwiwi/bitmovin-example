package com.example.bitmovin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bitmovin.databinding.FragmentFirstBinding
import com.example.bitmovin.navigator.FirstNavigator

class FirstFragment : Fragment() {
    private var binding: FragmentFirstBinding? = null
    private var navigator: FirstNavigator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = FirstNavigator()
        setupButton()
    }

    private fun setupButton() = binding?.apply {
        navigationBtn.setOnClickListener {
            navigator?.goToPlayer(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigator = null
        binding?.navigationBtn?.setOnClickListener(null)
        binding = null
    }
}
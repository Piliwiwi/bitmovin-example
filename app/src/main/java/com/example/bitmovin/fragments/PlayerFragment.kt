package com.example.bitmovin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bitmovin.player.api.media.subtitle.SubtitleTrack
import com.example.bitmovin.databinding.FragmentPlayerBinding
import com.example.player.AttrsPlayerComponent
import com.example.player.AttrsPlayerComponentSubtitle

class PlayerFragment : Fragment() {
    var binding: FragmentPlayerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null)
            binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayer()
    }

    private fun setupPlayer() = binding?.apply {
        player.setAttributes(
            AttrsPlayerComponent(
                videoHlsUrl = "https://mdstrm.com/video/62d77b709bb4e60836d97de0.m3u8",
                thumbnailsUrl = "https://thumbs.cdn.mdstrm.com/thumbs/60affd0addf19308279e2d16/62d77b709bb4e60836d97de0/preview_62d77b709bb4e60836d97de0.vtt",
                ads = "https://mdstrm.com/ads/6266d5e7e20e33083b4e33ea/map.xml?duration=1232",
                subtitles = listOf(AttrsPlayerComponentSubtitle())
            )

        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val TAG = "VCP"

        fun makeInstance() = PlayerFragment()
    }
}
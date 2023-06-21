package com.example.bitmovin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bitmovin.databinding.FragmentPlayerBinding
import com.example.bitmovin.player.AttrsPlayerComponent
import com.example.bitmovin.player.AttrsPlayerComponentSubtitle

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
                videoHlsUrl = "https://mdstrm.com/video/63719dcceef30855695da2a3.mpd",
                thumbnailsUrl = "https://thumbs.cdn.mdstrm.com/thumbs/60affd0addf19308279e2d16/62d77b709bb4e60836d97de0/preview_62d77b709bb4e60836d97de0.vtt",
                ads = "https://pubads.g.doubleclick.net/gampad/ads?npa=0&sz=1920x1080&gdfp_req=1&output=vmap&unviewed_position_start=1&env=vp&impl=s&vad_type=linear&ad_rule=1&iu=/105773011/MELIPLAY-AR&description_url=https%3A%2F%2Fwww.mercadolibre.com.ar%2Fplay%2Fcontent%2Fdf2c8fa04c18459ea40a6ffc6a68354d&vid_d=6061&tfcd=0&ppid=dd2236c9cd2cbbd60fe29c46d0cb4896&cust_params=content_provider%3DSONY%26content_id%3Ddf2c8fa04c18459ea40a6ffc6a68354d%26rating%3D+14%26release_year%3D2013%26site_id%3DMLC%26ppid%3Ddd2236c9cd2cbbd60fe29c46d0cb4896",
                subtitles = listOf(AttrsPlayerComponentSubtitle())
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val TAG = "PlayerFragment"
    }
}
package com.example.bitmovin.player

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bitmovin.player.api.advertising.AdItem
import com.bitmovin.player.api.advertising.AdSource
import com.bitmovin.player.api.advertising.AdSourceType
import com.bitmovin.player.api.advertising.AdvertisingConfig
import com.bitmovin.player.api.media.subtitle.SubtitleTrack
import com.bitmovin.player.api.media.thumbnail.ThumbnailTrack
import com.bitmovin.player.api.source.Source
import com.bitmovin.player.api.source.SourceConfig
import com.bitmovin.player.api.ui.StyleConfig
import com.example.bitmovin.databinding.MplayMediaPlayerComponentPlayerBinding

data class AttrsPlayerComponent(
    val videoDashUrl: String? = null,
    val videoHlsUrl: String? = null,
    val thumbnailsUrl: String,
    val subtitles: List<AttrsPlayerComponentSubtitle>? = null,
    val ads: String? = null
)

data class AttrsPlayerComponentSubtitle(
    val url: String = "https://platform-static.cdn.mdstrm.com/subs/60affd0addf19308279e2d16_62d77b709bb4e60836d97de0_1658773186323.vtt",
    val label: String = "Español",
    val language: String = "es-CL"
)

class PlayerComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: MplayMediaPlayerComponentPlayerBinding? = null

    init {
        if (binding == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = MplayMediaPlayerComponentPlayerBinding.inflate(inflater, this, true)
        }
    }

    fun setAttributes(attrs: AttrsPlayerComponent) {
        setAds(attrs.ads)
        configPlayer(attrs)
        setupClickBuntton()
    }

    private fun configPlayer(attrs: AttrsPlayerComponent) = binding?.playerVideo?.player?.apply {
        config.playbackConfig.isAutoplayEnabled = true
        binding?.playerVideo?.keepScreenOn = true
        config.remoteControlConfig.isCastEnabled = true
        attrs.videoHlsUrl?.let { safeUrl ->
            val sourceConfig = SourceConfig.fromUrl(safeUrl).also {
                it.metadata = mapOf(
                    Pair(
                        "vmapAdsRequest",
                        "https://pubads.g.doubleclick.net/gampad/ads?npa=0&sz=1920x1080&gdfp_req=1&output=vmap&unviewed_position_start=1&env=vp&impl=s&vad_type=linear&ad_rule=1&iu=/105773011/MELIPLAY-AR&description_url=https%3A%2F%2Fwww.mercadolibre.com.ar%2Fplay%2Fcontent%2Fdf2c8fa04c18459ea40a6ffc6a68354d&vid_d=6061&tfcd=0&ppid=dd2236c9cd2cbbd60fe29c46d0cb4896&cust_params=content_provider%3DSONY%26content_id%3Ddf2c8fa04c18459ea40a6ffc6a68354d%26rating%3D+14%26release_year%3D2013%26site_id%3DMLC%26ppid%3Ddd2236c9cd2cbbd60fe29c46d0cb4896"
                    )
                )
            }
            sourceConfig.thumbnailTrack = ThumbnailTrack(attrs.thumbnailsUrl)
            attrs.subtitles?.let {
                sourceConfig.subtitleTracks = it.toSubtitleTracks()
            }
            val source = Source.create(sourceConfig)
            load(source)
        }
    }

    private fun setAds(ads: String?) = binding?.playerVideo?.player?.apply {
        ads?.let { safeAds ->
            val adsVant = AdSource(AdSourceType.Ima, safeAds)
            val newAd = AdItem(adsVant)
            val advertising = AdvertisingConfig(newAd)
            config.advertisingConfig = advertising
            config.styleConfig = StyleConfig().also {
                it.isHideFirstFrame = true
            }
        }
    }

    private fun List<AttrsPlayerComponentSubtitle>.toSubtitleTracks() = map {
        it.toSubtitleTracks()
    }

    private fun AttrsPlayerComponentSubtitle.toSubtitleTracks() = SubtitleTrack(
        url = url, label = label, language = language
    )

    private fun unload() = binding?.playerVideo?.player?.apply {
        unload()
    }

    private fun dealloc() = binding?.playerVideo?.apply {
        player?.onStop()
        unload()
        player?.destroy()
        onDestroy()
    }

    private fun setupClickBuntton() = binding?.playerVideo?.player?.apply {
        binding?.forwardVideo?.setOnClickListener {
            seek(currentTime + 600)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dealloc()
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        binding?.playerVideo?.player = null
    }
}

package com.example.player

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
import com.example.player.databinding.MplayMediaPlayerComponentPlayerBinding

data class AttrsPlayerComponent(
    val videoDashUrl: String? = null,
    val videoHlsUrl: String? = null,
    val thumbnailsUrl: String,
    val subtitles: List<AttrsPlayerComponentSubtitle>? = null,
    val ads: String? = null
)

data class AttrsPlayerComponentSubtitle(
    val url: String,
    val label: String,
    val language: String
)

class PlayerComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: MplayMediaPlayerComponentPlayerBinding? = null

    init {
        if (binding == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = MplayMediaPlayerComponentPlayerBinding.inflate(inflater, this)
        }
    }

    fun setAttributes(attrs: AttrsPlayerComponent) {
        configPlayer(attrs)
    }

    private fun configPlayer(attrs: AttrsPlayerComponent) = binding?.playerVideo?.player?.apply {
        config.playbackConfig.isAutoplayEnabled = true
        setAds(attrs.ads)
        resolveSourceUrl(attrs.videoDashUrl, attrs.videoHlsUrl)?.let { safeUrl ->
            val sourceConfig = SourceConfig.fromUrl(safeUrl)
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

    private fun resolveSourceUrl(videoDashUrl: String?, videoHlsUrl: String?): String? {
        return if (!videoDashUrl.isNullOrEmpty()) {
            videoDashUrl
        } else {
            videoHlsUrl
        }
    }

    private fun List<AttrsPlayerComponentSubtitle>.toSubtitleTracks() = map {
        it.toSubtitleTracks()
    }

    private fun AttrsPlayerComponentSubtitle.toSubtitleTracks() = SubtitleTrack(
        url = url,
        label = label,
        language = language
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dealloc()
    }
}

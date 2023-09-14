package com.example.player

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.bitmovin.player.PlayerView
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.PlayerConfig
import com.bitmovin.player.api.advertising.AdItem
import com.bitmovin.player.api.advertising.AdSource
import com.bitmovin.player.api.advertising.AdSourceType
import com.bitmovin.player.api.advertising.AdvertisingConfig
import com.bitmovin.player.api.event.on
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
    val url: String = "https://platform-static.cdn.mdstrm.com/subs/60affd0addf19308279e2d16_62d77b709bb4e60836d97de0_1658773186323.vtt",
    val label: String = "Español",
    val language: String = "es-CL"
)

class PlayerComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: MplayMediaPlayerComponentPlayerBinding? = null
    private var bitmovinLicense = "4cf22514-2ac7-4f71-9d30-4a41ee669422"
    private var playerView: PlayerView? = null
    private var playerUi: PlayerUi? = null

    init {
        if (binding == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = MplayMediaPlayerComponentPlayerBinding.inflate(inflater, this)
        }
    }

    fun setAttributes(attrs: AttrsPlayerComponent) {
        configPlayer(attrs)
        loadVideo(attrs)
    }

    private fun configPlayer(attrs: AttrsPlayerComponent) {
        playerView?.keepScreenOn = true

        val player = Player.create(context, getPlayerConfig())

        playerView = PlayerView(context, player).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        playerUi = PlayerUi(context).also {
            it.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }

        binding?.playerUiContainer?.addView(playerUi)
        binding?.playerComponentContainer?.addView(playerView)
    }

    private fun getPlayerConfig() = PlayerConfig(key = bitmovinLicense).also {
        it.styleConfig.isUiEnabled = false
        it.bufferConfig.audioAndVideo.forwardDuration = 10.0
        it.bufferConfig.startupThreshold = 5.0
        it.adaptationConfig.maxSelectableVideoBitrate = 500000
        it.playbackConfig.isMuted = true
        it.remoteControlConfig.isCastEnabled = false
    }

    private fun loadVideo(attrs: AttrsPlayerComponent) = playerView?.player?.apply {
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

    /*private fun configPlayer(attrs: AttrsPlayerComponent) = binding?.playerVideo?.player?.apply {
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
*/
    private fun setAds(ads: String?) = playerView?.player?.apply {
        ads?.let { safeAds ->
            val adsVant = AdSource(AdSourceType.Ima, safeAds)
            val newAd = AdItem(adsVant)
            val advertising = AdvertisingConfig(newAd)
            config.advertisingConfig = advertising
            config.styleConfig = StyleConfig().also {
                it.isHideFirstFrame = true
                it.isUiEnabled = false
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

    private fun unload() = playerView?.player?.apply {
        unload()
    }

    private fun dealloc() = playerView?.apply {
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

package com.example.player

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.player.databinding.MplayAutoplayUiBinding

class PlayerUi(context: Context) : ConstraintLayout(context) {

    private var binding: MplayAutoplayUiBinding? = null

    init {
        if (binding == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            binding = MplayAutoplayUiBinding.inflate(inflater, this)
        }
    }
}
package com.ficko.rssfeed.ui.common

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("android:visibleIf")
    fun View.visibleIf(visible: Boolean?) {
        visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun ImageView.setImageUrl(url: String?) {
        url?.let {
            if (url.isNotBlank()) Picasso.get().load(url).into(this)
        }
    }
}
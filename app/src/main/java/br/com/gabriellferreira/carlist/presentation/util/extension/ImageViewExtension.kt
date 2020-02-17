package br.com.gabriellferreira.carlist.presentation.util.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadCenterCrop(url: String, listener: RequestListener<Drawable>) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .listener(listener)
        .into(this)
}
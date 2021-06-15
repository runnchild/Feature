package com.rongc.demo.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("url")
fun ImageView.url(url: String) {
    Glide.with(this).load(url).into(this)
}
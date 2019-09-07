package com.inu.cafeteria.common.extension

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.inu.cafeteria.R
import timber.log.Timber

fun ImageView.loadFromUrl(url: String) =
    Glide.with(context.applicationContext)
        .load(url)
        .fallback(R.drawable.no_img)
        .error(R.drawable.no_img)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadUrlAndResumeEnterTransition(url: String, activity: FragmentActivity) {
    val target: Target<Drawable> = ImageViewBaseTarget(this, activity)
    Glide.with(context.applicationContext)
        .load(url)
        .fallback(R.drawable.no_img)
        .error(R.drawable.no_img)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(target)
}

fun ImageView.loadFromDrawable(@DrawableRes resId: Int) =
    Glide.with(context.applicationContext)
        .load(resId)
        .fallback(R.drawable.no_img)
        .error(R.drawable.no_img)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadDrawableAndResumeEnterTransition(@DrawableRes resId: Int, activity: FragmentActivity) {
    val target: Target<Drawable> = ImageViewBaseTarget(this, activity)
    Glide.with(context.applicationContext)
        .load(resId)
        .fallback(R.drawable.no_img)
        .error(R.drawable.no_img)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(target)
}


/**
 * Base target class for an image view associated with activity transition.
 */
private class ImageViewBaseTarget (var imageView: ImageView, var activity: FragmentActivity)
    : CustomViewTarget<ImageView, Drawable>(imageView) {

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        imageView.setImageDrawable(resource)
        activity.supportStartPostponedEnterTransition()

        Timber.v("Resource ready")
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        imageView.setImageResource(R.drawable.no_img)
        activity.supportStartPostponedEnterTransition()

        Timber.i("Resource load failed.")
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        activity.supportStartPostponedEnterTransition()

        Timber.i("Resource cleared.")
    }
}

fun ImageView.setTint(color: Int) {
    imageTintList = ColorStateList.valueOf(color)
}

fun ImageView.setTintRes(@ColorRes colorResId: Int) {
    setTint(resources.getColor(colorResId, null))
}

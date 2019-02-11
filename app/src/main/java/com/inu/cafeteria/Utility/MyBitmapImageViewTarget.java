package com.inu.cafeteria.Utility;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class MyBitmapImageViewTarget extends BitmapImageViewTarget {

    // https://github.com/lishide/ImgCoverFlow 소스 참고

    public MyBitmapImageViewTarget(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(Bitmap resource) {
        super.setResource(resource);
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadStarted(placeholder);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        if (errorDrawable != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadFailed(e, errorDrawable);
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        super.onLoadCleared(placeholder);
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

        if (resource != null && view.getScaleType() != ImageView.ScaleType.FIT_XY) {
            view.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        super.onResourceReady(resource, glideAnimation);
    }
}

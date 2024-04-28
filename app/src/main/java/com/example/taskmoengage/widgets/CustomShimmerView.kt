package com.example.taskmoengage.widgets

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import com.example.taskmoengage.R

class CustomShimmerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    private val skeletonDrawable: GradientDrawable

    init {
        skeletonDrawable =
            ContextCompat.getDrawable(context, R.drawable.skeleton_loading) as GradientDrawable
        init()
    }

    private fun init() {
        background = skeletonDrawable
        startSkeletonAnimation()
    }

    private fun startSkeletonAnimation() {
        val scaleAnimation = ScaleAnimation(1f, 1f, 1f, 1f)
        scaleAnimation.duration = 500
        scaleAnimation.repeatCount = Animation.INFINITE
        scaleAnimation.repeatMode = Animation.REVERSE

        val alphaAnimation = AlphaAnimation(1f, 0.5f)
        alphaAnimation.duration = 500
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE

        val animationSet = AnimationSet(true)
        animationSet.interpolator = DecelerateInterpolator()
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)

        startAnimation(animationSet)
    }


    fun clearAnim() {
        clearAnimation()
    }
}
package com.slobodanantonijevic.simpleopenweatherkt.util

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.slobodanantonijevic.simpleopenweatherkt.R

/**
 * A class to build our basic animations into
 */
internal object Animations {

    /**
     * Simple expand animation
     * @param ctx context on where the animation is taking place
     * @param v animating view
     */
    fun expand(ctx: Context, v: View?) {

        val a = AnimationUtils.loadAnimation(ctx, R.anim.layout_anim_expand)
        if (a != null) {

            a.reset()
            if (v != null) {

                v.clearAnimation()
                v.startAnimation(a)
            }
        }

        a!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // TODO Auto-generated method stub

                v!!.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationEnd(animation: Animation) {


            }
        })
    }

    /**
     * Simple collapse animation
     * @param ctx context on where the animation is taking place
     * @param v animating view
     */
    fun collapse(ctx: Context, v: View?) {

        val a = AnimationUtils.loadAnimation(ctx, R.anim.layout_anim_collapse)
        if (a != null) {

            a.reset()
            if (v != null) {

                v.clearAnimation()
                v.startAnimation(a)
            }
        }

        a!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationRepeat(animation: Animation) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationEnd(animation: Animation) {

                v!!.visibility = View.GONE
            }
        })
    }

    /**
     * Simple rotate animation
     * @param ctx context on where the animation is taking place
     * @param v animating view
     */
    fun rotate(ctx: Context, v: View?) {

        val a = AnimationUtils.loadAnimation(ctx, R.anim.item_anim_rotation)
        if (a != null) {

            a.reset()
            a.repeatCount = Animation.INFINITE
            a.repeatMode = Animation.INFINITE
            if (v != null) {

                v.clearAnimation()
                v.startAnimation(a)
            }
        }
    }
}
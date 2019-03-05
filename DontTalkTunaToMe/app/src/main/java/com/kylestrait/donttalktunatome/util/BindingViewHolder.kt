package com.kylestrait.donttalktunatome.util

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    val binding: ViewDataBinding?

    //    constructor(itemView: View) : super(itemView) {}

    //    fun clearAnimation() {
//        val v = binding!!.root
//        ViewCompat.setAlpha(v, 1f)
//        ViewCompat.setScaleY(v, 1f)
//        ViewCompat.setScaleX(v, 1f)
//        ViewCompat.setTranslationY(v, 0f)
//        ViewCompat.setTranslationX(v, 0f)
//        ViewCompat.setRotation(v, 0f)
//        ViewCompat.setRotationY(v, 0f)
//        ViewCompat.setRotationX(v, 0f)
//        ViewCompat.setPivotY(v, (v.measuredHeight / 2).toFloat())
//        ViewCompat.setPivotX(v, (v.measuredWidth / 2).toFloat())
//        ViewCompat.animate(v).setInterpolator(null).startDelay = 0
//    }
    init {
        this.binding = DataBindingUtil.bind(itemView)
    }
}
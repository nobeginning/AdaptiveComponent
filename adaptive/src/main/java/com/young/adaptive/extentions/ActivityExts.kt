package com.young.adaptive.extentions

import android.app.Activity
import androidx.annotation.LayoutRes
import com.young.adaptive.AdaptiveAssistant

fun Activity.setContentView(@LayoutRes layoutId:Int, adaptive:Boolean){
    if (adaptive){
        AdaptiveAssistant.setContentView(this, layoutId)
    } else {
        setContentView(layoutId)
    }
}
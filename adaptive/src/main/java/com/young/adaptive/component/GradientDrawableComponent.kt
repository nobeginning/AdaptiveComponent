package com.young.adaptive.component

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import com.young.adaptive.AdaptiveComponent

class GradientDrawableComponent : IComponent {
    override fun adaptive(view: View, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int) {
        val bgDrawable = view.background
        if (bgDrawable is GradientDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (bgDrawable.shape != GradientDrawable.RECTANGLE) {
                    return
                }
                if (bgDrawable.useLevel) {
                    return
                }
                bgDrawable.useLevel = true
                val corner = bgDrawable.cornerRadius
                if (corner > 0) {
                    bgDrawable.cornerRadius = AdaptiveComponent.calculate(designWidth, screenWidth, corner)
                } else {
                    val cornerArray = bgDrawable.cornerRadii ?: return
                    (0 until cornerArray.size).forEach {
                        val c = cornerArray[it]
                        if (c > 0) {
                            cornerArray[it] = AdaptiveComponent.calculate(designWidth, screenWidth, c)
                        }
                    }
                    bgDrawable.cornerRadii = cornerArray
                }
            } else {
                try {
                    val field = GradientDrawable::class.java.getDeclaredField("mGradientState")
                    field.isAccessible = true
                    val state = field.get(bgDrawable)
                    val useLevelField = state::class.java.getDeclaredField("mUseLevel")
                    useLevelField.isAccessible = true
                    val useLevel = useLevelField.get(state) as Boolean
                    if (useLevel) {
                        return
                    }
                    useLevelField.set(state, true)
                    val radiusField = state::class.java.getDeclaredField("mRadius")
                    radiusField.isAccessible = true
                    val corner = radiusField.get(state) as Float
                    if (corner > 0) {
                        radiusField.set(state, AdaptiveComponent.calculate(designWidth, screenWidth, corner))
                    } else {
                        val arrField = state::class.java.getDeclaredField("mRadiusArray")
                        val cornerArray = arrField.get(state) as FloatArray?
                        cornerArray ?: return
                        (0 until cornerArray.size).forEach {
                            val c = cornerArray[it]
                            if (c > 0) {
                                cornerArray[it] = AdaptiveComponent.calculate(designWidth, screenWidth, c)
                            }
                        }
                        arrField.set(state, cornerArray)
                    }

                } catch (e: Exception) {
                    println(e.message ?: e)
                }
            }
        }
    }
}
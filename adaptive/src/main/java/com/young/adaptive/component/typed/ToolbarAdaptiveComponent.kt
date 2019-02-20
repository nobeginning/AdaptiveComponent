package com.young.adaptive.component.typed

import android.os.Build
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.ViewTreeObserver
import android.widget.TextView
import com.young.adaptive.AdaptiveComponent
import com.young.adaptive.component.TypedComponent
import java.math.BigDecimal

class ToolbarAdaptiveComponent : TypedComponent<Toolbar> {
    override fun typedAdaptive(view: Toolbar, zoomRate: BigDecimal) {
        if (view.title == null) {
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    try {
                        val field = Toolbar::class.java.getDeclaredField("mTitleTextView")
                        field.isAccessible = true
                        val titleView: TextView = field.get(view) as TextView
                        val textSize = titleView.textSize
                        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AdaptiveComponent.calculate(zoomRate, textSize))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                    } catch (e: Exception) {
                        println(e.message ?: e.toString())
                    }
                }
            })
        }
    }
}
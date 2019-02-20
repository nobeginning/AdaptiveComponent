package com.young.adaptive.component

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.young.adaptive.AdaptiveComponent
import java.math.BigDecimal

/**
 * Created by Young on 2017/12/10.
 */
class TextSizeComponent : IComponent {
    override fun adaptive(view: View, zoomRate:BigDecimal) {
        if (view is TextView) {
            val textSize = view.textSize
            var adaptiveSize = AdaptiveComponent.calculate(zoomRate, textSize)
            adaptiveSize *= AdaptiveComponent.scaledDensity
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, adaptiveSize)
        }
    }
}
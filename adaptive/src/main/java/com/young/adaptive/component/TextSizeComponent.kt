package com.young.adaptive.component

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.young.adaptive.AdaptiveComponent

/**
 * Created by Young on 2017/12/10.
 */
class TextSizeComponent : IComponent {
    override fun adaptive(view: View, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int) {
        if (view is TextView) {
            val textSize = view.textSize
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, AdaptiveComponent.calculate(designHeight, screenHeight, textSize))
        }
    }
}
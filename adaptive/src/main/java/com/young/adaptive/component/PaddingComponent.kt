package com.young.adaptive.component

import android.view.View
import com.young.adaptive.AdaptiveComponent
import com.young.adaptive.PX_1
import com.young.adaptive.PX_UNIT
import java.math.BigDecimal

/**
 * Created by Young on 2017/12/10.
 */
class PaddingComponent : IComponent {
    override fun adaptive(view: View, zoomRate:BigDecimal) {
        var paddingLeft = 0
        if (view.paddingLeft > 0) {
            paddingLeft = AdaptiveComponent.calculate(zoomRate, view.paddingLeft)
        } else if (view.paddingLeft == PX_1) {
            paddingLeft = PX_UNIT
        }

        var paddingTop = 0
        if (view.paddingTop > 0) {
            paddingTop = AdaptiveComponent.calculate(zoomRate, view.paddingTop)
        } else if (view.paddingTop == PX_1) {
            paddingTop = PX_UNIT
        }

        var paddingRight = 0
        if (view.paddingRight > 0) {
            paddingRight = AdaptiveComponent.calculate(zoomRate, view.paddingRight)
        } else if (view.paddingRight == PX_1) {
            paddingRight = PX_UNIT
        }

        var paddingBottom = 0
        if (view.paddingBottom > 0) {
            paddingBottom = AdaptiveComponent.calculate(zoomRate, view.paddingBottom)
        } else if (view.paddingBottom == PX_1) {
            paddingBottom = PX_UNIT
        }

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }
}
package com.young.adaptive.component

import android.view.View
import android.view.ViewGroup
import com.young.adaptive.AdaptiveComponent
import com.young.adaptive.PX_1
import com.young.adaptive.PX_UNIT
import java.math.BigDecimal

/**
 * Created by Young on 2017/12/10.
 */
open class ParameterComponent : IComponent {
    override fun adaptive(view: View, zoomRate: BigDecimal) {
        val params: ViewGroup.LayoutParams? = view.layoutParams
        params?.apply {
            view.layoutParams = autoLayoutParameters(params, zoomRate, view)
        }
    }

    open fun autoLayoutParameters(params: ViewGroup.LayoutParams,
                                  zoomRate: BigDecimal,
                                  view: View): ViewGroup.LayoutParams {
        if (params.width > 0) {
            params.width = AdaptiveComponent.calculate(zoomRate, params.width)
        } else if (params.width == PX_1) {
            params.width = PX_UNIT
        }
        if (params.height > 0) {
            params.height = AdaptiveComponent.calculate(zoomRate, params.height)
        } else if (params.height == PX_1) {
            params.height = PX_UNIT
        }

        if (params is ViewGroup.MarginLayoutParams) {
            if (params.leftMargin > 0) {
                params.leftMargin = AdaptiveComponent.calculate(zoomRate, params.leftMargin)
            } else if (params.leftMargin == PX_1) {
                params.leftMargin = PX_UNIT
            }
            if (params.rightMargin > 0) {
                params.rightMargin = AdaptiveComponent.calculate(zoomRate, params.rightMargin)
            } else if (params.rightMargin == PX_1) {
                params.rightMargin = PX_UNIT
            }
            if (params.topMargin > 0) {
                params.topMargin = AdaptiveComponent.calculate(zoomRate, params.topMargin)
            } else if (params.topMargin == PX_1) {
                params.topMargin = PX_UNIT
            }
            if (params.bottomMargin > 0) {
                params.bottomMargin = AdaptiveComponent.calculate(zoomRate, params.bottomMargin)
            } else if (params.bottomMargin == PX_1) {
                params.bottomMargin = PX_UNIT
            }
        }
        return params
    }
}
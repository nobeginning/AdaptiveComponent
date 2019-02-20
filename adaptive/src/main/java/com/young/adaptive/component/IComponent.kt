package com.young.adaptive.component

import android.view.View
import java.math.BigDecimal

/**
 * Created by young on 2017/12/19.
 */
interface IComponent {
    /**
     * @param view  正在进行自适应处理的View
     * @param zoomRate 缩放率 screenWidth / designWidth
     */
    fun adaptive(view: View, zoomRate:BigDecimal)
}

interface TypedComponent<T : View> {
    fun typedAdaptive(view: T, zoomRate:BigDecimal)
}
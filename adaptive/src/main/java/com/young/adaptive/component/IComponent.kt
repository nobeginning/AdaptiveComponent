package com.young.adaptive.component

import android.view.View

/**
 * Created by young on 2017/12/19.
 */
interface IComponent {
    /**
     * @param view  正在进行自适应处理的View
     * @param screenWidth   屏幕宽度
     * @param screenHeight  屏幕高度
     * @param designWidth   设计稿宽度
     * @param designHeight  设计稿高度
     */
    fun adaptive(view: View, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int)
}

interface TypedComponent<T : View> {
    fun typedAdaptive(view: T, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int)
}
package com.young.adaptive.component

import android.view.View

/**
 * Created by young on 2017/12/19.
 */
interface IComponent {
    fun adaptive(view: View, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int)
}
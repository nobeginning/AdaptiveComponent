package com.young.adaptive

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.windowManager

/**
 * Created by Young on 2017/9/27.
 */

const val PX_1: Int = -10
const val PX_LINE: Int = -10
const val WRAP_CONTENT: Int = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT: Int = ViewGroup.LayoutParams.MATCH_PARENT

interface AdaptiveComponent<in T> {
    fun createView(ui: AnkoContext<T>): View
}

private const val PX_UNIT = 1

@Deprecated("Use #R.id.text_view_auto_size stead")
const val KEY_TAG_TEXT_SIZE_AUTO_LAYOUT: Int = 0x7fffffff

const val TAG_TEXT_SIZE_AUTO_LAYOUT: String = "YES"
private const val META_NAME_DESIGN_WIDTH: String = "com.young.adaptive.designWidth"
private const val META_NAME_DESIGN_HEIGHT: String = "com.young.adaptive.designHeight"
private const val LOG_TAG: String = "AdaptiveComponent"

fun <T:TextView> T.setAdaptiveTextSize(pxSize: Float) {
    this.setTag(R.id.text_view_auto_size, TAG_TEXT_SIZE_AUTO_LAYOUT)
    this.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, pxSize)
}

fun <T : Activity> AdaptiveComponent<T>.setContentView(activity: T): View =
        createView(AdaptiveLayoutContext(activity, activity, true))

open class AdaptiveLayoutContext<out T>(
        override val ctx: Context,
        override val owner: T,
        private val setContentView: Boolean
) : AnkoContext<T> {

    private var myView: View? = null
    private val displayMetrics: DisplayMetrics = DisplayMetrics()

    override val view: View
        get() = myView ?: throw IllegalStateException("View was not set previously")

    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        if (view == null) return

        if (myView != null) {
            alreadyHasView()
        }

        this.myView = view

        if (setContentView) {
            doAddView(ctx, view)
        }
    }

    private fun doAddView(context: Context, view: View) {
        ctx.windowManager.defaultDisplay.getMetrics(displayMetrics)
        when (context) {
            is Activity -> context.setContentView(autoLayout(view))
            is ContextWrapper -> doAddView(context.baseContext, view)
            else -> throw IllegalStateException("Context is not an Activity, can't set content view")
        }
    }

    private val designWidth: Int
            get() {
                if (metaData==null){
                    metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
                }
                return if (metaData!=null && metaData!!.containsKey(META_NAME_DESIGN_WIDTH)){
                    metaData!!.get(META_NAME_DESIGN_WIDTH) as Int
                } else {
                    0
                }
            }

    private val designHeight: Int
        get() {
            if (metaData==null){
                metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
            }
            return if (metaData!=null && metaData!!.containsKey(META_NAME_DESIGN_HEIGHT)){
                metaData!!.get(META_NAME_DESIGN_HEIGHT) as Int
            } else {
                0
            }
        }
    private var metaData: Bundle? = null


    private fun autoLayout(view: View): View {
        val params:ViewGroup.LayoutParams? = view.layoutParams
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        autoLayoutPadding(view, screenWidth, screenHeight)
        autoLayoutText(view, screenWidth, screenHeight)
        params?.apply {
            autoLayoutParameters(params, screenWidth, screenHeight, view)
        }
        if (view is ViewGroup) {
            val childCount = view.childCount
            (0 until childCount)
                    .map { view.getChildAt(it) }
                    .forEach { autoLayout(it) }
        }
//        else {
//            val params = view.layoutParams
//            val screenWidth = displayMetrics.widthPixels
//            val screenHeight = displayMetrics.heightPixels
//            autoLayoutPadding(view, screenWidth, screenHeight)
//            autoLayoutText(view, screenWidth, screenHeight)
//            autoLayoutParameters(params, screenWidth, screenHeight)
//        }
        return view
    }

    private fun autoLayoutPadding(view: View, screenWidth: Int, screenHeight: Int) {
        var paddingLeft = 0
        if (view.paddingLeft > 0) {
            paddingLeft = calculate(designWidth, screenWidth, view.paddingLeft)
        } else if (view.paddingLeft == PX_1) {
            paddingLeft = PX_UNIT
        }

        var paddingTop = 0
        if (view.paddingTop > 0) {
            paddingTop = calculate(designHeight, screenHeight, view.paddingTop)
        } else if (view.paddingTop == PX_1) {
            paddingTop = PX_UNIT
        }

        var paddingRight = 0
        if (view.paddingRight > 0) {
            paddingRight = calculate(designWidth, screenWidth, view.paddingRight)
        } else if (view.paddingRight == PX_1) {
            paddingRight = PX_UNIT
        }

        var paddingBottom = 0
        if (view.paddingBottom > 0) {
            paddingBottom = calculate(designHeight, screenHeight, view.paddingBottom)
        } else if (view.paddingBottom == PX_1) {
            paddingBottom = PX_UNIT
        }

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    private fun autoLayoutText(view: View, screenWidth: Int, screenHeight: Int) {
        if (view is TextView
                && TAG_TEXT_SIZE_AUTO_LAYOUT == view.getTag(R.id.text_view_auto_size)) {
            val textSize = view.textSize
            view.textSize = calculate(designHeight, screenHeight, textSize)
        }
    }

    private fun autoLayoutParameters(params: ViewGroup.LayoutParams, screenWidth: Int, screenHeight: Int, view: View) {
        if (params.width > 0) {
            params.width = calculate(designWidth, screenWidth, params.width)
        } else if (params.width == PX_1) {
            params.width = PX_UNIT
        }
        if (params.height > 0) {
            params.height = calculate(designHeight, screenHeight, params.height)
        } else if (params.height == PX_1) {
            params.height = PX_UNIT
        }

        if (params is ViewGroup.MarginLayoutParams) {
            if (params.leftMargin > 0) {
                params.leftMargin = calculate(designWidth, screenWidth, params.leftMargin)
            } else if (params.leftMargin == PX_1) {
                params.leftMargin = PX_UNIT
            }
            if (params.rightMargin > 0) {
                params.rightMargin = calculate(designWidth, screenWidth, params.rightMargin)
            } else if (params.rightMargin == PX_1) {
                params.rightMargin = PX_UNIT
            }
            if (params.topMargin > 0) {
                params.topMargin = calculate(designHeight, screenHeight, params.topMargin)
            } else if (params.topMargin == PX_1) {
                params.topMargin = PX_UNIT
            }
            if (params.bottomMargin > 0) {
                params.bottomMargin = calculate(designHeight, screenHeight, params.bottomMargin)
            } else if (params.bottomMargin == PX_1) {
                params.bottomMargin = PX_UNIT
            }
        }
        view.layoutParams = params
    }

    private fun calculate(designValue: Int, screeValue: Int, originValue: Int): Int {
        if (designValue <= 0) {
            Log.w(LOG_TAG, "Found design value **$designValue** is invalid. Have u forgot it?")
            return originValue
        }
        return (originValue.toDouble() * screeValue.toDouble() / designValue.toDouble()).toInt()
    }

    private fun calculate(designValue: Int, screeValue: Int, originValue: Float): Float {
        if (designValue <= 0) {
            Log.w(LOG_TAG, "Found design value **$designValue** is invalid. Have u forgot it?")
            return originValue
        }
        return (originValue.toDouble() * screeValue.toDouble() / designValue.toDouble()).toFloat()
    }

    open protected fun alreadyHasView(): Unit = throw IllegalStateException("View is already set: $myView")

}
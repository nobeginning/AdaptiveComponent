package com.young.adaptive

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.StringDef
import android.support.annotation.StringRes
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.young.adaptive.component.PaddingComponent
import com.young.adaptive.component.ParameterComponent
import com.young.adaptive.component.TextSizeComponent
import java.lang.annotation.RetentionPolicy
import java.util.*

/**
 * Created by Young on 2017/9/27.
 */
private const val DEBUG = true

const val PX_1: Int = -10
const val PX_LINE: Int = -10
const val WRAP_CONTENT: Int = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT: Int = ViewGroup.LayoutParams.MATCH_PARENT

const val PX_UNIT = 1

private const val META_NAME_DESIGN_WIDTH: String = "com.young.adaptive.designWidth"
private const val META_NAME_DESIGN_HEIGHT: String = "com.young.adaptive.designHeight"
private const val LOG_TAG: String = "AdaptiveComponent"

class LayoutAssistant {
    fun setContentLayout(activity: Activity, layoutId: Int) {
        val layout = LayoutInflater.from(activity).inflate(layoutId, null)
        AdaptiveLayoutContext(activity, activity, true).addView(layout, null)
    }

    fun autoLayout(context: Context, parentView: ViewGroup?, layoutId: Int): View {
        val layout = LayoutInflater.from(context).inflate(layoutId, parentView, false)
        return AdaptiveLayoutContext(context, context, false).doAutoLayout(context, layout)
    }
}

interface AdaptiveViewManager<out T> : ViewManager {
    abstract val ctx: Context
    abstract val owner: T
    abstract val view: View
}

interface IComponent {
    fun adaptive(view: View, screenWidth: Int, screenHeight: Int, designWidth: Int, designHeight: Int)
}

const val COMPONENT_PRESET_PADDING = "COMPONENT_PRESET_PADDING"
const val COMPONENT_PRESET_PARAMETER = "COMPONENT_PRESET_PARAMETER"
const val COMPONENT_PRESET_TEXT_SIZE = "COMPONENT_PRESET_TEXT_SIZE"

public object AdaptiveComponent {

    @StringDef(COMPONENT_PRESET_PADDING, COMPONENT_PRESET_PARAMETER, COMPONENT_PRESET_TEXT_SIZE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class PresetComponent

    private val components: LinkedList<IComponent> = LinkedList()
    private val presetPaddingComponent: IComponent = PaddingComponent()
    private val presetParameterComponent: IComponent = ParameterComponent()
    private val presetTextSizeComponent: IComponent = TextSizeComponent()

    init {
        components.add(presetPaddingComponent)
        components.add(presetParameterComponent)
        components.add(presetTextSizeComponent)
    }

    fun initWithoutPreset() {
        remove(presetPaddingComponent)
        remove(presetParameterComponent)
        remove(presetTextSizeComponent)
    }

    fun removePresetComponent(@PresetComponent component: String) {
        when (component) {
            COMPONENT_PRESET_PADDING -> remove(presetPaddingComponent)
            COMPONENT_PRESET_PARAMETER -> remove(presetParameterComponent)
            COMPONENT_PRESET_TEXT_SIZE -> remove(presetTextSizeComponent)
        }
    }

    fun add(component: IComponent) {
        components.add(component)
    }

    fun remove(component: IComponent) {
        components.remove(component)
    }

    fun clear() {
        components.clear()
    }

    fun getAllComponents(): List<IComponent> {
        return components
    }
}

class AdaptiveLayoutContext<out T>(
        override val ctx: Context,
        override val owner: T,
        private val setContentView: Boolean
) : AdaptiveViewManager<T> {

    companion object {
        fun calculate(designValue: Int, screeValue: Int, originValue: Int): Int {
            if (designValue <= 0) {
                Log.w(LOG_TAG, "Found design value **$designValue** is invalid. Have u forgot it?")
                return originValue
            }
            var result = (originValue.toDouble() * screeValue.toDouble() / designValue.toDouble()).toInt()
            if (result<=0){
                result = 1
            }
            return result
        }

        fun calculate(designValue: Int, screeValue: Int, originValue: Float): Float {
            if (designValue <= 0) {
                Log.w(LOG_TAG, "Found design value **$designValue** is invalid. Have u forgot it?")
                return originValue
            }
            var result = (originValue.toDouble() * screeValue.toDouble() / designValue.toDouble()).toFloat()
            if (result<1f){
                result = 1f
            }
            return result
        }
    }

    override fun removeView(view: View?) {
        if (DEBUG) {
            println("AdaptiveLayoutContext: removeView: view -- $view")
        }
    }

    override fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {
        if (DEBUG) {
            println("AdaptiveLayoutContext: updateViewLayout: view -- $view ; params -- $params")
        }
    }

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
        when (context) {
            is Activity -> context.setContentView(doAutoLayout(context, view))
            is ContextWrapper -> doAddView(context.baseContext, view)
            else -> throw IllegalStateException("Context is not an Activity, can't set content view")
        }
    }

    private val designWidth: Int
        get() {
            if (metaData == null) {
                metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
            }
            return if (metaData != null && metaData!!.containsKey(META_NAME_DESIGN_WIDTH)) {
                metaData!!.get(META_NAME_DESIGN_WIDTH) as Int
            } else {
                0
            }
        }

    private val designHeight: Int
        get() {
            if (metaData == null) {
                metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
            }
            return if (metaData != null && metaData!!.containsKey(META_NAME_DESIGN_HEIGHT)) {
                metaData!!.get(META_NAME_DESIGN_HEIGHT) as Int
            } else {
                0
            }
        }
    private var metaData: Bundle? = null

    fun doAutoLayout(context: Context, view: View): View {
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return autoLayout(context, view)
    }

    private fun autoLayout(context: Context, view: View): View {
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val components = AdaptiveComponent.getAllComponents()
        components.forEach {
            it.adaptive(view, screenWidth, screenHeight, designWidth, designHeight)
        }

        if (view is ViewGroup) {
            val childCount = view.childCount
            (0 until childCount)
                    .map { view.getChildAt(it) }
                    .forEach { autoLayout(context, it) }
        }
        return view
    }

    fun alreadyHasView(): Unit = throw IllegalStateException("View is already set: $myView")

}
package com.young.adaptive

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.StringDef
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.young.adaptive.component.IComponent
import com.young.adaptive.component.PaddingComponent
import com.young.adaptive.component.ParameterComponent
import com.young.adaptive.component.TextSizeComponent
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

class AdaptiveAssistant {
    fun setContentView(activity: Activity, layoutId: Int) {
        val layout = LayoutInflater.from(activity).inflate(layoutId, null)
        AdaptiveLayoutContext(activity, activity, true).addView(layout, null)
    }

    fun adaptive(context: Context, parentView: ViewGroup?, layoutId: Int): View {
        val layout = LayoutInflater.from(context).inflate(layoutId, parentView, false)
        return AdaptiveLayoutContext(context, context, false).doAdaptive(context, layout)
    }

    fun adaptiveWidth(context: Context, width: Int): Int {
        return AdaptiveComponent.calculate(AdaptiveComponent.getDesignWidth(context), AdaptiveComponent.getScreenWidth(context), width)
    }

    fun adaptiveHeight(context: Context, height: Int): Int {
        return AdaptiveComponent.calculate(AdaptiveComponent.getDesignHeight(context), AdaptiveComponent.getScreenHeight(context), height)
    }
}

interface AdaptiveViewManager<out T> : ViewManager {
    abstract val ctx: Context
    abstract val owner: T
    abstract val view: View
}

const val COMPONENT_PRESET_PADDING = "COMPONENT_PRESET_PADDING"
const val COMPONENT_PRESET_PARAMETER = "COMPONENT_PRESET_PARAMETER"
const val COMPONENT_PRESET_TEXT_SIZE = "COMPONENT_PRESET_TEXT_SIZE"

public object AdaptiveComponent {

    private var designWidth: Int = -1
    private var designHeight: Int = -1
    private var metaData: Bundle? = null
    private var displayMetrics: DisplayMetrics? = null

    private fun initDisplayMetrics(context: Context){
        displayMetrics = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    fun getScreenWidth(context: Context):Int{
        if (displayMetrics==null){
            initDisplayMetrics(context)
        }
        return displayMetrics!!.widthPixels
    }

    fun getScreenHeight(context: Context):Int{
        if (displayMetrics==null){
            initDisplayMetrics(context)
        }
        return displayMetrics!!.heightPixels
    }

    fun getDesignWidth(ctx: Context):Int{
        if (designWidth>-1){
            return designWidth
        }
        if (metaData == null) {
            metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
        }
        return if (metaData != null && metaData!!.containsKey(META_NAME_DESIGN_WIDTH)) {
            designWidth = metaData!!.get(META_NAME_DESIGN_WIDTH) as Int
            designWidth
        } else {
            0
        }
    }

    fun getDesignHeight(ctx: Context):Int{
        if (designHeight>-1){
            return designHeight
        }
        if (metaData == null) {
            metaData = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)?.metaData
        }
        return if (metaData != null && metaData!!.containsKey(META_NAME_DESIGN_HEIGHT)) {
            designHeight = metaData!!.get(META_NAME_DESIGN_HEIGHT) as Int
            designHeight
        } else {
            0
        }
    }

    fun calculate(designValue: Int, screeValue: Int, originValue: Int): Int {
        if (designValue <= 0) {
            Log.w(LOG_TAG, "Found design value **$designValue** is invalid. Have u forgot it?")
            return originValue
        }
        var result = (originValue.toDouble() * screeValue.toDouble() / designValue.toDouble()).toInt()
        if (result <= 0) {
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
        if (result < 1f) {
            result = 1f
        }
        return result
    }

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
        val ctx: Context,
        val owner: T,
        private val setContentView: Boolean
) {

    fun removeView(view: View?) {
        if (DEBUG) {
            println("AdaptiveLayoutContext: removeView: view -- $view")
        }
    }

    fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {
        if (DEBUG) {
            println("AdaptiveLayoutContext: updateViewLayout: view -- $view ; params -- $params")
        }
    }

    private var myView: View? = null


    val view: View
        get() = myView ?: throw IllegalStateException("View was not set previously")

    fun addView(view: View?, params: ViewGroup.LayoutParams?) {
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
            is Activity -> context.setContentView(doAdaptive(context, view))
            is ContextWrapper -> doAddView(context.baseContext, view)
            else -> throw IllegalStateException("Context is not an Activity, can't set content view")
        }
    }



    fun doAdaptive(context: Context, view: View): View {
        return adaptiveView(context, view)
    }

    private fun adaptiveView(context: Context, view: View): View {
        val screenWidth = AdaptiveComponent.getScreenWidth(context)
        val screenHeight = AdaptiveComponent.getScreenHeight(context)

        val components = AdaptiveComponent.getAllComponents()
        components.forEach {
            it.adaptive(view, screenWidth, screenHeight, AdaptiveComponent.getDesignWidth(ctx), AdaptiveComponent.getDesignHeight(ctx))
        }

        if (view is ViewGroup) {
            val childCount = view.childCount
            (0 until childCount)
                    .map { view.getChildAt(it) }
                    .forEach { adaptiveView(context, it) }
        }
        return view
    }

    fun alreadyHasView(): Unit = throw IllegalStateException("View is already set: $myView")

}
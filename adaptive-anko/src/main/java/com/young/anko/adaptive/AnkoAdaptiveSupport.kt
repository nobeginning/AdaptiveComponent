package com.young.anko.adaptive

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.young.adaptive.AdaptiveLayoutContext
import org.jetbrains.anko.AnkoContext

/**
 * Created by young on 2017/12/8.
 */

interface AnkoAdaptiveComponent<in T> {
    fun createView(ui: AnkoContext<T>): View
}

fun <T : Activity> AnkoAdaptiveComponent<T>.setContentView(activity: T): View =
        createView(AnkoAdaptiveLayoutContext(activity, activity, true))

open class AnkoAdaptiveLayoutContext<out T>(
        override val ctx: Context,
        override val owner: T,
        private val setContentView: Boolean
) : AnkoContext<T> {

    private val adaptiveComponent: AdaptiveLayoutContext<T> = AdaptiveLayoutContext(ctx, owner, setContentView)

    override val view: View
        get() = adaptiveComponent.view

    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        adaptiveComponent.addView(view, params)
    }
}
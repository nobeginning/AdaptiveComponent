package com.young.anko.adaptive

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.young.adaptive.AdaptiveLayoutContext
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

/**
 * Created by young on 2017/12/8.
 */

interface AnkoAdaptiveComponent<in T> {
    fun createView(ui: AnkoContext<T>): View
}

fun <T : Activity> AnkoAdaptiveComponent<T>.setContentView(activity: T): View =
        createView(AnkoAdaptiveLayoutContext(activity, activity, true))

fun <T : Activity> AnkoComponent<T>.setAdaptiveContentView(activity: T): View =
        createView(AnkoAdaptiveLayoutContext(activity, activity, true))

open class AnkoAdaptiveLayoutContext<out T>(
        override val ctx: Context,
        override val owner: T,
        private val setContentView: Boolean
) : AnkoContext<T> {

    private val adaptiveComponent: AdaptiveLayoutContext<T> = AdaptiveLayoutContext(ctx, owner, setContentView)

    private val ankoContext:AnkoContext<T> by lazy {
        AnkoContext.create(ctx, owner, setContentView)
    }

    override val view: View
        get() = adaptiveComponent.view

    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        if (view!=null && view.isInEditMode) {
            ankoContext.addView(view, params)
        } else {
            adaptiveComponent.addView(view, params)
        }
    }
}
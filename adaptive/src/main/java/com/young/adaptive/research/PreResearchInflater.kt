package com.young.adaptive.research

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

/**
 * Created by young on 2017/12/8.
 */
class PreResearchInflater(layoutInflater: LayoutInflater, context: Context?) : LayoutInflater(layoutInflater, context) {
    private val sClassPrefixList = arrayOf("android.widget.", "android.webkit.", "android.app.")

    override fun cloneInContext(newContext: Context?): LayoutInflater {
        return PreResearchInflater(this, newContext)
    }

    override fun onCreateView(parent: View?, name: String?, attrs: AttributeSet?): View {
        println("LayoutInflater: onCreateView:3")
        return super.onCreateView(parent, name, attrs)
    }

    override fun onCreateView(name: String?, attrs: AttributeSet?): View {
        println("LayoutInflater: onCreateView:2")
        for (prefix in sClassPrefixList) {
            try {
                val view = createView(name, prefix, attrs)
                if (view != null) {
                    return view
                }
            } catch (e: ClassNotFoundException) {
                // In this case we want to let the base class take a crack
                // at it.
            }

        }

        return super.onCreateView(name, attrs)
    }
}
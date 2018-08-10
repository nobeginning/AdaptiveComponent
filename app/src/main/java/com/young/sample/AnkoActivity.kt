package com.young.sample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.young.adaptive.*
import com.young.anko.adaptive.AnkoAdaptiveComponent
import com.young.anko.adaptive.setAdaptiveContentView
import com.young.anko.adaptive.setContentView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout

class AnkoActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, AnkoActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnkoActivityUI().setAdaptiveContentView(this)
    }
}
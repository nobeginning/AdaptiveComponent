package com.young.sample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.young.adaptive.*
import org.jetbrains.anko.*

class AnkoActivity : AppCompatActivity() {

    companion object {
        fun launch(context:Context){
            context.startActivity(Intent(context, AnkoActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UI().setContentView(this)
    }
}

class UI : AdaptiveComponent<AnkoActivity> {
    override fun createView(ui: AnkoContext<AnkoActivity>): View {
        return with(ui){
            scrollView {
                verticalLayout {
                    textView("Hello Auto Layout Anko!") {
                        tag = "HELLO"
                        setAdaptiveTextSize(24f)
                        setBackgroundColor(Color.DKGRAY)
                        setPadding(40, 10, 60, 20)
                        setOnClickListener { toast("Clicked Text") }
                    }.lparams(MATCH_PARENT, WRAP_CONTENT) {
                        setMargins(11, 22, 33, 44)
                    }
                    view {
                        setBackgroundColor(Color.parseColor("#dddddd"))
                    }.lparams(ViewGroup.LayoutParams.MATCH_PARENT, PX_1) {
                        setMargins(20, 20, 60, 30)
                    }
                    button("Click Me") {
                        setAdaptiveTextSize(18f)
                        setOnClickListener { toast("Oh, NO") }
                    }.lparams {
                        setMargins(10, 20, 30, 40)
                    }
                    button("点我试试") {
                        setAdaptiveTextSize(18f)
                        setOnClickListener { toast("试试就试试") }
                    }.lparams {
                        setMargins(40, 10, 20, 10)
                    }
                }
            }
        }
    }

}
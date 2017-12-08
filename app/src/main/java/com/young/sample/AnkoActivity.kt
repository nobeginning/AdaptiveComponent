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
import com.young.anko.adaptive.AdaptiveComponent
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
        UI().setContentView(this)
    }
}

class UI : AdaptiveComponent<AnkoActivity> {
    override fun createView(ui: AnkoContext<AnkoActivity>): View {
        println("UI: Start create view")
        return with(ui) {
            verticalLayout {
                appBarLayout {
                    val toolbar = toolbar {
                        title = "Anko-New-B"
//                        textView("Anko-New-B").lparams(Gravity.CENTER)
                    }
                    owner.setSupportActionBar(toolbar)
                }.lparams(MATCH_PARENT, 200)
                scrollView {
                    verticalLayout {
                        relativeLayout {
                            backgroundColor = Color.GRAY
                            horizontalPadding = 20
                            verticalPadding = 30

                            textView("Hello Auto Layout Anko!") {
                                tag = "HELLO"
                                setTextSize(TypedValue.COMPLEX_UNIT_PX, 30f)
                                setBackgroundColor(Color.DKGRAY)
                                setPadding(40, 10, 60, 20)
                                setOnClickListener { toast("Clicked Text") }
                            }.lparams(MATCH_PARENT, WRAP_CONTENT) {
                                setMargins(11, 22, 33, 44)
                            }
                        }.lparams(MATCH_PARENT, 640){
                            horizontalMargin = 10
                            verticalMargin = 20
                        }

                        view {
                            setBackgroundColor(Color.parseColor("#dddddd"))
                        }.lparams(ViewGroup.LayoutParams.MATCH_PARENT, PX_1) {
                            setMargins(20, 20, 60, 30)
                        }
                        button("Click Me") {
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, 18f)
                            setOnClickListener { toast("Oh, NO") }
                        }.lparams {
                            setMargins(10, 20, 30, 40)
                        }
                        button("点我试试") {
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, 18f)
                            setOnClickListener { toast("试试就试试") }
                        }.lparams {
                            setMargins(40, 10, 20, 10)
                        }
                        textView("Match"){
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, 62f)
                            leftPadding = 360
                            backgroundColor = Color.BLUE
                        }.lparams(MATCH_PARENT, WRAP_CONTENT)
                    }
                }
            }
        }
    }

}
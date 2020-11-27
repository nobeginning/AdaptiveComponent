package com.young.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.young.adaptive.AdaptiveAssistant
import kotlinx.android.synthetic.main.activity_normal.*

class NormalActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, NormalActivity::class.java))
        }
    }

    var tSize = 16f
    private val text16 = "wrap * wrap : marginStart-100px : tSize:16px\nClick me to change the textSize to 26px"
    private val text26 = "wrap * wrap : marginStart-100px : tSize:26px\nClick me to change the textSize to 16px"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdaptiveAssistant.setContentView(this, R.layout.activity_normal)
        println(toolbar)
        button.setOnClickListener {
            button.text = if (tSize == 16f) {
                tSize = 26f
                text26
            } else {
                tSize = 16f
                text16
            }
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, AdaptiveAssistant.calculateAdaptiveTextSize(this, tSize))
        }
        setSupportActionBar(toolbar)
    }


}

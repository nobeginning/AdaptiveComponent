package com.young.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.young.adaptive.AdaptiveAssistant
import kotlinx.android.synthetic.main.activity_normal.*

class NormalActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context){
            context.startActivity(Intent(context, NormalActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdaptiveAssistant.setContentView(this, R.layout.activity_normal)
        println(toolbar)
        setSupportActionBar(toolbar)
    }


}

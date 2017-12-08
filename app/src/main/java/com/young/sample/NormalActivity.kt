package com.young.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.young.adaptive.LayoutAssistant
import kotlinx.android.synthetic.main.activity_normal.*
import kotlinx.android.synthetic.main.cell_for_list.view.*

class NormalActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context){
            context.startActivity(Intent(context, NormalActivity::class.java))
        }
    }

    val layoutAssistant:LayoutAssistant = LayoutAssistant()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutAssistant.setContentLayout(this, R.layout.activity_normal)
        listView.adapter = MyAdapter()
    }

    inner class MyAdapter: BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutAssistant.autoLayout(this@NormalActivity, parent ,R.layout.cell_for_list)
//            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
//            val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
            view.tvTitle.text = "Title -> ${getItemId(position)}"
            view.tvDesc.text = "Description -> ${getItemId(position)}"
            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 44
        }

    }
}

package com.young.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import com.young.adaptive.AdaptiveAssistant
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.cell_for_list.view.*

class ListActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdaptiveAssistant.setContentView(this, R.layout.activity_list)
        listView.adapter = MyAdapter()
    }

    inner class MyAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView
                    ?: AdaptiveAssistant.adaptive(this@ListActivity, parent, R.layout.cell_for_list)
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

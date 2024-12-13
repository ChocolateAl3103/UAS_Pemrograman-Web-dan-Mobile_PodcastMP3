package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(private var context: Context, private var modelArrayList: ArrayList<ColorSpace.Model>, private var stat: String): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val inflater    = LayoutInflater.from(parent.context)
        val view        = inflater.inflate(R.layout.view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        val model       = modelArrayList[position]
        Picasso.get().load(model.image).into(holder.ImageView)
        holder.textCr.text      = model.title
    }
}
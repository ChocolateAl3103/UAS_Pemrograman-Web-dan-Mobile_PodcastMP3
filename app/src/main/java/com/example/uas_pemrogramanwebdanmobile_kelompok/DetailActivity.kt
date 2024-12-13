package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var imageView  : ImageView
    private lateinit var title      : TextView
    private lateinit var txtdate    : TextView
    private lateinit var txtdesc    : TextView
    private lateinit var txtrate    : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        title       = findViewById(R.id.textJudul)
        imageView   = findViewById(R.id.imgCourse)
        txtdate     = findViewById(R.id.textDate)
        txtdesc     = findViewById(R.id.textDeskripsi)
        txtrate     = findViewById(R.id.textrate)

        val bundle = intent.getBundleExtra("dataPodcast")
        if (bundle != null) {
            Picasso.get().load(bundle.getString("image")).into(imageView)
            title.text      = bundle.getString("title")
            txtdate.text    = bundle.getString("date")+" s.d "
            txtdesc.text    = bundle.getString("description")
            txtrate.text   = "".format(bundle.getString("rate").toString().toFloat())
        }
    }
}
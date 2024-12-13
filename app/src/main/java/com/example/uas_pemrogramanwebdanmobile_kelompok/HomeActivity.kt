package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val txtuser     : TextView  = findViewById(R.id.txtUsername)
        val btnlogout   : TextView  = findViewById(R.id.btnLogout)
        val btnpodcast  : ImageView = findViewById(R.id.menuCourses)
        val btnhelp     : ImageView = findViewById(R.id.menuHelp)

        val sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE)

        val uname = sharedPreferences.getString("Username","")
        if (uname!=""){
            txtuser.text = "Hello, $uname"
        }

        btnpodcast.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        btnhelp.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("l1826@lecturer.ubm.ac.id"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HELP ME")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hai, saya memiliki masalah dari menggunakan aplikasi xxxx ini.")
            try {
                startActivity(Intent.createChooser(emailIntent, "Choose.."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this@HomeActivity, "There is no email client installed", Toast.LENGTH_SHORT).show()
            }
        }

        btnlogout.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}
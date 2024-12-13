package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtlogin        : TextView = findViewById(R.id.txtLogin)
        val editemail       : EditText = findViewById(R.id.editEmail)  // Ganti "editUsername" dengan "editEmail"
        val editfullname    : EditText = findViewById(R.id.editFullname)
        val editpass        : EditText = findViewById(R.id.editPassword)
        val btnregis        : Button   = findViewById(R.id.btnRegister)

        txtlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnregis.setOnClickListener {
            val url: String = AppConfig().ipServer + "/podcastmp3/registrasidata.php"
            val stringRequest = object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    val jsonObj = JSONObject(response)
                    if (jsonObj.getBoolean("message")) {
                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                        // Clear input fields after failed registration
                        editemail.setText("")
                        editfullname.setText("")
                        editpass.setText("")
                        editemail.requestFocus()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("NetworkError", "Error: ${error.message}", error)
                    Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): HashMap<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = editemail.text.toString()  // Ganti "username" dengan "email"
                    params["fullname"] = editfullname.text.toString()
                    params["password"] = editpass.text.toString()  // Enkripsi password dengan md5 sebelum kirim
                    params["level"] = "Member"  // Default level sebagai "Member"
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}

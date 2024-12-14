package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.content.Context
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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtregis        : TextView  = findViewById(R.id.txtSignup)
        val editusername    : EditText  = findViewById(R.id.editUsername)
        val editpass        : EditText  = findViewById(R.id.editPassword)
        val btnlogin        : Button    = findViewById(R.id.btnLogin)

        txtregis.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {
            val url: String = AppConfig().ipServer + "/podcastmp3/logindata.php"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val jsonObj = JSONObject(response)
                    if (jsonObj.getBoolean("message")) {
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                        val sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("Nama", editusername.text.toString())
                        editor.apply()

                        val intent = Intent(this, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        editusername.setText("")
                        editpass.setText("")
                        editusername.requestFocus()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("NetworkError", "Error: ${error.message}", error)
                    Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): HashMap<String, String> {
                    val params = HashMap<String, String>()
                    params["nama"]      = editusername.text.toString()
                    params["password"]  = editpass.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}

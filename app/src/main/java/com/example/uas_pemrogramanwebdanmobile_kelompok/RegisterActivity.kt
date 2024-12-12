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
        val editusername    : EditText = findViewById(R.id.editUsername)
        val editfullname    : EditText = findViewById(R.id.editFullname)
        val editemail       : EditText = findViewById(R.id.editEmail)
        val editpass        : EditText = findViewById(R.id.editPassword)
        val btnregis        : Button   = findViewById(R.id.btnRegister)

        txtlogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnregis.setOnClickListener{
            val url: String = AppConfig().ipServer + "/podcastmp3/registrasidata.php"
            val stringRequest = object : StringRequest(Method.POST,url,
                Response.Listener { response ->
                    val jsonObj = JSONObject(response)
                    if (jsonObj.getBoolean("message")) {
                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "Registrasi Failed", Toast.LENGTH_SHORT).show()
                        editusername.setText("")
                        editfullname.setText("")
                        editemail.setText("")
                        editpass.setText("")
                        editusername.requestFocus()
                    }

                },
                Response.ErrorListener { error ->
                    Log.e("NetworkError", "Error: ${error.message}", error)
                    Toast.makeText(this,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["username"]   = editusername.text.toString()
                    params["fullname"]   = editfullname.text.toString()
                    params["email"]      = editemail.text.toString()
                    params["password"]   = editpass.text.toString()
                    params["level"]      = "Member"
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}
package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.content.Intent
import android.graphics.ColorSpace
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView   : RecyclerView
    private lateinit var searchView     : SearchView
    private var models                  = ArrayList<ColorSpace.Model>()
    private lateinit var adapter        : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        displayData()

        val add: FloatingActionButton   = findViewById(R.id.fab)
        add.visibility  = View.VISIBLE
        add.setOnClickListener {
            val intent  = Intent(this,SendActivity::class,java)
            startActivity(intent)
        }
    }

    private fun displayData() {
        recyclerView                = findViewById(R.id.recyclerView)
        searchView                  = findViewById(R.id.searchView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager  = LinearLayoutManager(this)

        val url: String     = AppConfig().ipServer + "/podcastmp3/view_data.php"
        val stringRequest   = object : StringRequest(Method.POST,url, Response.Listener{response ->
            val jsonObj     = JSONObject("message")) {
            val jsonArray = jsonObj.getJSONArray("data")
            var model: Model
            models.clear()
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                model = Model()
                model.id = item.getString("id")
                model.title = item.getString("title")
                model.image = AppConfig().ipServer + "/course/" + item.getString("image")
                model.desc = item.getString("description")
                model.rate = item.getString("rate")
                models.add(model)
            }
            adapter = MyAdapter(this, models, "Member") // Status tetap diisi, meskipun tidak relevan
            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText!!.lowercase())
                    return true
                }
            })
        } else {
            Toast.makeText(this, "No Courses", Toast.LENGTH_SHORT).show()
        }
        },
            Response.ErrorListener { error ->
                Log.e("NetworkError", "Error: ${error.message}", error)
                Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
            }) {}
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList    = ArrayList<Model>()
            for (i in models) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}
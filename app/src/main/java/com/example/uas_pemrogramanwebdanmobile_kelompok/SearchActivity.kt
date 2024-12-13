package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.content.Intent
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var models = ArrayList<Model>()
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        displayData()

        // Handle FAB button for navigation
        val add: FloatingActionButton = findViewById(R.id.fab)
        add.visibility = View.VISIBLE
        add.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayData() {
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // API URL for fetching data
        val url: String = AppConfig().ipServer + "/podcastmp3/view_data.php"

        // Fetch data using Volley
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val jsonObj = JSONObject(response)
                if (jsonObj.getBoolean("message")) {
                    val jsonArray = jsonObj.getJSONArray("data")
                    models.clear()
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val model = Model().apply {
                            id = item.getString("id")
                            title = item.getString("title")
                            image = AppConfig().ipServer + "/podcastmp3/" + item.getString("image")
                            desc = item.getString("description")
                            rate = item.getString("rate")
                        }
                        models.add(model)
                    }

                    // Set up RecyclerView adapter
                    adapter = MyAdapter(this, models, "Member")
                    recyclerView.adapter = adapter

                    // Set up SearchView listener
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            filterList(newText?.lowercase(Locale.ROOT))
                            return true
                        }
                    })
                } else {
                    Toast.makeText(this, "No Podcast", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("NetworkError", "Error: ${error.message ?: "Unknown error"}", error)
                Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
            }) {}
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun filterList(query: String?) {
        if (query != null) {
            // Filter the list based on query
            val filteredList = models.filter { it.title.lowercase(Locale.ROOT).contains(query) }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(ArrayList(filteredList))
            }
        }
    }
}
package com.example.uas_pemrogramanwebdanmobile_kelompok

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SendActivity : AppCompatActivity() {
    private var id        = ""
    private var resId     = 0
    private lateinit var bitmap     : Bitmap
    private lateinit var imageView  : ImageView
    private lateinit var title      : EditText
    private lateinit var date      : EditText
    private lateinit var desc       : EditText
    private lateinit var rate     : EditText
    private lateinit var save       : Button
    private lateinit var edit       : Button
    private lateinit var delete     : Button
    private val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_send)

        imageView   = findViewById(R.id.fotoCourse)
        title       = findViewById(R.id.editJudul)
        date       = findViewById(R.id.date)
        desc        = findViewById(R.id.editDeskripsi)
        rate       = findViewById(R.id.editRate)
        save        = findViewById(R.id.btnSave)
        edit        = findViewById(R.id.btnUpdate)
        delete      = findViewById(R.id.btnDelete)

        imagePick()

        date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    cal.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(cal.time)
                    date.setText(formattedDate)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        save.setOnClickListener {
            insertData()
        }

        updateData()

        delete.setOnClickListener {
            deleteData()
        }
    }
    private fun imagePick() {
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data!!
                val uri = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    imageView.setImageBitmap(bitmap)
                    resId = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        imageView.setOnClickListener {
            val intent  = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            activityResultLauncher.launch(intent)
        }
    }
    private fun insertData() {
        if (resId == 1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
            val url: String = AppConfig().ipServer + "/course/send_data.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    val jsonObj = JSONObject(response)
                    Toast.makeText(this,jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,CoursesActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    //Clear Data dalam Form
//                    imageView.setImageResource(R.drawable.icupload)
//                    title.setText("")
//                    date.setText("")
//                    desc.setText("")
//                    rate.setText("")
                    resId = 0
                },
                Response.ErrorListener { _ ->
                    Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["title"]          = title.text.toString()
                    params["image"]          = base64Image
                    params["description"]    = desc.text.toString()
                    params["date"]           = date.text.toString()
                    params["rate"]           = rate.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this,"Select the image first",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData() {
        val bundle = intent.getBundleExtra("dataCourse")
        if (bundle != null) {
            id = bundle.getString("id")!!
            Picasso.get().load(bundle.getString("image")).into(imageView)
            title.setText(bundle.getString("title"))
            date.setText(bundle.getString("date"))
            desc.setText(bundle.getString("description"))
            rate.setText(bundle.getString("rate"))
            //visible edit button and hide save button
            save.visibility     = View.GONE
            edit.visibility     = View.VISIBLE
            delete.visibility   = View.VISIBLE

            edit.setOnClickListener{
                if (resId == 1) {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val bytes = byteArrayOutputStream.toByteArray()
                    val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                    val url1: String = AppConfig().ipServer+ "/course/update_dataWithImage.php"
                    val stringRequest = object : StringRequest(Method.POST, url1,
                        Response.Listener { response ->
                            val jsonObj = JSONObject(response)
                            Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                            resId = 0
                            val intent = Intent(this, CoursesActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        },
                        Response.ErrorListener { _ ->
                            Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        override fun getParams(): HashMap<String, String> {
                            val params = HashMap<String, String>()
                            params["id"]            = id
                            params["title"]         = title.text.toString()
                            params["image"]         = base64Image
                            params["date"]          = date.text.toString()
                            params["description"]   = desc.text.toString()
                            params["rate"]          = rate.text.toString()
                            return params
                        }
                    }
                    Volley.newRequestQueue(this).add(stringRequest)
                }
                else {
                    val url2: String = AppConfig().ipServer + "/course/update_data.php"
                    val stringRequest = object : StringRequest(Method.POST,url2,
                        Response.Listener { response ->
                            val jsonObj = JSONObject(response)
                            Toast.makeText(this,jsonObj.getString("message"),Toast.LENGTH_SHORT).show()
                            resId = 0
                            val intent = Intent(this, CoursesActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        },
                        Response.ErrorListener { _ ->
                            Toast.makeText(this,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                        }
                    ){
                        override fun getParams(): HashMap<String,String>{
                            val params = HashMap<String,String>()
                            params["id"]            = id
                            params["title"]         = title.text.toString()
                            params["date"]          = date.text.toString()
                            params["description"]   = desc.text.toString()
                            params["rate"]          = rate.text.toString()
                            return params
                        }
                    }
                    Volley.newRequestQueue(this).add(stringRequest)
                }
            }
        }
    }
    private fun deleteData() {
        MaterialAlertDialogBuilder(this).setTitle("Delete").setMessage("Yakin hapus?")
            .setPositiveButton("Delete"){_,_->
                val url: String = AppConfig().ipServer + "/course/delete_data.php"
                val strReq = object : StringRequest(Method.POST,url, Response.Listener { response ->
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CoursesActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        this.startActivity(intent)
                    }
                    catch (e: JSONException) { e.printStackTrace() } },
                    Response.ErrorListener {}) {
                    override fun getParams(): HashMap<String,String>{
                        val params = HashMap<String,String>()
                        params["id"] = id
                        return params
                    }
                }
                Volley.newRequestQueue(this).add(strReq)
            }
            .setNegativeButton("Cancel"){_,_->}.show()
    }
}
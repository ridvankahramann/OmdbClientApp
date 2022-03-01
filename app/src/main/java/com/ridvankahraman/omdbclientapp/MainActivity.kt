package com.ridvankahraman.omdbclientapp

import android.os.AsyncTask
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search = findViewById<EditText>(R.id.search)
        search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            val url = "https://www.omdbapi.com/?t=${v.text}&apikey="
            AsyncTaskHandleJson().execute(url)
            return@OnEditorActionListener true
        })
    }
    inner class  AsyncTaskHandleJson : AsyncTask<String, String, String>(){
        override fun doInBackground(vararg url: String?): String? {
            val text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use{reader-> reader.readText()} }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }
    private fun handleJson(jsonString: String?){
        val imageView = findViewById<ImageView>(R.id.imageview)
        val imdbpoint = findViewById<TextView>(R.id.imdbpoint)
        val movietitle = findViewById<TextView>(R.id.movietitle)
        val model: Model = Gson().fromJson(jsonString, Model::class.java)
        if (model.Title == null){
            imdbpoint.text = "Sonuç bulunamadı"
        }else{
            Picasso.get().load(model.Poster).resize(256,464).centerCrop().into(imageView)
            imdbpoint.text = model.imdbRating
            movietitle.text = model.Title
        }
    }
}
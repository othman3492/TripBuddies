package com.othman.tripbuddies.controllers.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_city.*

class ChatActivity : AppCompatActivity() {

    private lateinit var city: City
    val COVER_IMAGE_URL =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        city = intent.getSerializableExtra("CHAT_CITY") as City
        configureUI(city)
    }


    @SuppressLint("DefaultLocale")
    private fun configureUI(city: City) {

        val path = COVER_IMAGE_URL + city.coverPicture + "&key=" + BuildConfig.google_apikey


        // Update views
        chat_city_name.text = city.name.toUpperCase()
        Picasso.get().load(path).into(chat_city_picture)


    }
}

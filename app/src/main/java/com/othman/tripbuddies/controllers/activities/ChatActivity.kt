package com.othman.tripbuddies.controllers.activities

import com.othman.tripbuddies.views.MessageAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_city.*

class ChatActivity : AppCompatActivity() {


    private lateinit var cityViewModel: FirestoreCityViewModel
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var city: City
    val COVER_IMAGE_URL =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="



    /*-----------------------------

    USER INTERFACE

    ---------------------------- */




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        city = intent.getSerializableExtra("CHAT_CITY") as City
        configureViewModel()
        configureUI(city)
    }


    @SuppressLint("DefaultLocale")
    private fun configureUI(city: City) {

        configureRecyclerView()
        configureButtons()
        getMessages()

        val path = COVER_IMAGE_URL + city.coverPicture + "&key=" + BuildConfig.google_apikey

        // Update views
        chat_city_name.text = city.name.toUpperCase()
        Picasso.get().load(path).into(chat_city_picture)
    }


    private fun configureRecyclerView() {

        // Configure RecyclerView
        messageAdapter = MessageAdapter(this, FirebaseUserHelper.getCurrentUser()!!.uid)
        chat_recycler_view.adapter = messageAdapter
        chat_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    private fun configureButtons() {

        chat_send_button.setOnClickListener { sendMessage() }
    }



    /*-----------------------------

    USER INTERFACE

    ---------------------------- */



    private fun configureViewModel() {

        cityViewModel = ViewModelProviders.of(this).get(FirestoreCityViewModel::class.java)
    }


    private fun getMessages() {

        cityViewModel.getAllMessagesFromChat(city.cityId).observe(this, androidx.lifecycle.Observer<List<Message>> {

            this.messageAdapter.updateData(it)
        })
    }


    private fun sendMessage() {

        if (chat_message_edit_text.text!!.isNotEmpty()) {

            val message = Message()

            message.userId = FirebaseUserHelper.getCurrentUser()!!.uid
            message.username = FirebaseUserHelper.getCurrentUser()!!.displayName!!
            message.urlProfile = FirebaseUserHelper.getCurrentUser()!!.photoUrl.toString()
            message.cityId = city.cityId
            message.content = chat_message_edit_text.text.toString()

            cityViewModel.createMessage(city.cityId, message).addOnSuccessListener {

                messageAdapter.notifyDataSetChanged()
                chat_message_field.editText!!.text.clear()
            }
        }


    }
}

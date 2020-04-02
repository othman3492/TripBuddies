package com.othman.tripbuddies.controllers.activities

import com.othman.tripbuddies.views.MessageAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils.convertDateAndTime
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreMessageViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {


    private lateinit var cityViewModel: FirestoreCityViewModel
    private lateinit var messageViewModel: FirestoreMessageViewModel
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var city: City
    private val coverImageURL =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="


    /*-----------------------------

    USER INTERFACE

    ----------------------------*/


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
        getMessages(city)

        val path = coverImageURL + city.coverPicture + "&key=" + BuildConfig.google_apikey

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

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModel() {

        cityViewModel = ViewModelProvider(this).get(FirestoreCityViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(FirestoreMessageViewModel::class.java)
    }


    // Get message list from city chat
    private fun getMessages(city: City) {

        cityViewModel.getCity(city.cityId).observe(this, Observer {

            val list: MutableList<Message> = ArrayList()

            for (doc in it.messagesList) {

                messageViewModel.getMessage(doc).observe(this, Observer { message ->
                    if (it != null && !list.contains(message)) {
                        list.add(message)
                        list.sortBy { message.timestamp }
                        messageAdapter.updateData(list)
                    }
                })
            }
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
            message.timestamp = convertDateAndTime(Date())

            messageViewModel.createMessage(message).addOnSuccessListener {

                cityViewModel.addMessageToChat(city.cityId, message.messageId)
                    .addOnSuccessListener {

                        // Empty message field
                        chat_message_field.editText!!.text.clear()
                    }
            }

        }
    }
}

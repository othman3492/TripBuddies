package com.othman.tripbuddies.controllers.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.utils.Utils
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.activity_add_edit.*
import java.util.*

class AddEditActivity : AppCompatActivity() {

    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel

    private var trip = Trip()


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        configureUI()
    }


    private fun configureUI() {


        configureViewModels()
        configureButtons()
        configureDatePicker(depart_date)
        configureDatePicker(return_date)

        // Get trip if it's EditActivity
        if (intent.getSerializableExtra("TRIP_TO_EDIT") != null) {
            trip = intent.getSerializableExtra("TRIP_TO_EDIT") as Trip
            fillData()
        }
    }


    private fun configureButtons() {

        create_button.setOnClickListener { createTrip(trip) }
        edit_button.setOnClickListener { updateTrip(trip) }
    }



    // Show/hide views depending on Add/Edit layout
    private fun fillData() {

        // Set views' visibility
        add_trip.text = getString(R.string.edit_a_trip)
        create_button.visibility = View.INVISIBLE
        edit_button.visibility = View.VISIBLE


        // Fill input texts
        name_text_input.setText(trip.name)
        description_text_input.setText(trip.description)

        // Set dates
        depart_date.text = trip.departDate
        return_date.text = trip.returnDate
    }


    // Apply data to Trip object
    private fun getDataFromInput(trip: Trip) {


        trip.name = name_text_input.text.toString()
        trip.description = description_text_input.text.toString()
        trip.userId = FirebaseUserHelper.getCurrentUser()!!.uid
        trip.departDate = depart_date.text.toString()
        trip.returnDate = return_date.text.toString()
        trip.username = FirebaseUserHelper.getCurrentUser()!!.displayName.toString()

    }


    private fun configureDatePicker(textView: TextView) {

        val calendar = Calendar.getInstance()

        // Create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                textView.text = Utils.convertDate(calendar.time)

            }

        // Open DatePickerDialog when clicked
        textView.setOnClickListener {
            DatePickerDialog(
                this@AddEditActivity, dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }


    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModels() {

        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
    }



    // Create Trip object from data and store it into database
    private fun createTrip(newTrip: Trip) {

        getDataFromInput(newTrip)

        if (newTrip.name != "") {

            // Set trip creation date
            newTrip.creationDate = Utils.convertDate(Date())

            // Create object
            tripViewModel.createTripIntoFirestore(newTrip).addOnSuccessListener {
                // Add trip to user trip list
                userViewModel.addTripToUser(trip.userId, trip.tripId).addOnSuccessListener {
                    // Add user to trip buddies list
                    tripViewModel.addBuddyToTrip(trip.tripId, trip.userId).addOnSuccessListener {

                        // Confirm creation
                        Toast.makeText(this, "New trip created !", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Return to MainActivity and pass it Trip via intent to display TripFragment
            val tripIntent = Intent(this, MainActivity::class.java)
            tripIntent.putExtra("TRIP", newTrip)
            startActivity(tripIntent)

        } else {

            Toast.makeText(this, "Give a name to your trip !", Toast.LENGTH_SHORT).show()
        }
    }


    // Update Trip object from data and store it into database
    private fun updateTrip(tripUpdate: Trip) {

        getDataFromInput(tripUpdate)

        if (tripUpdate.name != "") {

            // Update object
            tripViewModel.updateTripIntoFirestore(tripUpdate)

            // Confirm creation
            Toast.makeText(this, "Trip updated !", Toast.LENGTH_SHORT).show()

            // Return to MainActivity and pass it Trip via intent to display TripFragment
            finish()

        } else {

            Toast.makeText(this, "Give a name to your trip !", Toast.LENGTH_SHORT).show()
        }
    }


}

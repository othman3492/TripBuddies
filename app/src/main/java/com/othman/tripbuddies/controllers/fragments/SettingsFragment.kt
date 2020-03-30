package com.othman.tripbuddies.controllers.fragments

import UserSearchAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.MainActivity
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_user_search.*
import java.util.*
import kotlin.collections.ArrayList


class SettingsFragment() : DialogFragment() {


    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel
    private lateinit var tripViewModel: FirestoreTripViewModel






    /*-----------------------------
    USER INTERFACE
    ---------------------------- */



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModels()
        configureButtons()
    }


    private fun configureButtons() {

        userViewModel.getUser(FirebaseUserHelper.getCurrentUser()!!.uid).observe(viewLifecycleOwner, Observer {

            // Set switch initial state
            if (it.displayEmail) email_settings_switch.isChecked = true

            // Configure switch
            email_settings_switch.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    it.displayEmail = true
                    userViewModel.updateUserIntoFirestore(it)
                } else {
                    it.displayEmail = false
                    userViewModel.updateUserIntoFirestore(it)
                }
            }

            // Configure delete account button
            delete_account_button.setOnClickListener { _ ->
                AlertDialog.Builder(activity)
                    .setMessage(R.string.delete_account_confirmation)
                    .setPositiveButton(R.string.yes) { _, _ -> deleteUser(it) }
                    .setNegativeButton(R.string.no, null)
                    .show()
            }
        })
    }






    /*-----------------------------
    DATA QUERIES
    ---------------------------- */


    private fun configureViewModels() {

        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProvider(this).get(FirestoreCityViewModel::class.java)
        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
    }


    private fun deleteUser(user: User) {

        // Remove user from cities
        for (doc in user.wishList) cityViewModel.removeUserFromWishList(doc, user.userId)

        // Remove user from trips
        for (doc in user.tripList) tripViewModel.removeBuddyFromTrip(doc, user.userId)

        // Delete user
        userViewModel.deleteUserFromFirestore(user.userId).addOnSuccessListener {
            activity!!.finish()
            Toast.makeText(activity, "Account deleted !", Toast.LENGTH_SHORT).show()
        }
    }






}
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
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_user_search.*
import java.util.*
import kotlin.collections.ArrayList


class AccountSettingsFragment(val userId: String) : DialogFragment() {


    private lateinit var userViewModel: FirestoreUserViewModel


    private var onDismissListener: DialogInterface.OnDismissListener? = null


    // Create a custom OnDismissListener to handle imageList
    interface OnDismissListener {

        fun dismissed()
    }



    /*-----------------------------

    USER INTERFACE

    ---------------------------- */



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModels()
        configureButtons()
    }


    private fun configureButtons() {

        logout_button.setOnClickListener { AuthUI.getInstance()
            .signOut(activity!!).addOnSuccessListener { activity!!.finish() } }

        delete_account_button.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage(R.string.delete_account_confirmation)
                .setPositiveButton(R.string.yes) { _, _ -> deleteUser(userId) }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }






    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModels() {

        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
    }


    private fun deleteUser(userId: String) {

        userViewModel.deleteUserFromFirestore(userId).addOnSuccessListener { activity!!.finish() }
    }






}

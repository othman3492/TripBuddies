package com.othman.tripbuddies.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.othman.tripbuddies.R
import com.othman.tripbuddies.data.model.User
import com.othman.tripbuddies.viewmodel.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodel.FirestoreUserViewModel
import com.othman.tripbuddies.adapters.UserSearchAdapter
import kotlinx.android.synthetic.main.fragment_user_search.*
import java.util.*
import kotlin.collections.ArrayList


class UserSearchFragment : DialogFragment() {


    private lateinit var userSearchAdapter: UserSearchAdapter
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel

    private lateinit var tripId: String
    private var usersList: List<User> = ArrayList()


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModels()
        configureRecyclerView()
        getUsersList()
        configureSearch()

        tripId = this.arguments!!.getString("TRIP_BUNDLE")!!

    }


    private fun configureRecyclerView() {

        // Configure RecyclerView
        userSearchAdapter = UserSearchAdapter(requireContext()) { user: User -> addBuddyToTrip(user) }
        user_search_recycler_view.adapter = userSearchAdapter
        user_search_recycler_view.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        user_search_recycler_view.addItemDecoration(
            DividerItemDecoration(
                user_search_recycler_view.context, DividerItemDecoration.HORIZONTAL
            )
        )
    }


    // Configure TextWatcher to update results on search
    private fun configureSearch() {

        user_search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                filterSearch((p0.toString()))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }


    // Add objects to filtered list depending on input
    private fun filterSearch(text: String) {

        val filteredList: MutableList<User> = ArrayList()

        for (user in usersList) {
            if (user.name.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(user)
            }
        }

        userSearchAdapter.updateData(filteredList)
    }



    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModels() {

        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
    }


    private fun getUsersList() {

        userViewModel.getAllUsers().observe(viewLifecycleOwner, Observer {

            usersList = it
            userSearchAdapter.updateData(it)
        })
    }


    private fun addBuddyToTrip(user: User) {

        // Add user to trip's buddies list
        tripViewModel.addBuddyToTrip(tripId, user.userId).addOnSuccessListener {
            // Add trip to user's trip list
            userViewModel.addTripToUser(user.userId, tripId)
        }

        dismiss()
    }





}

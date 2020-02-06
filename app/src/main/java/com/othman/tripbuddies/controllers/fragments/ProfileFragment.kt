package com.othman.tripbuddies.controllers.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.AddEditActivity
import com.othman.tripbuddies.controllers.activities.MainActivity
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var profileUser = User()
    private lateinit var userViewModel: FirestoreUserViewModel


    companion object {
        fun newInstance(userId: String): ProfileFragment {

            val args = Bundle()
            args.putString("USER_ID", userId)

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModel()
        configureUI()
    }


    private fun configureUI() {

        // Retrieve user data
        userViewModel.getUser(arguments!!.getString("USER_ID")!!).observe(viewLifecycleOwner, Observer {


            // Fill user data into views
            username.text = it.name
            user_presentation.text = it.presentation
            setProfilePicture(it)
            setCoverPicture(it)

            configureFloatingButton(it)

            profileUser = it
        })
    }


    @SuppressLint("RestrictedApi")
    private fun configureFloatingButton(user: User) {


        if (user.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

            add_floating_action_button.setOnClickListener {

                val userIntent = Intent(activity, AddEditActivity::class.java)
                userIntent.putExtra("USER", profileUser)
                startActivity(userIntent)
            }
        } else {

            add_floating_action_button.visibility = View.GONE
            chat_floating_action_button.visibility = View.VISIBLE

            chat_floating_action_button.setOnClickListener { // display chat fragment //
                 }

    }

}


private fun configureRecyclerView() {}

private fun configureViewModel() {

    userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
}


// Load profile picture into view
private fun setProfilePicture(user: User) {

    Glide.with(this)
        .load(user.urlPicture)
        .apply(RequestOptions.circleCropTransform())
        .into(profile_picture)
}

// Load cover picture into view if not null
private fun setCoverPicture(user: User) {

    if (user.urlCoverPicture != null) {

        Glide.with(this)
            .load(user.urlCoverPicture)
            .into(cover_picture)
    }

}


}

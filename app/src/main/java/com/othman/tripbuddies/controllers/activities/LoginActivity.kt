package com.othman.tripbuddies.controllers.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private lateinit var userViewModel: FirestoreUserViewModel


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        configureViewModel()
        setSignInButtons()

        /*FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d("TOKEN", token!!)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })*/

    }


    // Configure sign-in buttons
    private fun setSignInButtons() {

        facebook_button.setOnClickListener { startFacebookSignInActivity() }
        google_button.setOnClickListener { startGoogleSignInActivity() }
    }


    // Launch Facebook sign-in activity
    private fun startFacebookSignInActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    Collections.singletonList(
                        AuthUI.IdpConfig.FacebookBuilder().build()
                    )
                )
                .setIsSmartLockEnabled(false, true)
                .build(), RC_SIGN_IN
        )
    }

    // Launch Google sign-in activity
    private fun startGoogleSignInActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    Collections.singletonList(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .setIsSmartLockEnabled(false, true)
                .build(), RC_SIGN_IN
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleResponseAfterSignIn(requestCode, resultCode, data)
    }


    // Create snackbar to display error message
    private fun showSnackbar(constraintLayout: ConstraintLayout, message: String) {

        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show()
    }


    // Show message depending on result from sign-in
    private fun handleResponseAfterSignIn(requestCode: Int, resultCode: Int, data: Intent?) {

        val response: IdpResponse = IdpResponse.fromResultIntent(data)!!

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {

                // Show message, create user and start MainActivity
                showSnackbar(login_constraint_layout, getString(R.string.connection_succeed))
                createUserInFirestore()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                when (response.error!!.errorCode) {
                    ErrorCodes.NO_NETWORK -> {
                        showSnackbar(login_constraint_layout, getString(R.string.error_no_internet))
                    }
                    ErrorCodes.UNKNOWN_ERROR -> {
                        showSnackbar(
                            login_constraint_layout,
                            getString(R.string.error_unknown_error)
                        )
                    }
                }
            }
        }
    }



    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    // Create user in Firestore if it doesn't exist and store data
    private fun createUserInFirestore() {

        val uid: String = FirebaseUserHelper.getCurrentUser()!!.uid
        val urlPicture: String? = if (FirebaseUserHelper.getCurrentUser()!!.photoUrl != null)
            FirebaseUserHelper.getCurrentUser()!!.photoUrl.toString() else null
        val username: String? = FirebaseUserHelper.getCurrentUser()!!.displayName

        val liveData: LiveData<User> = userViewModel.getUser(FirebaseAuth.getInstance().currentUser!!.uid)

        liveData.observe(this, androidx.lifecycle.Observer<User> {
                if (liveData.hasActiveObservers())
                    liveData.removeObservers(this)

                if (it == null) {
                    userViewModel.createUserIntoFirestore(User(uid, username!!, urlPicture))
                }
            })
    }

    private fun configureViewModel() {

        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
    }


}


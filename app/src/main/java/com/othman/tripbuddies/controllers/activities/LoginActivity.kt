package com.othman.tripbuddies.controllers.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils
import com.othman.tripbuddies.utils.Utils.generateId
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private lateinit var userViewModel: FirestoreUserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        configureViewModel()
        setSignInButtons()

    }


    // Configure sign-in buttons
    private fun setSignInButtons() {

        email_button.setOnClickListener { startEmailSignInActivity() }
        facebook_button.setOnClickListener { startFacebookSignInActivity() }
        google_button.setOnClickListener { startGoogleSignInActivity() }
    }


    // Launch email sign-in activity
    private fun startEmailSignInActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    Collections.singletonList(
                        EmailBuilder().build()
                    )
                )
                .setIsSmartLockEnabled(false, true)
                .build(), RC_SIGN_IN
        )
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


    // Create user in Firestore if it doesn't exist and store data
    private fun createUserInFirestore() {

        val urlPicture: String? = if (FirebaseUserHelper.getCurrentUser()!!.photoUrl != null)
            FirebaseUserHelper.getCurrentUser()!!.photoUrl.toString() else null
        val username: String? = FirebaseUserHelper.getCurrentUser()!!.displayName

        userViewModel.getUser(FirebaseAuth.getInstance().currentUser!!.uid).observe(this,
            androidx.lifecycle.Observer<User> {

                if (it == null)
                    userViewModel.createUserIntoFirestore(
                        User(
                            username!!,
                            "",
                            urlPicture
                        )
                    )
            })
    }

    private fun configureViewModel() {

        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
    }
}


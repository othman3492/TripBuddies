package com.othman.tripbuddies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }


    // Configure sign-in buttons
    @OnClick(R.id.email_button)
    fun onClickEmailButton() {
        startEmailSignInActivity()
    }

    @OnClick(R.id.facebook_button)
    fun onClickFacebookButton() {
        startFacebookSignInActivity()
    }

    @OnClick(R.id.google_button)
    fun onClickGoogleButton() {
        startGoogleSignInActivity()
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
            }  else {
                when {
                    Objects.requireNonNull(response.error)!!.errorCode == ErrorCodes.NO_NETWORK -> {
                        showSnackbar(login_constraint_layout, getString(R.string.error_no_internet));
                    }
                    response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR -> {
                        showSnackbar(login_constraint_layout, getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }


    // Create user in Firestore if it doesn't exist and store data
    private fun createUserInFirestore() {

        val urlPicture: String? = if (FirebaseAuth.getInstance().currentUser!!.photoUrl != null)
                Objects.requireNonNull(FirebaseAuth.getInstance().currentUser!!.photoUrl).toString() else null
        val username: String? = FirebaseAuth.getInstance().currentUser!!.displayName
        val userId: String? = FirebaseAuth.getInstance().currentUser!!.uid

        


    }
}

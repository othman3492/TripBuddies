package com.othman.tripbuddies

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.facebook.FacebookSdk.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.othman.tripbuddies.utils.Connection

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkInfo
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConnectionTest {
    private var connectivityManager: ConnectivityManager? = null
    private var shadowOfActiveNetworkInfo: ShadowNetworkInfo? = null
    @Before
    fun setUp() {
        connectivityManager =
            getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        shadowOfActiveNetworkInfo =
            shadowOf(connectivityManager!!.activeNetworkInfo)
    }

    @get:Test
    val activeNetworkInfoshouldInitializeItself: Unit
        get() {
            assertThat(connectivityManager!!.activeNetworkInfo).isNotNull()
        }

    @get:Test
    val activeNetworkInfoshouldReturnTrueCorrectly: Unit
        get() {
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTED)
            assertThat(Connection.isInternetAvailable(getApplicationContext())).isTrue()
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTING)
            assertThat(Connection.isInternetAvailable(getApplicationContext())).isFalse()
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
            assertThat(Connection.isInternetAvailable(getApplicationContext())).isFalse()
        }
}
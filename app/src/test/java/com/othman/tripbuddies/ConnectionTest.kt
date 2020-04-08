package com.othman.tripbuddies

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.othman.tripbuddies.utils.Connection
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkInfo

@RunWith(AndroidJUnit4::class)
class ConnectionTest {
    private var connectivityManager: ConnectivityManager? = null
    private var shadowOfActiveNetworkInfo: ShadowNetworkInfo? = null

    @Before
    fun setUp() {

        val context = getApplicationContext<Context>()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        shadowOfActiveNetworkInfo =
            shadowOf(connectivityManager!!.activeNetworkInfo)
    }

    @Test
    fun getActiveNetworkInfo_shouldInitializeItself() {
        assertThat(connectivityManager!!.activeNetworkInfo).isNotNull()
    }

    @Test
    fun activeNetworkInfoshouldReturnTrueCorrectly() {
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTED)
            assertThat(Connection.checkNetworkState(getApplicationContext())).isTrue()
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTING)
            assertThat(Connection.checkNetworkState(getApplicationContext())).isFalse()
            shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
            assertThat(Connection.checkNetworkState(getApplicationContext())).isFalse()
        }
}
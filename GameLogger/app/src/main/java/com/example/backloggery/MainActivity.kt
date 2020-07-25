package com.example.backloggery

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    // activity_main.xml hosts the navigation fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    // Check if the device is online
    fun isOnline(): Boolean {
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        return if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) { /*
                 * Toast.makeText(getActivity(), "No Internet connection!",
                 * Toast.LENGTH_LONG).show();
                 */
            false
        } else true
    }


}

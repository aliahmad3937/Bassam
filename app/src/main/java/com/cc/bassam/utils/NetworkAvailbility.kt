package com.cc.bassam.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.isNetworkConnected(): Boolean {
    var cm =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
}

fun Context.showToast(msg:String){
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}

package com.learn.androidplayground.webviewjsbridge

import android.app.Activity
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.ProgressBar
import com.learn.androidplayground.R

class JSBridge(private val activity: Activity) {

    @JavascriptInterface
    fun showLoading() {
        activity.runOnUiThread {
            val progressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
        }
    }

    @JavascriptInterface
    fun hideLoading() {
        activity.runOnUiThread {
            val progressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.GONE
        }
    }
}
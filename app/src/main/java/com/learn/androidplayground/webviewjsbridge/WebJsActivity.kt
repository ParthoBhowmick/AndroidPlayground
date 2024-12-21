package com.learn.androidplayground.webviewjsbridge

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.learn.androidplayground.R

class WebJsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_js)

        val webView: WebView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSBridge(this), "AndroidBridge")

        // Set WebViewClient to keep navigation inside WebView
        webView.webViewClient = object : WebViewClient() {}

        // Load HTML from raw folder
        val inputStream = resources.openRawResource(R.raw.my_web_file)
        val htmlContent = inputStream.bufferedReader().use { it.readText() }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
}
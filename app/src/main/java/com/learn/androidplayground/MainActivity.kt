package com.learn.androidplayground

import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerRecyclerView: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val etTest = findViewById<EditText>(R.id.etTest)
        Handler(Looper.getMainLooper()).postDelayed({
            etTest.setText("bla bla")
        }, 10000)
        //initView()
    }

    private fun initView() {
        viewPagerRecyclerView = findViewById(R.id.viewPagerRecyclerView)
        viewPagerRecyclerView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= M) {
                val rsaKeyPair = generateRsaKeyPair()
                // Log.e("Public key", Base64.encodeToString(rsaKeyPair.public.encoded, Base64.NO_WRAP))
                Log.e(
                    "private key",
                    Base64.encodeToString(rsaKeyPair.private.encoded, Base64.NO_WRAP)
                )
            }
        }
    }

    @RequiresApi(M)
    fun generateRsaKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        generator.initialize(2048, SecureRandom())
        return generator.genKeyPair()
    }
}
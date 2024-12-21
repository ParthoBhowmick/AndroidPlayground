package com.learn.androidplayground.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.learn.androidplayground.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestCoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_coroutine)
        GlobalScope.launch {
            val answer1 = async { fakeNetworkCall(3000) }
            val answer2 = async { fakeNetworkCall(7000) }
            Log.e("ans 1", answer1.await() )
            Log.e("ans 2", answer2.await() )
        }
    }

    private suspend fun fakeNetworkCall(waitingTime: Long): String {
        delay(waitingTime)
        return "result after ${waitingTime / 1000} second"
    }
}
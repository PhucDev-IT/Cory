package com.developer.cory.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.developer.cory.MainActivity
import com.developer.cory.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                nextActivity()
            }
        }, 2000) // đợi 1 giây trước khi chạy Runnable
    }

    private fun nextActivity(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
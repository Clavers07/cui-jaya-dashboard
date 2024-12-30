package com.example.database

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.inappmessaging.MessagesProto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val start: Button = findViewById(R.id.start)

        start.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // Delay 2 detik
                val intent = Intent(this@MainSplash, MainActivity::class.java)
                startActivity(intent)
                finish() // Menutup SplashActivity agar tidak kembali saat back
            }
        }

    }
}
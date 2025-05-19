package com.example.konsinyasi

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.inappmessaging.MessagesProto
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
        val version: TextView = findViewById(R.id.version)

        version.text = "Versi: " + getVersionName()

        start.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // Delay 2 detik
                val intent = Intent(this@MainSplash, MainActivity::class.java)
                startActivity(intent)
                finish() // Menutup SplashActivity agar tidak kembali saat back
            }
        }

    }
    // Fungsi untuk mendapatkan versionName
    private fun getVersionName(): String {
        return try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName.toString()
        } catch (e: Exception) {
            "Unknown"
        }
    }

    // Fungsi untuk mendapatkan versionCode
    private fun getVersionCode(): Int {
        return try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionCode
        } catch (e: Exception) {
            -1
        }
    }
}
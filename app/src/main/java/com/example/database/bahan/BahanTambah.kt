package com.example.database.bahan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.database.DBHelper
import com.example.database.databinding.ActivityBahanTambahBinding

class BahanTambah : AppCompatActivity() {

    private lateinit var binding: ActivityBahanTambahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup View Binding
        binding = ActivityBahanTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }


        binding.simpan.setOnClickListener {
            val db = DBHelper(this, null)

            // Mengambil nilai dari EditText
            val nameValue = binding.namaBahan.text.toString().trim()
            val hargaValue = binding.harga.text.toString()
            var num : Int
            try {
                num = hargaValue.toInt()
            } catch (e: NumberFormatException) {
                num = 0 // Handle invalid input
            }

            // Cek apakah input valid
            if (nameValue.isNotEmpty() && num != null) {

                // Menambahkan data ke database
                val res = db.addBahan(nameValue, hargaValue)

                // Menampilkan pesan Toast
                if (res) {
                    Toast.makeText(this, "$nameValue added to database", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed to add $nameValue to database", Toast.LENGTH_LONG).show()
                }
//                // Membersihkan EditText
//                binding.enterName.text.clear()
//                binding.enterAge.text.clear()
            } else {
                Toast.makeText(this, "Please enter both name and age", Toast.LENGTH_LONG).show()
            }

            Intent(this, ActivityBahan::class.java).also {
                startActivity(it)
                finish()
            }

        }

    }
}
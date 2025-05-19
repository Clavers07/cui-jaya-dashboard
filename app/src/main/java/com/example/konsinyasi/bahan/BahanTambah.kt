package com.example.konsinyasi.bahan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.konsinyasi.Formatter
import com.example.konsinyasi.utils.DBHelper
import com.example.konsinyasi.databinding.ActivityBahanTambahBinding

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
        binding.kembali.setOnClickListener {
            finish()
        }

        binding.simpan.setOnClickListener {
            val db = DBHelper(this, null)

            // Mengambil nilai dari EditText
            val nameValue = binding.namaBahan.text.toString().trim()
            val hargaValue = Formatter.onlyInt(binding.harga.text.toString())

            // Cek apakah input valid
            if (nameValue.isNotEmpty() && hargaValue >= 0) {

                // Menambahkan data ke database
                val res = db.addBahan(nameValue, hargaValue)

                // Menampilkan pesan Toast
                if (res) {
                    Toast.makeText(this, "Berhasil menambahkan pengeluaran $nameValue", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Gagal menambahkan pengeluaran $nameValue", Toast.LENGTH_LONG).show()
                }
//                // Membersihkan EditText
//                binding.enterName.text.clear()
//                binding.enterAge.text.clear()
            } else {
                Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_LONG).show()
            }

            finish()

        }

    }
}
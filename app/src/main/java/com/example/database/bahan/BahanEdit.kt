package com.example.database.bahan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import com.example.database.Formatter
import com.example.database.services.DBHelper
import com.example.database.databinding.ActivityBahanEditBinding

class BahanEdit : AppCompatActivity() {

    private lateinit var binding: ActivityBahanEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBahanEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.kembali.setOnClickListener {
            finish()
        }

        binding.back.setOnClickListener {
            finish()
        }


        binding.root.doOnPreDraw { // Use post to run on the UI thread after layout is inflated
            binding.id.setText(intent.getStringExtra("ID"))
            binding.id.isEnabled = false
//            Toast.makeText(this, intent.getIntExtra("ID", 0).toString(), Toast.LENGTH_LONG).show()
            binding.namaBahan.setText(intent.getStringExtra("NAMA_BAHAN"))
            binding.harga.setText(Formatter.toCurrency(intent.getIntExtra("HARGA", 0)))
        }

        binding.ubah.setOnClickListener {

            val db = DBHelper(this, null)
            val id = binding.id.text.toString().toInt()
            val namaBahan = binding.namaBahan.text.toString()
            val harga = Formatter.onlyInt(binding.harga.text.toString())

            // Cek apakah nama kosong
            if (namaBahan.isNotEmpty() && harga >= 0) {
                // Coba untuk menghapus data berdasarkan nama
                val updated = db.updateBahan(id, namaBahan, harga)

                if (updated > 0) {
                    // Menampilkan pesan sukses
                    Toast.makeText(this, "Perbarui pengeluaran $id", Toast.LENGTH_LONG).show()

                    // Pindah halaman
                    finish()
                } else {
                    // Menampilkan pesan error jika nama tidak ditemukan
                    Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Masukkan data dengan benar!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
package com.example.database.sumber

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.DBHelper
import com.example.database.R
import com.example.database.databinding.ActivitySumberBinding
import com.example.database.model.Pemasukan
import com.example.database.model.Sumber
import com.example.database.model.SumberJoinTotal

class ActivitySumber : AppCompatActivity() {

    private lateinit var binding: ActivitySumberBinding

    private lateinit var sumber: MutableList<SumberJoinTotal>
    private lateinit var sumberAdapter: SumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup View Binding
        binding = ActivitySumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.kembali.setOnClickListener {
            finish()
        }

        binding.tambah.setOnClickListener {
            Intent(this, SumberDetail::class.java).also {
                startActivity(it)
            }
        }

        sumber = mutableListOf()

        fun show() {
            val db = DBHelper(this, null)

            // Mendapatkan semua data nama dari database
            val cursor = db.getSumber()
            val size = cursor

            // Memeriksa apakah cursor tidak null
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Mendapatkan kolom berdasarkan nama yang benar
                    val idColumnIndex = cursor.getColumnIndexOrThrow("id")
                    val namaColumnIndex = cursor.getColumnIndexOrThrow("nama")
                    val alamatColumnIndex = cursor.getColumnIndexOrThrow("alamat")
                    val keteranganColumnIndex = cursor.getColumnIndexOrThrow("keterangan")
                    val tanggalColumnIndex = cursor.getColumnIndexOrThrow("tanggal")
                    val totalColumnIndex = cursor.getColumnIndexOrThrow("total")

                    val data: SumberJoinTotal = SumberJoinTotal(cursor.getInt(idColumnIndex), cursor.getString(namaColumnIndex), cursor.getString(alamatColumnIndex), cursor.getString(keteranganColumnIndex), cursor.getString(tanggalColumnIndex), cursor.getInt(totalColumnIndex))
                    // Menambahkan data ke TextView
                    sumber.add(data)
                } while (cursor.moveToNext())

                // Tutup cursor setelah digunakan
                cursor.close()
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
            }
        }
        show()

        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        sumberAdapter = SumberAdapter(sumber.toList())
        recyclerView.adapter = sumberAdapter
    }
}
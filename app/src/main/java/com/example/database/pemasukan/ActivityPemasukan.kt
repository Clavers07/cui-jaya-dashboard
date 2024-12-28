package com.example.database.pemasukan

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
import com.example.database.databinding.ActivityPemasukanBinding
import com.example.database.model.Pemasukan
import com.example.database.sumber.ActivitySumber

class ActivityPemasukan : AppCompatActivity() {

    private lateinit var binding: ActivityPemasukanBinding

    private lateinit var pemasukan: MutableList<Pemasukan>
    private lateinit var pemasukanAdapter: PemasukanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /// Set Up Init
        // Setup View Binding
        binding = ActivityPemasukanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kembali.setOnClickListener {
            finish()
        }

        // Setup Lists form of data
        pemasukan = mutableListOf()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tambah.setOnClickListener {
            val intent = Intent(this, ActivitySumber::class.java)
            startActivity(intent)
        }

        // Getting the lists & assigning it to the adapter
        getLists()
        assignToAdapter()


    }

    override fun onResume() {
        super.onResume()

        pemasukan.removeAll(pemasukan)
        getLists()
        assignToAdapter()
    }

    fun getLists() {
        val db = DBHelper(this, null)

        // Mendapatkan semua data nama dari database
        val cursor = db.getPemasukan()
        val size = cursor

        // Memeriksa apakah cursor tidak null
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Mendapatkan kolom berdasarkan nama yang benar
                val idColumnIndex = cursor.getColumnIndexOrThrow("id")
                val id_SumberColumnIndex = cursor.getColumnIndexOrThrow("id_sumber")
                val namaColumnIndex = cursor.getColumnIndexOrThrow("nama")
                val nilaiColumnIndex = cursor.getColumnIndexOrThrow("nilai")
                val catatanColumnIndex = cursor.getColumnIndexOrThrow("catatan")
                val tanggalColumnIndex = cursor.getColumnIndexOrThrow("tanggal")

                val id = cursor.getInt(idColumnIndex)
                val id_sumber = cursor.getInt(id_SumberColumnIndex)
                val nama = cursor.getString(namaColumnIndex)
                val nilai = cursor.getInt(nilaiColumnIndex)
                val keterangan = cursor.getString(catatanColumnIndex)
                val tanggal = cursor.getString(tanggalColumnIndex)

                val data: Pemasukan = Pemasukan(id, id_sumber, nama, nilai, keterangan, tanggal)
                // Menambahkan data ke TextView
                pemasukan.add(data)
            } while (cursor.moveToNext())

            // Tutup cursor setelah digunakan
            cursor.close()
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
        }
    }

    fun assignToAdapter() {
        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        pemasukanAdapter = PemasukanAdapter(pemasukan)
        recyclerView.adapter = pemasukanAdapter
    }
}
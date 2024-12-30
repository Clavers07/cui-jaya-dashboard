package com.example.database

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.bahan.ActivityBahan
import com.example.database.databinding.ActivityMainBinding
import com.example.database.model.SumberJoinTotal
import com.example.database.pemasukan.ActivityPemasukan
import com.example.database.services.DBHelper
import com.example.database.sumber.ActivitySumber
import com.example.database.sumber.SumberAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sumber: MutableList<SumberJoinTotal>
    private lateinit var sumberAdapter: SumberAdapter

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this, null)
        sumber = mutableListOf()

        getDataList()
        setData()

        binding.pemasukan.setOnClickListener {
            val intent = Intent(this, ActivityPemasukan::class.java)
            startActivity(intent)
        }

        binding.lihatSumber.setOnClickListener {
            val intent = Intent(this, ActivitySumber::class.java)
            startActivity(intent)
        }

        binding.pengeluaran.setOnClickListener {
            val intent = Intent(this, ActivityBahan::class.java)
            startActivity(intent)
        }

        binding.info.setOnClickListener {
            Intent(this, InfoActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.bantuan.setOnClickListener {
            Intent(this, TutorialActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        sumber.removeAll(sumber)
        getDataList()
        setData()
    }

    fun getDataList() {
        val db = DBHelper(this, null)

        // Mendapatkan semua data nama dari database
        val cursor = db.getSumberToday()

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

//                Toast.makeText(this, "Data ditemukan", Toast.LENGTH_LONG).show()

                val data: SumberJoinTotal = SumberJoinTotal(cursor.getInt(idColumnIndex), cursor.getString(namaColumnIndex), cursor.getString(alamatColumnIndex), cursor.getString(keteranganColumnIndex), cursor.getString(tanggalColumnIndex), cursor.getInt(totalColumnIndex))
                // Menambahkan data ke TextView
                sumber.add(data)
            } while (cursor.moveToNext())

            // Tutup cursor setelah digunakan
            cursor.close()
        } else {
//            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
        }
    }

    fun setData() {
        binding.totalPemasukan.text = "+" + Formatter.toCurrency(db.getSumPemasukan())
        binding.totalPengeluaran.text = "-" + Formatter.toCurrency(db.getSumBahan())

        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        sumberAdapter = SumberAdapter(sumber.toList(), true)
        recyclerView.adapter = sumberAdapter
    }
}
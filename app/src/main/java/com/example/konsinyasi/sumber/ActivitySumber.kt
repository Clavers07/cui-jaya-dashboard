package com.example.konsinyasi.sumber

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konsinyasi.Formatter
import com.example.konsinyasi.utils.DBHelper
import com.example.konsinyasi.R
import com.example.konsinyasi.databinding.ActivitySumberBinding
import com.example.konsinyasi.model.SumberJoinTotal
import com.example.konsinyasi.utils.PdfGenerator

class ActivitySumber : AppCompatActivity() {

    private lateinit var binding: ActivitySumberBinding

    var sum: Int = 0

    private lateinit var mitra: MutableList<SumberJoinTotal>
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

        // Set status bar and navigation bar colors
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.main_bg)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        binding.kembali.setOnClickListener {
            finish()
        }

        binding.tambah.setOnClickListener {
            Intent(this, SumberDetail::class.java).also {
                startActivity(it)
            }
        }

        mitra = mutableListOf()

        getDataList()
        setData()

        val pdf = PdfGenerator()
        binding.pdf.setOnClickListener {
            val db = DBHelper(this, null)
            val date = db.timeNow("dd_MMM_yyyy-HH_mm_ss")

            val datas = db.getName()

            pdf.sumberPdf(this, mitra, date, "Laporan Sumber")
            db.close()
        }

    }

    override fun onResume() {
        super.onResume()

        getDataList()
        setData()
    }

    fun getDataList() {
        val db = DBHelper(this, null)
        mitra.clear()
        sum = 0

        // Mendapatkan semua data nama dari database
        val cursor = db.getMitra()
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

                sum += cursor.getInt(totalColumnIndex)

                val data: SumberJoinTotal = SumberJoinTotal(cursor.getInt(idColumnIndex), cursor.getString(namaColumnIndex), cursor.getString(alamatColumnIndex), cursor.getString(keteranganColumnIndex), cursor.getString(tanggalColumnIndex), cursor.getInt(totalColumnIndex))
                // Menambahkan data ke TextView
                mitra.add(data)
            } while (cursor.moveToNext())

            // Tutup cursor setelah digunakan
            cursor.close()
        } else {
//            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
        }
    }

    fun setData() {
        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        sumberAdapter = SumberAdapter(mitra.toList())
        recyclerView.adapter = sumberAdapter

        binding.sum.text = "Total: " + Formatter.toCurrency(sum)
    }
}
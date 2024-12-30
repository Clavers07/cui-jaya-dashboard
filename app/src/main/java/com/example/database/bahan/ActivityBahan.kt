package com.example.database.bahan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.Formatter
import com.example.database.services.DBHelper
import com.example.database.databinding.ActivityBahanBinding
import com.example.database.model.Bahan
import com.example.database.MainActivity
import com.example.database.R
import com.example.database.model.DataClass
import com.example.database.services.PdfGenerator

class ActivityBahan : AppCompatActivity() {

    private lateinit var binding: ActivityBahanBinding

    var sum: Int = 0

    private lateinit var bahan: MutableList<Bahan>
    private lateinit var bahanAdapter: BahanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Setup View Binding
        binding = ActivityBahanBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        window.statusBarColor = ContextCompat.getColor(this, R.color.gamma)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.beta)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding.kembali.setOnClickListener {
            finish()
        }

        bahan = mutableListOf()

        binding.kembali.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.tambah.setOnClickListener {
            val intent = Intent(this, BahanTambah::class.java)
            startActivity(intent)
        }

        show()
        setView()

        val pdf = PdfGenerator()
        binding.pdf.setOnClickListener {
            val db = DBHelper(this, null)
            val date = db.timeNow("dd_MMM_yyyy-HH_mm_ss")

            val data = DataClass(1, "Agus", "23")
            val lists = mutableListOf<DataClass>()
            lists.add(data)

            val datas = db.getName()

            if (datas != null) {
                while(datas.moveToNext()) {
                    val id = datas.getInt(0)
                    val name = datas.getString(1)
                    val age = datas.getString(2)


                    lists.add(DataClass(id, name, age))
                }
            }


            pdf.bahanPdf(this, bahan, date, "Laporan_Pengeluaran")
        }

    }

    override fun onResume() {
        super.onResume()

        show()
        setView()
    }

    fun show() {
        val db = DBHelper(this, null)
        bahan.clear()
        sum = 0

        // Mendapatkan semua data nama dari database
        val cursor = db.getBahan()
        val size = cursor

        // Memeriksa apakah cursor tidak null
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Mendapatkan kolom berdasarkan nama yang benar
                val idColumnIndex = cursor.getColumnIndexOrThrow("id")
                val nameColumnIndex = cursor.getColumnIndexOrThrow("namaBahan")
                val hargaColumnIndex = cursor.getColumnIndexOrThrow("harga")
                val tanggalColumnIndex = cursor.getColumnIndexOrThrow("tanggal")

                sum += cursor.getInt(hargaColumnIndex)


                // Menambahkan data ke TextView
                bahan.add(Bahan(cursor.getInt(idColumnIndex), cursor.getString(nameColumnIndex), cursor.getInt(hargaColumnIndex), cursor.getString(tanggalColumnIndex)))
            } while (cursor.moveToNext())

            // Tutup cursor setelah digunakan
            cursor.close()
        } else {
//            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
        }
    }

    fun setView() {
        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        bahanAdapter = BahanAdapter(bahan)
        recyclerView.adapter = bahanAdapter

        binding.sum.text = "Total: " + Formatter.toCurrency(sum)
    }
}
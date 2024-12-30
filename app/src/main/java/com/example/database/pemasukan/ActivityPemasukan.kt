package com.example.database.pemasukan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.Formatter
import com.example.database.services.DBHelper
import com.example.database.R
import com.example.database.databinding.ActivityPemasukanBinding
import com.example.database.model.DataClass
import com.example.database.model.Pemasukan
import com.example.database.services.PdfGenerator
import com.example.database.sumber.ActivitySumber
import kotlin.properties.Delegates

class ActivityPemasukan : AppCompatActivity() {

    private lateinit var binding: ActivityPemasukanBinding

    var sum: Int = 0

    private lateinit var pemasukan: MutableList<Pemasukan>
    private lateinit var pemasukanAdapter: PemasukanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /// Set Up Init
        // Setup View Binding
        binding = ActivityPemasukanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gamma)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.beta)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE


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
            pdf.pemasukanPdf(this, pemasukan, date, "Laporan_Pemasukan")
        }


    }

    override fun onResume() {
        super.onResume()

        getLists()
        assignToAdapter()
    }

    fun getLists() {
        val db = DBHelper(this, null)
        pemasukan.clear()
        sum = 0

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

                sum += nilai

                val data: Pemasukan = Pemasukan(id, id_sumber, nama, nilai, keterangan, tanggal)
                // Menambahkan data ke TextView
                pemasukan.add(data)
            } while (cursor.moveToNext())

            // Tutup cursor setelah digunakan
            cursor.close()
        } else {
//            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
        }
    }

    fun assignToAdapter() {
        binding.sum.text = "Total " + Formatter.toCurrency(sum)

        val recyclerView = binding.list
        recyclerView.layoutManager = LinearLayoutManager(this)
        pemasukanAdapter = PemasukanAdapter(pemasukan)
        recyclerView.adapter = pemasukanAdapter
    }
}
package com.example.konsinyasi.bahan

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
import com.example.konsinyasi.databinding.ActivityBahanBinding
import com.example.konsinyasi.model.Pengeluaran
import com.example.konsinyasi.MainActivity
import com.example.konsinyasi.R
import com.example.konsinyasi.model.DataClass
import com.example.konsinyasi.utils.PdfGenerator

class ActivityBahan : AppCompatActivity() {

    private lateinit var binding: ActivityBahanBinding

    var sum: Int = 0
    var filter: String = ""

    private lateinit var pengeluaran: MutableList<Pengeluaran>
    private lateinit var bahanAdapter: BahanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Set status bar and navigation bar colors
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_bg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.main_bg)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Setup View Binding
        binding = ActivityBahanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // After binding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.kembali.setOnClickListener {
            finish()
        }

        pengeluaran = mutableListOf()

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
            pdf.bahanPdf(this, pengeluaran, date, "Laporan_Pengeluaran", filter)
        }
        fun resetBtn() {
            binding.all.backgroundTintList = ContextCompat.getColorStateList(this, R.color.delta)
            binding.all.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.today.backgroundTintList = ContextCompat.getColorStateList(this, R.color.delta)
            binding.today.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.thisMonth.backgroundTintList = ContextCompat.getColorStateList(this, R.color.delta)
            binding.thisMonth.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.thisYear.backgroundTintList = ContextCompat.getColorStateList(this, R.color.delta)
            binding.thisYear.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.all.setOnClickListener {
            resetBtn()
            binding.all.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            binding.all.setTextColor(ContextCompat.getColor(this, R.color.delta))
            filter = DBHelper.nowTime("")
            show()
            setView()
        }
        binding.today.setOnClickListener {
            resetBtn()
            binding.today.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            binding.today.setTextColor(ContextCompat.getColor(this, R.color.delta))
            filter = DBHelper.nowTime("dd MMM yyyy")
            show()
            setView()
        }
        binding.thisMonth.setOnClickListener {
            resetBtn()
            binding.thisMonth.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            binding.thisMonth.setTextColor(ContextCompat.getColor(this, R.color.delta))
            filter = DBHelper.nowTime("MMM yyyy")
            show()
            setView()
        }
        binding.thisYear.setOnClickListener {
            resetBtn()
            binding.thisYear.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            binding.thisYear.setTextColor(ContextCompat.getColor(this, R.color.delta))
            filter = DBHelper.nowTime("yyyy")
            show()
            setView()
        }


    }

    override fun onResume() {
        super.onResume()

        show()
        setView()
    }

    fun show() {
        val db = DBHelper(this, null)
        pengeluaran.clear()
        sum = 0

        // Mendapatkan semua data nama dari database
        val cursor = db.getBahan(filter)
        val size = cursor

        // Memeriksa apakah cursor tidak null
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Mendapatkan kolom berdasarkan nama yang benar
                val idColumnIndex = cursor.getColumnIndexOrThrow("id")
                val nameColumnIndex = cursor.getColumnIndexOrThrow("nama")
                val hargaColumnIndex = cursor.getColumnIndexOrThrow("harga")
                val tanggalColumnIndex = cursor.getColumnIndexOrThrow("tanggal")

                sum += cursor.getInt(hargaColumnIndex)


                // Menambahkan data ke TextView
                pengeluaran.add(Pengeluaran(cursor.getInt(idColumnIndex), cursor.getString(nameColumnIndex), cursor.getInt(hargaColumnIndex), cursor.getString(tanggalColumnIndex)))
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
        bahanAdapter = BahanAdapter(pengeluaran)
        recyclerView.adapter = bahanAdapter

        binding.sum.text = "Total: " + Formatter.toCurrency(sum)
    }
}
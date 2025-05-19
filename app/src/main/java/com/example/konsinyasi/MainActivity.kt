package com.example.konsinyasi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konsinyasi.bahan.ActivityBahan
import com.example.konsinyasi.databinding.ActivityMainBinding
import com.example.konsinyasi.model.SumberJoinTotal
import com.example.konsinyasi.pemasukan.ActivityPemasukan
import com.example.konsinyasi.utils.DBHelper
import com.example.konsinyasi.sumber.ActivitySumber
import com.example.konsinyasi.sumber.SumberAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sumber: MutableList<SumberJoinTotal>
    private lateinit var sumberAdapter: SumberAdapter

    private lateinit var db: DBHelper

    private lateinit var X: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this, null)
        sumber = mutableListOf()

        getDataList()
        setData()


        setupLineChartWithDates(binding.chart, getSumPemasukan(), X)

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

        setupLineChartWithDates(binding.chart, getSumPemasukan(), X)
    }


    fun getSumPemasukan(): List<Entry> {
        val db = DBHelper(this, null)
        val cursor = db.getSumPemasukanWeek()

        var hari = mutableListOf<String>()
        var sum = mutableListOf<Float>()

        println("CEK CURSOR")
        if (cursor != null && cursor.moveToFirst()) {
            print(cursor)
            do {
                try {
                    hari.add(cursor.getString(1))
                    sum.add(cursor.getInt(2).toFloat())
                } catch (e: Exception) {
                    println("Error: $e")
                }
            } while (cursor.moveToNext())
            cursor.close()
        } else {
            sum = mutableListOf(80f, 85f, 78f, 90f, 88f, 92f, 87f)
            hari = mutableListOf(
                "12 Mei", "13 Mei", "14 Mei", "15 Mei", "16 Mei", "17 Mei", "18 Mei"
            )

            // Membuat Entry (x = index, y = nilai)
            val entries = sum.mapIndexed { index, value ->
                Entry(index.toFloat(), value)
            }

            X = hari
            return entries
        }
        val entries = sum.mapIndexed { i, v ->
            Entry(i.toFloat(), v)
        }
        X = hari
        return entries
    }

    fun setupLineChartWithDates(chart: LineChart, data: List<Entry>, X: List<String>) {
        // Data statis: nilai dan tanggal
        val values = listOf(80f, 85f, 78f, 90f, 88f, 92f, 87f)
        val dates = listOf(
            "12 Mei", "13 Mei", "14 Mei", "15 Mei", "16 Mei", "17 Mei", "18 Mei"
        )

        // Membuat Entry (x = index, y = nilai)
        val entries = values.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }

        val dataSet = LineDataSet(data, "Nilai Harian").apply {
            color = Color.BLUE
            valueTextColor = ContextCompat.getColor(this@MainActivity, R.color.main_font)
            setCircleColor(Color.BLUE) // warna garis
            lineWidth = 2f
            circleRadius = 4f
            mode = LineDataSet.Mode.CUBIC_BEZIER // bentuk garis

        }

        // Pasang data ke chart
        chart.data = LineData(dataSet) // bentuk data set dalam chart

        // Label X: Ubah angka menjadi tanggal
        val formatter = IndexAxisValueFormatter(X)
        chart.xAxis.valueFormatter = formatter
        chart.xAxis.granularity = 1f
        chart.xAxis.labelRotationAngle = 0f
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.textColor = ContextCompat.getColor(this@MainActivity, R.color.main_font)
        chart.xAxis.spaceMin = 1f
        chart.xAxis.spaceMax = 1f

        chart.axisRight.isEnabled = false

        chart.legend.apply {
            yEntrySpace = -10f
            xEntrySpace = 5f
            yOffset = 10f
            xOffset = 10f
        }

        chart.xAxis.setDrawGridLines(false)      // grid vertikal (garis di bawah label X)
        chart.axisLeft.setDrawGridLines(true)  // grid horizontal kiri (garis di belakang data)
        chart.axisRight.setDrawGridLines(false) // biasanya grid kanan dimatikan


        chart.axisLeft.textColor = ContextCompat.getColor(this@MainActivity, R.color.main_font)
        chart.axisRight.textColor = ContextCompat.getColor(this@MainActivity, R.color.main_font)

        chart.legend.textColor = ContextCompat.getColor(this@MainActivity, R.color.main_font)
        chart.legend.form = Legend.LegendForm.CIRCLE


        // Pengaturan lainnya
        chart.description.isEnabled = false
        chart.animateY(1000)
        chart.invalidate()
    }

    fun getDataList() {
        val db = DBHelper(this, null)

        // Mendapatkan semua data nama dari database
        val cursor = db.getMitraToday()

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
package com.example.konsinyasi.sumber

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import com.example.konsinyasi.utils.DBHelper
import com.example.konsinyasi.R
import com.example.konsinyasi.databinding.ActivitySumberDetailBinding

class SumberDetail : AppCompatActivity() {

    private lateinit var binding: ActivitySumberDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Setup View Binding
        binding = ActivitySumberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        window.statusBarColor = ContextCompat.getColor(this, R.color.sec_bg)
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding.kembali.setOnClickListener {
            finish()
        }

        binding.batal.setOnClickListener {
            finish()
        }

        var action: Int = intent.getIntExtra("action", 0)

        when (action) {
            0 -> { // insertion
                binding.id.visibility = View.GONE
                binding.hapus.visibility = View.GONE

                binding.simpan.setOnClickListener {
                    val db = DBHelper(this, null)

                    // Mengambil nilai dari EditText
                    val nama = binding.nama.text.toString().trim()
                    val alamat = binding.alamat.text.toString()
                    val keterangan = binding.keterangan.text.toString()

                    // Cek apakah input valid
                    if (nama.isNotEmpty() && alamat.isNotEmpty()) {

                        // Menambahkan data ke database
                        val res = db.addMitra(nama, alamat, keterangan)

                        // Menampilkan pesan Toast
                        if (res) {
                            Toast.makeText(this, "$nama added to database", Toast.LENGTH_LONG).show()

                            finish()
                        } else {
                            Toast.makeText(this, "Failed to add $nama to database", Toast.LENGTH_LONG).show()
                        }
//
                    } else {
                        Toast.makeText(this, "Please enter both nama and alamat", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else -> { // update
                binding.id.visibility = View.VISIBLE
                binding.hapus.visibility = View.VISIBLE

                val id = intent.getStringExtra("ID")

                binding.id.text = id.toString()
                binding.nama.setText(intent.getStringExtra("NAMA_SUMBER"))
                binding.alamat.setText(intent.getStringExtra("ALAMAT"))
                binding.keterangan.setText(intent.getStringExtra("KETERANGAN"))

                binding.hapus.setOnClickListener {
                    val db = DBHelper(this, null)
                    db.deleteMitra(binding.id.text.toString().replace("ID: ", "").toInt())

                    Toast.makeText(this, "Data sumber id ${id.toString()} berhasil dihapus", Toast.LENGTH_LONG).show()

                    finish()
                }

                binding.root.doOnPreDraw { // Use post to run on the UI thread after layout is inflated
                    binding.id.setText("ID: " + intent.getStringExtra("ID"))
//                    Toast.makeText(this, intent.getIntExtra("ID", 0).toString(), Toast.LENGTH_LONG).show()
                    binding.nama.setText(intent.getStringExtra("NAMA"))
                    binding.alamat.setText(intent.getStringExtra("ALAMAT"))
                    binding.keterangan.setText(intent.getStringExtra("KETERANGAN"))

                }

                binding.simpan.setOnClickListener {

                    val db = DBHelper(this, null)
                    val id = binding.id.text.toString().replace("ID: ", "").toInt()
                    val nama = binding.nama.text.toString()
                    val alamat = binding.alamat.text.toString()
                    val keterangan = binding.keterangan.text.toString()

                    // Cek apakah nama kosong
                    if (nama.isNotEmpty() && alamat.isNotEmpty()) {
                        // Coba untuk menghapus data berdasarkan nama
                        val updated = db.updateMitra(id, nama, alamat, keterangan)

                        if (updated > 0) {
                            // Menampilkan pesan sukses
                            Toast.makeText(this, "Berhasil perbarui data sumber $nama", Toast.LENGTH_LONG).show()

                            // Pindah halaman
                            finish()
                        } else {
                            // Menampilkan pesan error jika nama tidak ditemukan
                            Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Masukkan sumber dan alamat dengan benar!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}
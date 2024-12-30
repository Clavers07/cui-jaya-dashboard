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
import androidx.core.view.doOnPreDraw
import com.example.database.Formatter
import com.example.database.services.DBHelper
import com.example.database.R
import com.example.database.databinding.ActivityPemasukanDetailBinding

class PemasukanDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPemasukanDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup View Binding
        binding = ActivityPemasukanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.gamma)
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding.kembali.setOnClickListener {
            finish()
        }

        binding.batal.setOnClickListener {
            finish()
        }

        var action: Int = intent.getIntExtra("action", 0)

        var id_sumber = intent.getStringExtra("ID_SUMBER")
//        Toast.makeText(this, id_sumber, Toast.LENGTH_LONG).show()
        var nama:String = ""
        var alamat: String = ""

        when (action) {
            0 -> { // insertion
                binding.id.visibility = View.GONE
                binding.hapus.visibility = View.GONE

                val db = DBHelper(this, null)

                binding.root.doOnPreDraw { // Use post to run on the UI thread after layout is inflated
                    binding.tanggal.setText(db.timeNow("dd MMMM yyyy HH:mm"))
                    binding.nama.setText(intent.getStringExtra("NAMA"))
                    binding.alamat.setText(intent.getStringExtra("ALAMAT"))
                }

                binding.simpan.setOnClickListener {

                    // Mengambil nilai dari EditText

                    val nama = binding.nama.text.toString().trim()
                    val nilai = Formatter.onlyInt(binding.nilai.text.toString())
                    val catatan = binding.catatan.text.toString()

                    // Cek apakah input valid
                    if (nilai != null) {

                        // Menambahkan data ke database
                        val res = db.addPemasukan(id_sumber.toString().toInt(), nilai, catatan)

                        // Menampilkan pesan Toast
                        if (res) {
                            Toast.makeText(this, "Sumber $nama berhasil ditambahkan", Toast.LENGTH_LONG).show()

                            finish()
                        } else {
                            Toast.makeText(this, "Gagal menambahkan $nama", Toast.LENGTH_LONG).show()
                        }
//
                    } else {
                        Toast.makeText(this, "Masukkan nama dan alamat dengan benar!", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else -> { // update
                binding.id.visibility = View.VISIBLE
                binding.hapus.visibility = View.VISIBLE

                binding.root.doOnPreDraw { // Use post to run on the UI thread after layout is inflated
                    binding.id.setText("ID: " + intent.getStringExtra("ID"))
//                    Toast.makeText(this, intent.getIntExtra("ID", 0).toString(), Toast.LENGTH_LONG).show()
                    binding.nama.setText(intent.getStringExtra("NAMA"))
                    binding.alamat.setText(intent.getStringExtra("ALAMAT"))
                    binding.nilai.setText(Formatter.easyRead(intent.getIntExtra("NILAI", 0)))
                    binding.catatan.setText(intent.getStringExtra("CATATAN"))
                    binding.tanggal.setText(intent.getStringExtra("TANGGAL"))
                }

                binding.hapus.setOnClickListener {
                    val id = intent.getStringExtra("ID")

                    val db = DBHelper(this, null)
                    db.deletePemasukan(binding.id.text.toString().replace("ID: ", ""))
//                    Toast.makeText(this, binding.id.text.toString().replace("ID: ", ""), Toast.LENGTH_LONG).show()

                    Intent(this, ActivityPemasukan::class.java).also {
                        Toast.makeText(this, "Data sumber id ${id.toString()} berhasil dihapus", Toast.LENGTH_LONG).show()
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(it)
                        finish()
                    }
                }

                binding.simpan.setOnClickListener {

                    val db = DBHelper(this, null)
                    val id = Formatter.onlyInt(binding.id.text.toString())
                    val nama = binding.nama.text.toString()
                    val alamat = binding.alamat.text.toString()
                    val nilai = Formatter.onlyInt(binding.nilai.text.toString())
                    val catatan = binding.catatan.text.toString()

                    // Cek apakah nama kosong
                    if (nilai != null) {
                        // Coba untuk menghapus data berdasarkan nama
                        val updated = db.updatePemasukan(id.toString(), nilai, catatan)

                        if (updated > 0) {
                            // Menampilkan pesan sukses
                            Toast.makeText(this, "Berhasil memperbarui data pemasukan $id", Toast.LENGTH_LONG).show()

                            // Pindah halaman
                            val intent = Intent(this, ActivityPemasukan::class.java).also {
                                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(it)
                                finish()
                            }
                        } else {
                            // Menampilkan pesan error jika nama tidak ditemukan
                            Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Input nilai yang benar", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
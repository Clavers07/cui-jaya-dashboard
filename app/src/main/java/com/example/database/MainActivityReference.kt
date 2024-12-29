package com.example.database

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.database.databinding.ActivityMainReferencesBinding
import com.example.database.model.DataClass
import com.example.database.services.DBHelper
import com.example.database.services.PdfGenerator

class MainActivityReference : AppCompatActivity() {

    private lateinit var binding: ActivityMainReferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup View Binding
        binding = ActivityMainReferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DBHelper(this, null).nowDate()

        binding.cari.setOnClickListener{
            val db = DBHelper(this, null)

        }

        // Tambahkan listener pada tombol addName
        binding.addName.setOnClickListener {
            val db = DBHelper(this, null)

            // Mengambil nilai dari EditText
            val name = binding.enterName.text.toString().trim()
            val age = binding.enterAge.text.toString().trim()

            // Cek apakah input valid
            if (name.isNotEmpty() && age.isNotEmpty()) {
                // Menambahkan data ke database
                val res = db.addName(name, age)

                // Menampilkan pesan Toast
                if (res) {
                    Toast.makeText(this, "$name added to database", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed to add $name to database", Toast.LENGTH_LONG).show()
                }
                // Membersihkan EditText
                binding.enterName.text.clear()
                binding.enterAge.text.clear()
            } else {
                Toast.makeText(this, "Please enter both name and age", Toast.LENGTH_LONG).show()
            }
            binding.printName.performClick()
        }

        binding.update.setOnClickListener {
            val db = DBHelper(this, null)

            // Mengambil nilai dari EditText untuk nama
            val id = 2
            val name = binding.enterName.text.toString().trim()
            val age = binding.enterAge.text.toString().trim()

            // Cek apakah nama kosong
            if (name.isNotEmpty() && age.isNotEmpty()) {
                // Coba untuk menghapus data berdasarkan nama
                val updated = db.updateData("data1", id, name, age)

                if (updated > 0) {
                    // Menampilkan pesan sukses
                    Toast.makeText(this, "update nama id $id menjadi $name umur $age", Toast.LENGTH_LONG).show()

                    // Membersihkan EditText
                    binding.enterName.text.clear()
                    binding.enterAge.text.clear()
                } else {
                    // Menampilkan pesan error jika nama tidak ditemukan
                    Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a name to delete", Toast.LENGTH_LONG).show()
            }
        }

        // Tambahkan listener pada tombol printName
        binding.printName.setOnClickListener {
            val db = DBHelper(this, null)

            // Reset TextView sebelum mencetak data baru
            binding.Name.text = ""
            binding.Age.text = ""

            // Mendapatkan semua data nama dari database
            val cursor = db.getName()
            val size = cursor

            // Memeriksa apakah cursor tidak null
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Mendapatkan kolom berdasarkan nama yang benar
                    val nameColumnIndex = cursor.getColumnIndexOrThrow(DBHelper.NAME_COL)
                    val ageColumnIndex = cursor.getColumnIndexOrThrow(DBHelper.AGE_COL)

                    // Menambahkan data ke TextView
                    binding.Name.append(cursor.getString(nameColumnIndex) + "\n")
                    binding.Age.append(cursor.getString(ageColumnIndex) + "\n")
                } while (cursor.moveToNext())

                // Tutup cursor setelah digunakan
                cursor.close()
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
            }
        }

        // Tambahkan listener pada tombol deleteName
        binding.deleteName.setOnClickListener {
            val db = DBHelper(this, null)

            // Mengambil nilai dari EditText untuk nama
            val name = binding.enterName.text.toString().trim()

            // Cek apakah nama kosong
            if (name.isNotEmpty()) {
                // Coba untuk menghapus data berdasarkan nama
                val deleted = db.deleteName(name)

                if (deleted) {
                    // Menampilkan pesan sukses
                    Toast.makeText(this, "$name deleted from database", Toast.LENGTH_LONG).show()

                    // Membersihkan EditText
                    binding.enterName.text.clear()
                    binding.enterAge.text.clear()
                } else {
                    // Menampilkan pesan error jika nama tidak ditemukan
                    Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a name to delete", Toast.LENGTH_LONG).show()
            }
        }

        val pdf = PdfGenerator()
        binding.pdf.setOnClickListener {
            val db = DBHelper(this, null)

            val data = DataClass(1, "Agus", "23")
            val lists = mutableListOf<DataClass>()

            val datas = db.getName()

            if (datas != null) {
                while(datas.moveToNext()) {
                    val id = datas.getInt(0)
                    val name = datas.getString(1)
                    val age = datas.getString(2)


                    lists.add(DataClass(id, name, age))
                }
            }


            pdf.generatePdfFromData(this, lists)
        }

    }
}

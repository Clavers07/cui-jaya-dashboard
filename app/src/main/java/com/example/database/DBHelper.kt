package com.example.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // Membuat database dengan query SQLite
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY, 
                $NAME_COL TEXT, 
                $AGE_COL TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
//
//        val res = db.rawQuery("SELECT DATE('now') as date", null)
//        res.moveToFirst()
//        Log.d("DBHelper", "onCreate: ${res.getString(0)}")

//        res.close()

        val createBahan = """
            CREATE TABLE IF NOT EXISTS bahan (
                "id" INTEGER PRIMARY KEY AUTOINCREMENT, 
                "namaBahan" TEXT,
                "harga" STRING,
                "tanggal" TEXT
            )
        """.trimIndent()
        db.execSQL(createBahan)

        val createSumber = """
            CREATE TABLE IF NOT EXISTS sumber (
                "id" INTEGER PRIMARY KEY AUTOINCREMENT, 
                "nama" TEXT,
                "alamat" STRING,
                "keterangan" TEXT,
                "tanggal" TEXT
            )
        """.trimIndent()
        db.execSQL(createSumber)

        val defSumber = """
            INSERT INTO sumber (nama, alamat, keterangan, tanggal)
            VALUES
                ('Langsung', '-', 'Penjualan secara langsung ke pelanggan', '${timeNow("dd MMMM yyyy")}'),
                ('Pesanan', '-', 'Produk dipesan oleh pelanggan', '${timeNow("dd MMMM yyyy")}')
        """.trimIndent()


        val initSumber = ("INSERT INTO sumber (nama, alamat, keterangan, tanggal) VALUES ('Langsung', '-', 'Penjualan secara langsung ke pelanggan', '${timeNow("dd MMMM yyyy")}'), ('Pesanan', '-', 'Produk dipesan oleh pelanggan', '${timeNow("dd MMMM yyyy")}')")
        db.execSQL(initSumber)


        val createPemasukan = """
            CREATE TABLE IF NOT EXISTS "pemasukan" (
                "id" INTEGER PRIMARY KEY AUTOINCREMENT,
                "id_sumber"	INTEGER,
                "nilai"	INTEGER,
                "catatan" TEXT,
                "tanggal" TEXT,
                FOREIGN KEY (id_sumber) REFERENCES sumber(id)
            )
        """.trimIndent()
        db.execSQL(createPemasukan)

        val createPelanggan = """
            CREATE TABLE IF NOT EXISTS Pelanggan (
                $ID_COL INTEGER PRIMARY KEY, 
                $NAME_COL TEXT,
                alamat TEXT
            )
        """.trimIndent()
        db.execSQL(createPelanggan)

        val createModal = """
            CREATE TABLE IF NOT EXISTS Modal (
                $ID_COL INTEGER PRIMARY KEY, 
                $NAME_COL TEXT
            )
        """.trimIndent()
        db.execSQL(createBahan)
    }

    // Method untuk upgrade database jika versi berubah
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Method untuk menambahkan data ke dalam database
    fun addName(name: String, age: String): Boolean {
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(AGE_COL, age)
        }

        val db = this.writableDatabase
        var result: Long = -1L

        val res = db.rawQuery("SELECT COUNT(id) AS found FROM $TABLE_NAME WHERE name = ?", arrayOf(name))
        if (res.moveToFirst() && res.getInt(0) < 1) {
            result = db.insert(TABLE_NAME, null, values)
        }
        res.close() // Close the cursor

        if (result == -1L) {
            db.close()
            Log.e("DBHelper", "Failed to insert data")
            return false
            // Consider throwing an exception here
        } else {
            db.close()
            Log.d("DBHelper", "Data inserted successfully")
            return true
        }
    }

    // Method untuk mengambil semua data dari database
    fun getName(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    fun timeNow(formatDate: String): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = android.text.format.DateFormat.format(formatDate, currentTime)

        return dateFormat.toString()
    }

    fun getSumberToday():Cursor? {
        val db = this.readableDatabase
        val today = timeNow("dd MMMM yyyy")
        val sql = "SELECT sumber.id, nama, alamat, keterangan, sumber.tanggal, COALESCE(SUM(CASE WHEN pemasukan.tanggal LIKE ? THEN pemasukan.nilai ELSE 0 END), 0) AS total FROM sumber LEFT JOIN pemasukan ON pemasukan.id_sumber = sumber.id GROUP BY sumber.id, nama, alamat, keterangan, sumber.tanggal HAVING total > 0 ORDER BY total DESC"

//        return db.execSQL(sql, arrayOf("%$today%"))
        return db.rawQuery(sql, arrayOf("%$today%"))
    }

    fun getSumber():Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT sumber.id, nama, alamat, keterangan, sumber.tanggal, COALESCE(SUM(pemasukan.nilai), 0) AS total FROM sumber LEFT JOIN pemasukan ON pemasukan.id_sumber = sumber.id GROUP BY sumber.id", null)
    }
    //
    fun addSumber(name: String, alamat: String, keterangan: String): Boolean {

        val values = ContentValues().apply {
            put("nama", name)
            put("alamat", alamat)
            put("keterangan", keterangan)
            put("tanggal", timeNow("dd MMMM yyyy"))
        }

        val db = this.writableDatabase
        var result: Long = db.insert("sumber", null, values)

        if (result == -1L) {
            db.close()
            Log.e("DBHelper", "Failed to insert Sumber data")
            return false
            // Consider throwing an exception here
        } else {
            db.close()
            Log.d("DBHelper", "Data Sumber inserted successfully")
            return true
        }
    }
    fun updateSumber(id: Int, nama: String, alamat: String, keterangan: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("nama", nama)
            put("alamat", alamat)
            put("keterangan", keterangan)
        }

        // Mengupdate data berdasarkan ID
        val result = db.update("sumber", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
    // Delete data bahan
    fun deleteSumber(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("sumber", "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }


    //
    fun getSumPemasukan(): Int {
        val db = this.readableDatabase
        val res = db.rawQuery("SELECT COALESCE(SUM(nilai), 0) AS total FROM pemasukan", null)
        res.moveToFirst()
        val total = res.getInt(0)
        db.close()

        return total
    }

    fun getPemasukan():Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT pemasukan.*, sumber.nama AS nama FROM pemasukan JOIN sumber ON pemasukan.id_sumber = sumber.id", null)
    }
    //
    fun addPemasukan(id_sumber: Int, nilai: Int, catatan: String): Boolean {

        val values = ContentValues().apply {
            put("id_sumber", id_sumber)
            put("nilai", nilai)
            put("catatan", catatan)
            put("tanggal", timeNow("dd MMMM yyyy HH:mm"))
        }

        val db = this.writableDatabase
        var result: Long = db.insert("pemasukan", null, values)

        if (result == -1L) {
            db.close()
            Log.e("DBHelper", "Failed to insert Pemasukan data")
            return false
            // Consider throwing an exception here
        } else {
            db.close()
            Log.d("DBHelper", "Data Pemasukan inserted successfully")
            return true
        }
    }
    fun updatePemasukan(id: String, nilai: Int, catatan: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("nilai", nilai)
            put("catatan", catatan)
        }

        // Mengupdate data berdasarkan ID
        val result = db.update("pemasukan", values, "id = ?", arrayOf(id))
        db.close()
        return result
    }
    // Delete data bahan
    fun deletePemasukan(id: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete("pemasukan", "id=?", arrayOf(id))
        db.close()
        return result > 0
    }



    //
    fun getSumBahan(): Int {
        val db = this.readableDatabase
        val res = db.rawQuery("SELECT COALESCE(SUM(harga), 0) AS total FROM bahan", null)
        res.moveToFirst()
        val total = res.getInt(0)
        db.close()

        return total
    }
    // Ambil data bahan
    fun getBahan():Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM bahan", null)
    }
    // Insert data bahan
    fun addBahan(name: String, harga: String): Boolean {

        val values = ContentValues().apply {
            put("namaBahan", name)
            put("harga", harga)
            put("tanggal", timeNow("dd MMM yyyy"))
        }

        val db = this.writableDatabase
        var result: Long = db.insert("bahan", null, values)

        if (result == -1L) {
            db.close()
            Log.e("DBHelper", "Failed to insert Bahan data")
            return false
            // Consider throwing an exception here
        } else {
            db.close()
            Log.d("DBHelper", "Data Bahan inserted successfully")
            return true
        }
    }
    // update data bahan
    fun updateBahan(id: Int, newName: String, newHarga: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", id)
            put("namaBahan", newName)
            put("harga", newHarga)
        }

        // Mengupdate data berdasarkan ID
        val result = db.update("bahan", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
    // Delete data bahan
    fun deleteBahan(id: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete("bahan", "id=?", arrayOf(id))
        db.close()
        return result > 0
    }

    fun getAll(table: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $table", null)
    }

    fun search(string: String, table: String, field:String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $table WHERE $field LIKE '%$string%'", null)
    }

    fun dup(string: String, table: String, field:String): Cursor? {
        val db = this.readableDatabase
        val res = db.rawQuery("SELECT * FROM $table WHERE $field='$string'", null)

        return res
    }

    fun nowDate() {
        val db = this.readableDatabase
        val res = db.rawQuery("SELECT DATE('now') as date", null)
        res.moveToFirst()
        Log.d("DBHelper", "onCreate: ${res.getString(0)}")
        res.close()
    }


//    fun edit() {
//        val db = this.readableDatabase
//        db.update(TABLE_NAME, "")
//        db.rawQuery("UPDATE $TABLE_NAME SET name='agus', age='19' WHERE id=2", null)
//        db.close()
//    }

    fun updateData(table:String, id: Int, newName: String, newAge: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ID_COL, id)
            put(NAME_COL, newName)
            put(AGE_COL, newAge)
        }

        // Mengupdate data berdasarkan ID
        val result = db.update(table, values, "$id = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Method untuk menghapus data dari database berdasarkan nama
    fun deleteName(name: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$NAME_COL=?", arrayOf(name))
        db.close()
        return result > 0
    }

    companion object {
        const val DATABASE_NAME = "data_user"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "data1"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val AGE_COL = "age"
    }
}

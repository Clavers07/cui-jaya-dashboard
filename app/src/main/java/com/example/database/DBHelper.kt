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

        val createBahan = """
            CREATE TABLE IF NOT EXISTS Bahan (
                $ID_COL INTEGER PRIMARY KEY, 
                $NAME_COL TEXT
            )
        """.trimIndent()
        db.execSQL(createBahan)

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

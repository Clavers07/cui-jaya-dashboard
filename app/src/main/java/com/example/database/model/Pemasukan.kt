package com.example.database.model

data class Pemasukan (
    val id: Int,
    val id_sumber: Int,
    val nama: String,
    val nilai: Int,
    val catatan: String,
    val tanggal: String
)
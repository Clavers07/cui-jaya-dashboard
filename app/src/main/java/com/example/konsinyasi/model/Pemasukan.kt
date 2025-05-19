package com.example.konsinyasi.model

data class Pemasukan (
    val id: Int,
    val id_sumber: Int,
    val nama: String,
    val titipan: Int,
    val nilai: Int,
    val komisi: Int,
    val catatan: String,
    val tanggal: String
)
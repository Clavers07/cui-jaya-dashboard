package com.example.konsinyasi.bahan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.konsinyasi.Formatter
import com.example.konsinyasi.utils.DBHelper
import com.example.konsinyasi.R
import com.example.konsinyasi.model.Pengeluaran

class BahanAdapter(private var pengeluaranList: List<Pengeluaran>) :
    RecyclerView.Adapter<BahanAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_bahan, parent, false)
        return viewHolder(view)
    }
    var sumber:MutableList<Pengeluaran> = pengeluaranList.toMutableList()

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val pengeluaran = pengeluaranList[position]
        holder.namaBahan.text = pengeluaran.name
        holder.harga.text = Formatter.toCurrency(pengeluaran.harga)
        holder.date.text = pengeluaran.tanggal

        holder.hapus.setOnClickListener {
            val db = DBHelper(holder.itemView.context, null)
            db.deleteBahan(pengeluaran.id.toString())
            sumber.removeAt(position)
            updateData(sumber)
        }
        holder.edit.setOnClickListener {
            val intent = Intent(holder.itemView.context, BahanEdit::class.java)
            intent.putExtra("ID", pengeluaran.id.toString())
            intent.putExtra("NAMA_BAHAN", pengeluaran.name)
            intent.putExtra("HARGA", pengeluaran.harga)

            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = pengeluaranList.size

    fun updateData(newData: List<Pengeluaran>) {
        pengeluaranList = newData
        notifyDataSetChanged()
    }

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namaBahan: TextView = view.findViewById(R.id.namaBahan)
        val harga: TextView = view.findViewById(R.id.harga)
        val date: TextView = view.findViewById(R.id.date)

        val edit: ImageButton = view.findViewById(R.id.edit)
        val hapus: ImageButton = view.findViewById(R.id.hapus)

    }
}


package com.example.database.bahan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.database.DBHelper
import com.example.database.R
import com.example.database.model.Bahan

class BahanAdapter(private var bahanList: List<Bahan>) :
    RecyclerView.Adapter<BahanAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_bahan, parent, false)
        return viewHolder(view)
    }
    var sumber:MutableList<Bahan> = bahanList.toMutableList()

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val bahan = bahanList[position]
        holder.namaBahan.text = bahan.id.toString() + ": " + bahan.name
        holder.harga.text = "Rp " + bahan.harga.toString()
        holder.date.text = bahan.tanggal

        holder.hapus.setOnClickListener {
            val db = DBHelper(holder.itemView.context, null)
            db.deleteBahan(bahan.id.toString())
            sumber.removeAt(position)
            updateData(sumber)
        }
        holder.edit.setOnClickListener {
            val intent = Intent(holder.itemView.context, BahanEdit::class.java)
            intent.putExtra("ID", bahan.id.toString())
            intent.putExtra("NAMA_BAHAN", bahan.name)
            intent.putExtra("HARGA", bahan.harga.toString())

            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = bahanList.size

    fun updateData(newData: List<Bahan>) {
        bahanList = newData
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


package com.example.konsinyasi.sumber

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.konsinyasi.Formatter
import com.example.konsinyasi.R
import com.example.konsinyasi.model.SumberJoinTotal
import com.example.konsinyasi.pemasukan.PemasukanDetail

class SumberAdapter(private var sumberList: List<SumberJoinTotal>, var limited: Boolean = false) :
    RecyclerView.Adapter<SumberAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_sumber, parent, false)
        return viewHolder(view)
    }
    var mitra:MutableList<SumberJoinTotal> = sumberList.toMutableList()

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val mitra = sumberList[position]
        holder.nama.text = mitra.nama
        holder.total.text = Formatter.toCurrency(mitra.total)
        holder.tanggal.text = mitra.tanggal
        holder.alamat.text = mitra.alamat

        holder.edit.setOnClickListener {
            val intent = Intent(holder.itemView.context, SumberDetail::class.java)
            intent.putExtra("action", 1)
            intent.putExtra("ID", mitra.id.toString())
            intent.putExtra("NAMA", mitra.nama)
            intent.putExtra("KETERANGAN", mitra.keterangan)
            intent.putExtra("TANGGAL", mitra.tanggal)
            intent.putExtra("ALAMAT", mitra.alamat)

            holder.itemView.context.startActivity(intent)
        }

        holder.tambah.setOnClickListener {
            val intent = Intent(holder.itemView.context, PemasukanDetail::class.java)
            intent.putExtra("action", 0)
            intent.putExtra("ID_SUMBER", mitra.id.toString())
            intent.putExtra("NAMA", mitra.nama)
            intent.putExtra("ALAMAT", mitra.alamat)

            holder.itemView.context.startActivity(intent)
        }

        if (limited) {
            holder.tambah.visibility = View.GONE
            holder.edit.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = sumberList.size

    fun updateData(newData: List<SumberJoinTotal>) {
        sumberList = newData
        notifyDataSetChanged()
    }

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.nama)
        val tanggal: TextView = view.findViewById(R.id.tanggal)
        val total: TextView = view.findViewById(R.id.total)
        val alamat: TextView = view.findViewById(R.id.alamat)
        val logo: ImageView = view.findViewById(R.id.logo)

        val tambah: AppCompatButton = view.findViewById(R.id.tambah)
        val edit: TextView = view.findViewById(R.id.edit)
    }
}


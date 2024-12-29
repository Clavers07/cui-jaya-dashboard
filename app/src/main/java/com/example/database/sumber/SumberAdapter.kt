package com.example.database.sumber

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.database.Formatter
import com.example.database.R
import com.example.database.model.Pemasukan
import com.example.database.model.SumberJoinTotal
import com.example.database.pemasukan.PemasukanDetail

class SumberAdapter(private var sumberList: List<SumberJoinTotal>, var limited: Boolean = false) :
    RecyclerView.Adapter<SumberAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_sumber, parent, false)
        return viewHolder(view)
    }
    var sumber:MutableList<SumberJoinTotal> = sumberList.toMutableList()

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val sumber = sumberList[position]
        holder.nama.text = sumber.nama
        holder.total.text = Formatter.toCurrency(sumber.total)
        holder.tanggal.text = sumber.tanggal
        holder.alamat.text = sumber.alamat

        holder.edit.setOnClickListener {
            val intent = Intent(holder.itemView.context, SumberDetail::class.java)
            intent.putExtra("action", 1)
            intent.putExtra("ID", sumber.id.toString())
            intent.putExtra("NAMA", sumber.nama)
            intent.putExtra("KETERANGAN", sumber.keterangan)
            intent.putExtra("TANGGAL", sumber.tanggal)
            intent.putExtra("ALAMAT", sumber.alamat)

            holder.itemView.context.startActivity(intent)
        }

        holder.tambah.setOnClickListener {
            val intent = Intent(holder.itemView.context, PemasukanDetail::class.java)
            intent.putExtra("action", 0)
            intent.putExtra("ID_SUMBER", sumber.id.toString())
            intent.putExtra("NAMA", sumber.nama)
            intent.putExtra("ALAMAT", sumber.alamat)

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


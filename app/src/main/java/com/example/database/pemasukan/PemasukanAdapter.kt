package com.example.database.pemasukan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.database.R
import com.example.database.model.Pemasukan

class PemasukanAdapter(private var pemasukanList: List<Pemasukan>) :
    RecyclerView.Adapter<PemasukanAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_pemasukan, parent, false)
        return viewHolder(view)
    }
    var Pemasukan:MutableList<Pemasukan> = pemasukanList.toMutableList()

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val pemasukan = pemasukanList[position]
        holder.nama.text = pemasukan.nama
        holder.tanggal.text = pemasukan.tanggal

        var prefix = "Rp "
        if (pemasukan.nilai >= 0) {
            holder.nilai.setTextColor(holder.itemView.context.resources.getColor(R.color.greenMoney))
            prefix = "+$prefix"
        } else {
            holder.nilai.setTextColor(holder.itemView.context.resources.getColor(R.color.redMoney))
            prefix = "-$prefix"
        }
        holder.nilai.text = prefix + pemasukan.nilai.toString()

        holder.container.setOnClickListener {
            val intent = Intent(holder.itemView.context, PemasukanDetail::class.java)
            intent.putExtra("action", 1)
            intent.putExtra("ID", pemasukan.id.toString())
            intent.putExtra("ID_SUMBER", pemasukan.id_sumber.toString())
            intent.putExtra("NAMA", pemasukan.nama)
            intent.putExtra("TANGGAL", pemasukan.tanggal)
            intent.putExtra("NILAI", pemasukan.nilai)
            intent.putExtra("CATATAN", pemasukan.catatan)

            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = pemasukanList.size

    fun updateData(newData: List<Pemasukan>) {
        pemasukanList = newData
        notifyDataSetChanged()
    }

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nama: TextView = view.findViewById(R.id.nama)
        var nilai: TextView = view.findViewById(R.id.total)
        var tanggal: TextView = view.findViewById(R.id.tanggal)
        var edit: TextView = view.findViewById(R.id.edit)

        var container: View = view.findViewById(R.id.container)
    }
}


package com.example.dashboardkiosdeliv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboardkiosdeliv.R
import com.example.dashboardkiosdeliv.model.ListLoketTerdampakModel
import com.google.android.material.imageview.ShapeableImageView

class ListLoketTerdampakAdapter (private  val listLoketTerdampak : ArrayList<ListLoketTerdampakModel>) :
    RecyclerView.Adapter<ListLoketTerdampakAdapter.ListLoketTerdampakViewHolder>() {

    private lateinit var mlistener : onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)

    }

    fun setOnItemclickListener(listener: onItemClickListener) {

        mlistener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLoketTerdampakViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_loket_terdampak_item, parent,false)

        return ListLoketTerdampakViewHolder(itemView, mlistener)
    }

    override fun onBindViewHolder(holder: ListLoketTerdampakViewHolder, position: Int) {

        val currentItem = listLoketTerdampak[position]

        holder.foto.setImageResource(currentItem.foto)
        holder.loket.text = currentItem.loket
        holder.nomor.text = currentItem.nomor
        holder.service.text = currentItem.service
        holder.tanggal.text = currentItem.tanggal

    }

    override fun getItemCount(): Int {

        return listLoketTerdampak.size
    }

    class ListLoketTerdampakViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        //belum fix
        val foto : ShapeableImageView = itemView.findViewById(R.id.image_loket)
        val loket : TextView = itemView.findViewById(R.id.tv_loket)
        val nomor : TextView = itemView.findViewById(R.id.tv_nomor_loket)
        val service : TextView = itemView.findViewById(R.id.tv_error)
        val tanggal : TextView = itemView.findViewById(R.id.tv_tanggal)


        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

    }


}
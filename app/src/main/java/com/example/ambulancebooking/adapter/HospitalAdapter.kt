package com.example.ambulancebooking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ambulancebooking.R
import com.example.ambulancebooking.model.Hospital
import com.squareup.picasso.Picasso

class HospitalAdapter(private val hospitalList : ArrayList<Hospital>) : RecyclerView.Adapter<HospitalAdapter.HospitalHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital, parent, false)
        return HospitalHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: HospitalHolder, position: Int) {

        val currentItem = hospitalList[position]
        holder.id.text = currentItem.id
        holder.hospitalName.text = currentItem.name
        holder.address.text = currentItem.address
        holder.phone.text = currentItem.phone
        Picasso.get().load(currentItem.image).fit().centerCrop().into(holder.image)
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }


    class HospitalHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val id : TextView = itemView.findViewById(R.id.tvId)
        val hospitalName : TextView = itemView.findViewById(R.id.tvHospitalName)
        val address : TextView = itemView.findViewById(R.id.tvAddress)
        val image : ImageView = itemView.findViewById(R.id.imgHospital)
        val phone : TextView = itemView.findViewById(R.id.tvPhone)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
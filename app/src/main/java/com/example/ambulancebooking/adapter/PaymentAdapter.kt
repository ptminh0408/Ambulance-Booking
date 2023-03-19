package com.example.ambulancebooking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ambulancebooking.R

class PaymentAdapter : RecyclerView.Adapter <PaymentAdapter.ViewHolder>() {

    private var hospitalName = arrayOf("Hospital 1", "Hospital 2","Hospital 3","Hospital 4","Hospital 5","Hospital 6",
        "Hospital 7","Hospital 8","Hospital 9","Hospital 10")

    private var dayBook = arrayOf("dd-mm-yyyy", "dd-mm-yyyy","dd-mm-yyyy","dd-mm-yyyy","dd-mm-yyyy","dd-mm-yyyy",
        "dd-mm-yyyy","dd-mm-yyyy","dd-mm-yyyy","dd-mm-yyyy")

    private var moneyPay = arrayOf("$$$$$", "$$$$$","$$$$$","$$$$$","$$$$$","$$$$$",
        "$$$$$","$$$$$","$$$$$","$$$$$",)

    private var imagePay = intArrayOf(R.drawable.invoice, R.drawable.invoice,R.drawable.invoice,R.drawable.invoice,
        R.drawable.invoice,R.drawable.invoice,R.drawable.invoice,R.drawable.invoice,R.drawable.invoice,R.drawable.invoice,)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.payment_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PaymentAdapter.ViewHolder, position: Int) {
        holder.itemHospital.text = hospitalName[position]
        holder.itemDay.text = dayBook[position]
        holder.itemMoney.text = moneyPay[position]
        holder.itemImage.setImageResource(imagePay[position])
    }

    override fun getItemCount(): Int {
        return hospitalName.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView
        var itemHospital : TextView
        var itemDay : TextView
        var itemMoney : TextView

        init{
            itemImage = itemView.findViewById(R.id.payment_image)
            itemHospital = itemView.findViewById(R.id.hospital_payment)
            itemDay = itemView.findViewById(R.id.day_payment)
            itemMoney = itemView.findViewById(R.id.money_payment)
        }
    }
}
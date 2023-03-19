package com.example.ambulancebooking.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ambulancebooking.adapter.PaymentAdapter
import com.example.ambulancebooking.R
import kotlinx.android.synthetic.main.activity_about.backButton
import kotlinx.android.synthetic.main.activity_payment.*

class Payment : AppCompatActivity() {

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<PaymentAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        adapter = PaymentAdapter()
        recyclerView.adapter = adapter

        backButton.setOnClickListener() {
            onBackPressed()
        }
    }
}
package com.example.earningapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.earningapp.databinding.HistoryitemBinding
import com.example.earningapp.model.History
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(var listHistory:ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryitemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {

        return HistoryCoinViewHolder(HistoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {

        holder.binding.timeHistoryTxtView.text= Date(listHistory[position].timeAndDate.toLong()).toString()
        holder.binding.coinTxtView.text=listHistory[position].coin
        holder.binding.status.text = if (listHistory[position].isWithdrawal){"- Coin Withdrawal"}else{"+ Coin Added"}
    }
}
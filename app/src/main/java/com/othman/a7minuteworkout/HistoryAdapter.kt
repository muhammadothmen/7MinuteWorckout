package com.othman.a7minuteworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.othman.a7minuteworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private var items: ArrayList<HistoryEntity>):
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemBinding: ItemHistoryRowBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        val llHistory = itemBinding.llHistoryItemMain
            val id = itemBinding.tvPosition
            val date = itemBinding.tvItem


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))   }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = items[position].date
        holder.id.text = items[position].id.toString()

        if (position % 2 == 0) {
            holder.llHistory.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.llHistory.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorWhite))

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}
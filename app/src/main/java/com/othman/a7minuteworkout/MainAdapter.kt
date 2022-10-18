package com.othman.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.othman.a7minuteworkout.databinding.ItemExerciseStatusBinding

class MainAdapter(private val exerciseList: ArrayList<ExerciseModel>)
    :RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        inner class MainViewHolder( itemExerciseStatusBinding: ItemExerciseStatusBinding):
        RecyclerView.ViewHolder(itemExerciseStatusBinding.root){
            val tvItem = itemExerciseStatusBinding.tvItem

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.tvItem.text = exercise.getId().toString()
        when{
            exercise.getIsSelected() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_circularr_thin_accent_color_border)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            exercise.getIsCompleted() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_circular_color_gray_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }

    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
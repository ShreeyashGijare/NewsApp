package com.example.taskmoengage.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmoengage.R
import com.example.taskmoengage.databinding.ItemFilterBottomsheetBinding
import com.example.taskmoengage.ui.inteface.NewsClickInterface
import com.example.taskmoengage.utils.Constants

class FilterBottomSheetAdapter(
    private val context: Context,
    private val orderTypeList: ArrayList<Constants.KeyValuePairImpl>,
    private val newsClickInterface: NewsClickInterface,
    private var selectedValue: String
) : RecyclerView.Adapter<FilterBottomSheetAdapter.MainViewHolder>() {

    class MainViewHolder constructor(
        var binding: ItemFilterBottomsheetBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterBottomsheetBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderTypeList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        if (orderTypeList[position].value == selectedValue) {
            holder.binding.ivSelected.visibility = View.VISIBLE
            holder.binding.llMain.background = null
        } else {
            holder.binding.ivSelected.visibility = View.INVISIBLE
            holder.binding.llMain.setBackgroundResource(R.drawable.selected_item_background)
        }

        holder.binding.tvFilterType.text = orderTypeList[position].value

        holder.binding.root.setOnClickListener {
            selectedValue = orderTypeList[position].value.toString()
            newsClickInterface.bottomSheetFilterClickListener(orderTypeList[position])
            notifyDataSetChanged()
        }
    }
}
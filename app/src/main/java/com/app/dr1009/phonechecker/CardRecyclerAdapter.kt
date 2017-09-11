package com.app.dr1009.phonechecker

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

class CardRecyclerAdapter(private val mContext: Context) : RecyclerView.Adapter<CardRecyclerAdapter.BindingHolder>() {

    private val mData = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.layout_card, parent, false)
        return BindingHolder(view)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        if (itemCount == 0) {
            return
        }

        val item = mData[position]
        holder.binding.setVariable(BR.text, item)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    fun addItem(string: String) {
        mData.add(string)
    }

    inner class BindingHolder constructor(rowView: View) : RecyclerView.ViewHolder(rowView) {

        val binding: ViewDataBinding = DataBindingUtil.bind(rowView)

    }
}


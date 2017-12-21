package com.eggon.androidd.inapppurchasetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eggon.androidd.inapppurchasetest.R


class MyPurchaseAdapter(private val context: Context, private var items: List<MyPurchaseItem>?) : RecyclerView.Adapter<MyPurchaseAdapter.Holder>() {

    var onItemClickListener: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): Holder =
            Holder(LayoutInflater.from(context).inflate(R.layout.my_purchase_item, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        items?.get(position)?.let {
            holder.sku.text = it.sku
            holder.id.text = it.id
            holder.token.text = it.token
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val sku: TextView = view.findViewById(R.id.sku)
        val id: TextView = view.findViewById(R.id.id)
        val token: TextView = view.findViewById(R.id.token)

        init {
            view.setOnClickListener { onItemClickListener?.invoke(token.text?.toString()) }
        }
    }

    fun updateData(newItems: List<MyPurchaseItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}

data class MyPurchaseItem(val sku: String? = null, val id: String? = null, val token: String? = null)
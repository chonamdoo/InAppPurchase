package com.eggon.androidd.inapppurchasetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eggon.androidd.inapppurchasetest.R


class CheckPurchaseAdapter(private val context: Context, private var items: List<CheckPurchaseItem>?) : RecyclerView.Adapter<CheckPurchaseAdapter.Holder>() {

    var onItemClickListener: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): Holder =
            Holder(LayoutInflater.from(context).inflate(R.layout.check_purchase_item, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        items?.get(position)?.let {
            holder.sku = it.sku
            holder.title.text = it.title
            holder.price.text = it.price
            holder.type.text = it.type
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val price: TextView = view.findViewById(R.id.price)
        val type: TextView = view.findViewById(R.id.type)
        var sku: String? = null

        init {
            view.setOnClickListener { onItemClickListener?.invoke(sku, type.text.toString()) }
        }
    }

    fun updateData(newItems: List<CheckPurchaseItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}

data class CheckPurchaseItem(val sku: String? = null, val title: String? = null, val price: String? = null, val type: String? = null)
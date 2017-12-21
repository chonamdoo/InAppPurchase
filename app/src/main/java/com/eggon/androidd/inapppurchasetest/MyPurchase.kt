package com.eggon.androidd.inapppurchasetest

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import co.eggon.eggoid.extension.error
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ConsumeResponseListener
import com.eggon.androidd.inapppurchasetest.adapter.MyPurchaseAdapter
import com.eggon.androidd.inapppurchasetest.adapter.MyPurchaseItem
import kotlinx.android.synthetic.main.my_purchase.*

class MyPurchase : AppCompatActivity() {

    private var adapter: MyPurchaseAdapter? = null
    private var purchaseList = arrayListOf<MyPurchaseItem>()

    private var consumeListener: ConsumeResponseListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_purchase)

        setAdapter()
        checkMyPurchase(BillingClient.SkuType.INAPP)
        checkMyPurchase(BillingClient.SkuType.SUBS)
    }

    private fun setAdapter() {
        adapter = MyPurchaseAdapter(this, purchaseList)
        adapter?.onItemClickListener = {
            AlertDialog.Builder(this)
                    .setTitle("Consumare l'item?")
                    .setPositiveButton("OK", { _, _ -> consumeItem(it) })
                    .show()
        }
        my_purchase_rv.layoutManager = LinearLayoutManager(this)
        my_purchase_rv.adapter = adapter
    }

    private fun checkMyPurchase(type: String?) {
        MainActivity.billingClient?.queryPurchaseHistoryAsync(type, { responseCode, purchasesList ->
            when (responseCode) {
                BillingClient.BillingResponse.OK -> {
                    purchasesList?.let {
                        if (it.isNotEmpty()) {
                            it.forEach {
                                it.toString().error("my purchase")
                                purchaseList.add(MyPurchaseItem(it.sku, it.orderId, it.purchaseToken))
                            }
                            adapter?.updateData(purchaseList)
                        } else {
                            "no purchase".error()
                        }
                    }
                }
            }
        })
    }

    private fun consumeItem(purchaseToken: String?) {
        purchaseToken.error("purchase token")
        MainActivity.billingClient?.consumeAsync(purchaseToken, consumeListener)

        consumeListener = ConsumeResponseListener { responseCode, purchaseToken ->
            purchaseToken.error("purchase token listener")
            when (responseCode) {
                BillingClient.BillingResponse.OK -> {
                    purchaseToken.error("item consumed")
                    purchaseList.remove(MyPurchaseItem(token = purchaseToken))
                    adapter?.updateData(purchaseList)
                }
                BillingClient.BillingResponse.ERROR -> {
                    "consume error".error()
                }
            }
        }
    }
}
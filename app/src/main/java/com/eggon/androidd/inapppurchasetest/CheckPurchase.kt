package com.eggon.androidd.inapppurchasetest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import co.eggon.eggoid.extension.error
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetailsParams
import com.eggon.androidd.inapppurchasetest.adapter.CheckPurchaseAdapter
import com.eggon.androidd.inapppurchasetest.adapter.CheckPurchaseItem
import kotlinx.android.synthetic.main.check_purchase.*

class CheckPurchase : AppCompatActivity() {

    private val skuParams by lazy { SkuDetailsParams.newBuilder() }
    private val flowParams by lazy { BillingFlowParams.newBuilder() }

    private var adapter: CheckPurchaseAdapter? = null
    private var purchaseList = arrayListOf<CheckPurchaseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_purchase)
        setAdapter()
        getAvailablePurchase(BillingClient.SkuType.INAPP)
        getAvailablePurchase(BillingClient.SkuType.SUBS)
    }

    private fun setAdapter() {
        adapter = CheckPurchaseAdapter(this, purchaseList)
        adapter?.onItemClickListener = { sku, type -> buyItem(sku, type) }
        purchase_rv.layoutManager = LinearLayoutManager(this)
        purchase_rv.adapter = adapter
    }

    private fun getAvailablePurchase(skuType: String) {
        skuParams.setSkusList(MainActivity.skuMap[skuType])
        skuParams.setType(skuType)
        "$skuType ${MainActivity.skuMap[skuType]?.size}".error()

        MainActivity.billingClient?.querySkuDetailsAsync(skuParams.build()) { responseCode, skuDetailsList ->
            when (responseCode) {
                BillingClient.BillingResponse.OK -> {
                    skuDetailsList?.let {
                        if (it.isNotEmpty()) {
                            it.forEach {
                                it.toString().error("purchase available")
                                purchaseList.add(CheckPurchaseItem(it.sku, it.title, it.price, it.type))
                            }
                            adapter?.updateData(purchaseList)
                        } else {
                            "no purchase".error()
                        }
                    }
                }
                BillingClient.BillingResponse.ERROR -> "purchase available error".error()
            }
        }
    }

    private fun buyItem(sku: String?, type: String?) {
        sku?.let { sku ->
            type?.let { type ->
                "$sku - $type".error()
                flowParams.setSku(sku)
                flowParams.setType(type)
                MainActivity.billingClient?.launchBillingFlow(this, flowParams.build())
            }
        }
    }
}
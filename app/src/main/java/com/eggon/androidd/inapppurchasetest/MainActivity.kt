package com.eggon.androidd.inapppurchasetest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.eggon.eggoid.extension.error
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponse
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PurchasesUpdatedListener {

    companion object {
        private val IN_APP_1 = "in_app_111"
        private val IN_APP_2 = "in_app_2b"
        private val IN_APP_3 = "in_app_3"
        private val SUBS_1 = "sub_1"

        var billingClient: BillingClient? = null

        var skuMap = HashMap<String, List<String>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBilling()
        check_purchase.setOnClickListener { startActivity(Intent(this, CheckPurchase::class.java)) }
        check_my_purchase.setOnClickListener { startActivity(Intent(this, MyPurchase::class.java)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        billingClient?.endConnection()
    }

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        when (responseCode) {
            BillingResponse.OK -> {
                "purchase update OK".error()
            }
            BillingResponse.USER_CANCELED -> {
                "purchase update USER CANCELED".error()
            }
            else -> {
            }
        }
    }

    private fun initBilling() {
        billingClient = BillingClient.newBuilder(this).setListener(this).build()
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(@BillingResponse billingResponseCode: Int) {
                if (billingResponseCode == BillingResponse.OK) {
                    "start connection OK".error()
                    skuMap.put(SkuType.INAPP, listOf(IN_APP_1, IN_APP_2, IN_APP_3))
                    skuMap.put(SkuType.SUBS, listOf(SUBS_1))
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                "disconnected billing client".error()
            }
        })
    }
}
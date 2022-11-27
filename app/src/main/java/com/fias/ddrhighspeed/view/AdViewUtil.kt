package com.fias.ddrhighspeed.view

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*

class AdViewUtil {
    fun loadAdView(adView: AdView, context: Context) {
        // AdMob 設定
        MobileAds.initialize(context) {}

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("trouble-adMob-isGone", "onAdFailedToLoad errorCode => " + adError.code)
                Log.i(
                    "trouble-adMob-isGone",
                    "onAdFailedToLoad errorMessage => " + adError.message
                )
            }
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
package com.fias.ddrhighspeed.view

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class AdViewUtil {
    fun loadAdView(adView: AdView, context : Context) {
        // AdMob 設定
        MobileAds.initialize(context) {}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
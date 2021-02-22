package com.kushkipagos.library

import android.content.Context
import siftscience.android.Sift

class SiftScience {

    fun initSiftScience(accountId: String, beaconKey: String, userId: String, sessionId: String, context: Context){
        var siftConfig = Sift.Config.Builder()
            .withAccountId(accountId)
            .withBeaconKey(beaconKey).build()

        Sift.open(context, siftConfig)
        Sift.setUserId(userId)
        Sift.collect()
    }

}
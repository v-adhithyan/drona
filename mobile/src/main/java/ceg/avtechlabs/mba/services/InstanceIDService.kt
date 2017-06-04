package ceg.avtechlabs.mba.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import ceg.avtechlabs.mba.util.Logger
import com.google.firebase.iid.FirebaseInstanceId

import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService

class InstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        Logger.out(FirebaseInstanceId.getInstance().token!!)
    }
}

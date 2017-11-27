package ceg.avtechlabs.mba.services

import ceg.avtechlabs.mba.util.Logger
import com.google.firebase.iid.FirebaseInstanceId

import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        Logger.out(FirebaseInstanceId.getInstance().token!!)
    }
}

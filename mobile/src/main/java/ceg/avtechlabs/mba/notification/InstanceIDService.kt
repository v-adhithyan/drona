package ceg.avtechlabs.mba.notification

import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId

import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token!!
        shareToken(token)
    }

    private fun shareToken(token: String) {
        val intent = Intent(Intent.ACTION_SEND);
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "token")
        intent.putExtra(Intent.EXTRA_TEXT, token)
        //startActivity(Intent.createChooser(intent, "Share using"))
    }
}

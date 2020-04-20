package com.madlab.servicesdemo.services

import android.app.IntentService
import android.content.Intent
import android.widget.Toast

class MyBackgroundService : IntentService(MyBackgroundService::class.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        val value = intent?.getStringExtra("Data")
        Toast.makeText(this, value, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        Toast.makeText(this, "Service Stop", Toast.LENGTH_LONG).show()
    }

}
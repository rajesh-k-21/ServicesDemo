package com.madlab.servicesdemo.services

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    private val mHandler: Handler = Handler()

    fun enqueueWork(context: Context?, intent: Intent?) {
        enqueueWork(context!!, MyJobIntentService::class.java, 2, intent!!)
    }

    override fun onHandleWork(intent: Intent) {
        val value = intent.getStringExtra("Data")
        showToast(value)
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Create MyJobIntentService", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Toast.makeText(this, "Destroy MyJobIntentService", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: CharSequence?) {
        mHandler.post(Runnable {
            Toast.makeText(this@MyJobIntentService, text, Toast.LENGTH_SHORT).show()
        })
    }
}
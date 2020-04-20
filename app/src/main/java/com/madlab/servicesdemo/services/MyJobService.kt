package com.madlab.servicesdemo.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.widget.Toast

class MyJobService : JobService() {

    private val mHandler: Handler = Handler()

    override fun onStartJob(params: JobParameters?): Boolean {
        showToast("On Job Start Job id:- ${params?.jobId}")
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        showToast("On Job Stop Job id:- ${params?.jobId}")
        return true
    }

    private fun showToast(text: CharSequence?) {
        mHandler.post(Runnable {
            Toast.makeText(this@MyJobService, text, Toast.LENGTH_LONG).show()
        })
    }


}
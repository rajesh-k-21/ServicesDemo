package com.madlab.servicesdemo


import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.madlab.servicesdemo.services.MyBackgroundService
import com.madlab.servicesdemo.services.MyJobIntentService
import com.madlab.servicesdemo.services.MyJobService
import com.madlab.servicesdemo.services.MyService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var startIntent: Intent
    private lateinit var backgroundIntent: Intent
    private lateinit var jobIntentService: Intent
    private lateinit var serviceConnection: ServiceConnection

    private var mScheduler: JobScheduler? = null

    private val JOB_ID = 3 * 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStartService.setOnClickListener(this)
        buttonStopService.setOnClickListener(this)

        val dataString: String = "This is test String"
        startIntent = Intent(this, MyService::class.java)

        backgroundIntent = Intent(this, MyBackgroundService::class.java).apply {
            putExtra("Data", dataString)
        }
        jobIntentService = Intent(this, MyJobIntentService::class.java).apply {
            putExtra("Data", dataString)
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val myBinder = service as MyService.MyBinder
                MyService().MyBinder().getService()
            }

        }

    }

    private fun createJobScheduler() {

        if (!isJobServiceOn()) {
            val componentName = ComponentName(this, MyJobService::class.java)

            val mJobInfo = JobInfo.Builder(JOB_ID, componentName)
                .setOverrideDeadline(6000)
                .build()

            mScheduler =
                getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            val resultCode = mScheduler?.schedule(mJobInfo)

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Toast.makeText(this, "Job Scheduled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Job not Scheduled", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        val mScheduler =
            getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        mScheduler.cancel(JOB_ID)
    }

    private fun isJobServiceOn(): Boolean {
        val scheduler =
            getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        var hasBeenScheduled = false

        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == JOB_ID) {
                hasBeenScheduled = true
            }
        }
        return hasBeenScheduled
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonStartService -> {
                //Service
                /*startIntent.also {
                    startService(it)
                }*/

                //Job Scheduler
                // createJobScheduler()

                //Intent Service
                //this.startService(backgroundIntent)

                //JobIntentService
                //MyJobIntentService().enqueueWork(this, jobIntentService)

                //BindService
                bindService(
                    Intent(this, MyService::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )

            }
            R.id.buttonStopService -> {
                //Service
                //stopService(startIntent)

                //JobScheduler
                //mScheduler?.cancelAll()


                //Intent Service
                //this.stopService(backgroundIntent)

                //UnBindService
                unbindService(serviceConnection)

            }
        }
    }
}

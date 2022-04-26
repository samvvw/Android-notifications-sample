package com.example.timernotificationapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    // Channel creation constants
    val CHANNEL_ID = "myChannelId"
    val CHANNEL_NAME = "MyChannelName"
    val NOTIFICATION_ID = 1


    // Button and Text output variables
    lateinit var btnCreateNotification: Button
    lateinit var elapsedTime: TextView


    // notification classes
    private lateinit var notifiaction: Notification
    private lateinit var notificationManager: NotificationManagerCompat

    // timer variables
    private lateinit var timer: CountDownTimer
    var selectedTime = 10 * 1_000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        btnCreateNotification = findViewById(R.id.triggerNotification)
        elapsedTime = findViewById(R.id.timerOutput)


        // create the pending intent
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val bigText = NotificationCompat.BigTextStyle()

        // create notification
        notifiaction = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Out notification title")
            .setContentText("This is the content text of our notificaiton")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setStyle(bigText.bigText("Hello world from big text"))
            .build()

        notificationManager = NotificationManagerCompat.from(this)

        btnCreateNotification.setOnClickListener {
//            notificationManager.notify(NOTIFICATION_ID,notifiaction)
            startTimer()
        }

    }


    private fun createNotificationChannel(){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val channel = NotificationChannel(
               CHANNEL_ID,
               CHANNEL_NAME,
               NotificationManager.IMPORTANCE_HIGH
           ).apply {
               lightColor = Color.RED
               enableLights(true)

               description = "This is the description"
               setShowBadge(true)
           }

           val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

           manager.createNotificationChannel(channel)
       }
    }

    private fun startTimer (){

        timer = object : CountDownTimer(selectedTime, 1_000L) {
            override fun onTick(timeRemainingInMilliseconds: Long) {
               elapsedTime.text = "Done in: " + (timeRemainingInMilliseconds/1000).toString()
            }

            override fun onFinish() {
                elapsedTime.text = "Done!!"
                notificationManager.notify(NOTIFICATION_ID,notifiaction)
            }

        }

        timer.start()
    }
}
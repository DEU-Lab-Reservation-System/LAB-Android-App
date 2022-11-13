package com.example.lab.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.view.activity.LoginActivity
import com.example.lab.view.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LabFirebaseMessagingService: FirebaseMessagingService() {
    /**
     * FCM 서버에 앱이 등록되면 호출되는 메소드
     * 파라미터로 토큰 값이 전달 됨
     */
    override fun onNewToken(token: String) {
        Log.i("[ FIREBASE ] onNewToken", "Success save token = $token")
        Log.i("[ FIREBASE ] onNewToken", "User Token = ${MyApplication.member?.deviceToken}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.data.isNotEmpty()){
            sendNotification(
                message.data["title"].toString(),
                message.data["body"].toString()
            )
        } else {
            message.notification?.let {
                sendNotification(
                    message.notification?.title.toString(),
                    message.notification?.body.toString()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title: String, body: String){
        val notifyId = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.app_name)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelId,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notifyId, notificationBuilder.build())
    }
}
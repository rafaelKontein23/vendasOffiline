package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActLogin
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.Locale

class ReceberPush: FirebaseMessagingService() {

    var link: String? = null
    var token: String? = null
    private var bitmap: Bitmap? = null
    private var remoteMessage1: RemoteMessage? = null
    private var notficationjson = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
            sendNotification(messageBody = remoteMessage)

    }

    private fun sendNotification(messageBody: RemoteMessage) {

             val intent = Intent(this, ActLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(
                this,
                ((Date().time / 1000L) % Int.MAX_VALUE).toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            bitmap = getBitmapFromURL(messageBody.data["Image"])

            val channelId = "My channel ID"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.tdf_azul)
                .setColor(Color.parseColor("#000000"))
                .setContentTitle(messageBody.data["Title"])
                .setContentText(messageBody.data["Body"])
                .setAutoCancel(true)

                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

               if (bitmap != null){
                   notificationBuilder.setLargeIcon(bitmap)
                       .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody.data["Body"]))
               }else{
                   notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.imgpush))
                       .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody.data["Body"]))
               }





            val notificationManager = NotificationManagerCompat.from(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(((Date().time / 1000L) % Int.MAX_VALUE).toInt(), notificationBuilder.build())


    }

    private fun getBitmapFromURL(url: String?): Bitmap? {
        try {
            val src = URL(url)
            val connection = src.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}


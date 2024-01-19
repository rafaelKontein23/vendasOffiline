package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentCargas

class PushNativo {

    companion object {
        fun showNotification(context: Context, channelId: String, title: String, message: String) {
            val contentView = RemoteViews(context.packageName, R.layout.celula_notificacao_carga)
            contentView.setTextViewText(R.id.notification_title,"Atualizando")
            contentView.setProgressBar(
                R.id.notification_content,10,
                FragmentCargas.progresspush,false)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.tdf_azul)
                .setContentTitle(title)
                .setCustomBigContentView(contentView)
                .setContentText(message)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)

                notificationManager.notify(1, notificationBuilder.build())


        }
        fun showNotificationPedido(context: Context, channelId: String, title: String, message: String) {

            val contentView = RemoteViews(context.packageName, R.layout.celula_notificacao_carga)
            contentView.setTextViewText(R.id.notification_title,title)
            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.bigText(message)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.tdf_azul)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(bigTextStyle)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)

            notificationManager.notify(15, notificationBuilder.build())


        }
    }
}
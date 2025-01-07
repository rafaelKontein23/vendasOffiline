package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.app.PendingIntent
import androidx.core.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentCargas

class PushNativo {

    companion object {
        fun showNotification(context: Context, channelId: String, title: String, message: String) {
            // 1. Criar o campo de resposta (RemoteInput)
            val replyLabel = "Digite sua resposta"
            val remoteInput = RemoteInput.Builder("key_text_reply")
                .setLabel(replyLabel)
                .build()

            // 2. Criar o PendingIntent para tratar a resposta
            val replyIntent = Intent(context, ActPricipal::class.java) // Substitua por sua Activity que processa a resposta
            val replyPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // Adicionar FLAG_MUTABLE no Android 12+
            )

            // 3. Adicionar a ação de resposta
            val replyAction = NotificationCompat.Action.Builder(
                R.drawable.tdf_azul,  // Ícone de resposta (substitua pelo ícone desejado)
                "Responder",               // Texto da ação
                replyPendingIntent
            )
                .addRemoteInput(remoteInput) // Adiciona o RemoteInput à ação
                .build()

            // 4. Construir a notificação
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.tdf_azul)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(replyAction) // Adiciona a ação de resposta à notificação

            // 5. Enviar a notificação
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(15, notificationBuilder.build())
        }

        fun showNotificationPedido(context: Context, channelId: String, title: String, message: String) {

            // 1. Criar o campo de resposta (RemoteInput)
            val replyLabel = "Digite sua resposta"
            val remoteInput = RemoteInput.Builder("key_text_reply")
                .setLabel(replyLabel)
                .build()

            // 2. Criar o PendingIntent para tratar a resposta
            val replyIntent = Intent(context, ActPricipal::class.java) // Substitua por sua Activity que processa a resposta
            val replyPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // Adicionar FLAG_MUTABLE no Android 12+
            )

            // 3. Adicionar a ação de resposta
            val replyAction = NotificationCompat.Action.Builder(
                R.drawable.tdf_azul,  // Ícone de resposta (substitua pelo ícone desejado)
                "Responder",               // Texto da ação
                replyPendingIntent
            )
                .addRemoteInput(remoteInput) // Adiciona o RemoteInput à ação
                .build()

            // 4. Construir a notificação
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.tdf_azul)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(replyAction) // Adiciona a ação de resposta à notificação

            // 5. Enviar a notificação
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(15, notificationBuilder.build())

        }
    }
}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.util.Log

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActLogin

class CapturaDeviceToken {

        fun recuperaToken(){
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    ActLogin.device = token

                    Log.d("device tokrnnnn", token)

                })
            }catch (e:Exception){
                e.printStackTrace()
            }

        }




}
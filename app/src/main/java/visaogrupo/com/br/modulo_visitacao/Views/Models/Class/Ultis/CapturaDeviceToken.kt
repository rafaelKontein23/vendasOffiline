package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.util.Log

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class CapturaDeviceToken {
    fun recuperaToken(){
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                Log.d("device tokrnnnn", token)

            })
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}
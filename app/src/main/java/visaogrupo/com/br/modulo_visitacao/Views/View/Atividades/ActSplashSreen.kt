package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.ExcluiDados
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.ExcluirPrefuser
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.PushNativo

class ActSplashSreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_splash_sreen)


               val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
               val dataUltimoLogin = sharedPreferences?.getString("dataLogin", null)
               val userObject = sharedPreferences?.getString("UserLogin", null)
              val handler = Handler()
            PushNativo.showNotificationPedido(this,"TESTE1","Seja Bem Vindo!","Tudo farma está em execução")


               val dataAtual = DataAtual()
               if (dataUltimoLogin != null) {
                   val dataPassadaBoleaan = dataAtual.recuperaData()

                   if (!dataUltimoLogin.equals(dataPassadaBoleaan)){
                       handler.postDelayed({
                           val intent =  Intent(this,ActLogin::class.java)
                           startActivity(intent)
                           finish() // Finalizando esta atividade se necessário
                       }, 1000)

                   }else {

                       if (userObject != null){
                           handler.postDelayed({

                               val intent =  Intent(this,ActPricipal::class.java)
                               intent.putExtra("Tela","")
                               startActivity(intent)
                               finish() // Finalizando esta atividade se necessário
                           }, 1000)

                       }else {
                           handler.postDelayed({
                               val intent =  Intent(this,ActLogin::class.java)
                               startActivity(intent)
                               finish() // Finalizando esta atividade se necessário
                           }, 1000)
                       }
                   }
               }else {
                   handler.postDelayed({
                       val intent =  Intent(this,ActLogin::class.java)
                       startActivity(intent)
                       finish() // Finalizando esta atividade se necessário
                   }, 1000)
               }
           }

}
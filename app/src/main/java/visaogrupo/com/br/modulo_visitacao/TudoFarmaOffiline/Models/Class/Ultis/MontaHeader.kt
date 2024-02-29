package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Retrofit_Request.URLs
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActLogin
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MontaHeader {

    companion object {
        fun montaHeader(userId:String):String{
            val autenticador = StringBuilder()
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val data = Calendar.getInstance().time
            autenticador.append(sdf.format(data))
            autenticador.append("|")
            autenticador.append(URLs.urlEnviaPedido)
            autenticador.append("|")
            val sdf2 = SimpleDateFormat("ddMMyy")
            autenticador.append(sdf2.format(data))
            val  inript = Incript()

            var autenticacao = inript.encryptCBC(autenticador.toString())
            var codigo = 1

            val params = "$codigo|${ActLogin.versao}"
            val body = inript.encryptCBC(params)
            autenticacao += ",$body"

            return autenticacao
        }
    }
}
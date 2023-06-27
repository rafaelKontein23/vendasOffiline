package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import java.text.SimpleDateFormat
import java.util.*

class RecuperaDataAtual {

    companion object {
        fun dataAtual():String{
            // Obter a data e hora atual
            var  data =""
            val calendar = Calendar.getInstance()
            val dataAtual = calendar.time
            val formatoData = SimpleDateFormat("dd/MM/yyyy")
            val dataFormatada = formatoData.format(dataAtual)
            val horaAtual = calendar.get(Calendar.HOUR_OF_DAY)
            val minutosAtuais = calendar.get(Calendar.MINUTE)
            val segundosAtuais = calendar.get(Calendar.SECOND)

            data= dataFormatada+horaAtual+ ":"+minutosAtuais + ":" +segundosAtuais

            return data
        }

    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import java.text.SimpleDateFormat
import java.util.Calendar

class HoraAtual {

    companion object
    {
         fun horaAtual():String{
             val calendar = Calendar.getInstance()
             val horaAtual = calendar.get(Calendar.HOUR_OF_DAY)
             val minutosAtuais = calendar.get(Calendar.MINUTE)

             val horaMinutos = "$horaAtual:$minutosAtuais"

             return horaMinutos
         }

    }
}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class VerificaTempoDoPedido {
    @RequiresApi(Build.VERSION_CODES.O)
    fun verifcaTempoPedido(ano:Int,mes:Int, dia:Int,horaAtual:Int,minuto:Int):String{
        val dataPedido = LocalDateTime.of(ano, mes, dia, horaAtual, minuto)
        val dataAtual = LocalDateTime.now()

        var diferencaTempo = ChronoUnit.MINUTES.between(dataPedido, dataAtual)
        var tempo = diferencaTempo.toString() + "min"
        if (diferencaTempo.toInt() == 0){
            tempo = "agora"
        }
        if (diferencaTempo > 60){
            diferencaTempo =  ChronoUnit.HOURS.between(dataPedido, dataAtual)
            tempo =diferencaTempo.toString() + "h"
            if (diferencaTempo >24){
                diferencaTempo =  ChronoUnit.DAYS.between(dataPedido, dataAtual)
                tempo = diferencaTempo.toString() + "d"
            }
        }
        Log.d("tempo", diferencaTempo.toString())

        return tempo
    }
}
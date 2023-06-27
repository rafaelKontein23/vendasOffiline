package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import java.text.SimpleDateFormat
import java.util.*

class DataAtual {

    fun recuperaData():String{
        val calendar = Calendar.getInstance()
        val dataAtual = calendar.time
        val formatoData = SimpleDateFormat("dd/MM/yyyy")
        val dataFormatada = formatoData.format(dataAtual)

        return dataFormatada
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

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


    fun verificarDataPassada(dataString: String): Boolean {
        val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataFornecida: Date = formatoData.parse(dataString) ?: return false

        val dataAtual = Calendar.getInstance().time

        return dataAtual.after(dataFornecida)
    }

}
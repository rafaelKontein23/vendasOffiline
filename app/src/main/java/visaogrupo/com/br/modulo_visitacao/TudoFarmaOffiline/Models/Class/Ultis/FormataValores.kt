package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import java.text.NumberFormat
import java.util.Locale

class FormataValores {

    companion object {
        fun formatarParaMoeda(numero: Number): String {
            val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            return formatoMoeda.format(numero)
        }

        fun converterMoedaParaDouble(valorMoeda: String): Double? {
            val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            return try {
                val numero = formatoMoeda.parse(valorMoeda)
                numero?.toDouble()
            } catch (e: Exception) {
                null
            }
        }

    }
}
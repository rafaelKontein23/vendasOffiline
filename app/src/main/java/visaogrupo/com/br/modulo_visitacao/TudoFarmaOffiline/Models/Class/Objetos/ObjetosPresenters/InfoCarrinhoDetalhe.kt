package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ObjetosPresenters

import android.content.res.ColorStateList

data class InfoCarrinhoDetalhe (
    val valorComDesconto:Double,
    val quatidade:Int,
    val valorTotal:Double,
    val colorProgrres: ColorStateList,
    val resultadoValorMinimo:Double,
    val valorTotdescontoMedio:Double
)
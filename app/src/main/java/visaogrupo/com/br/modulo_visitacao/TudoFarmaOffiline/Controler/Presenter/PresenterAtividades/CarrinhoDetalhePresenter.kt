package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.res.ColorStateList
import android.graphics.Color
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ObjetosPresenters.InfoCarrinhoDetalhe

class CarrinhoDetalhePresenter {

    fun atualizainformacoesCarrinhoDetalhe(valorTotal:Double,
                                           lista: MutableList<Carrinho>,
                                           valorMinimo:Double,
                                           ): InfoCarrinhoDetalhe {
        var valorDesconto = 0.0
        var quantidade = 0
        var valorTotalFun =valorTotal
        var colorStateList :ColorStateList

        for (i in 0 until  lista.size){
            valorTotalFun += lista[i].valortotal.toString().toDouble()
        }
        for(k in 0 until  lista.size){
            valorDesconto += lista[k].desconto.toString().toDouble()
        }
        for(j in 0 until  lista.size){
            quantidade += lista[j].quantidade.toString().toInt()
        }

        val resultado = (valorMinimo * 40) / 100

        if(valorTotalFun<=resultado ){

            val greenColor = Color.parseColor("#B40101")
            colorStateList = ColorStateList.valueOf(greenColor)
        }else if(valorTotalFun > resultado && valorTotalFun <valorMinimo){
            val greenColor = Color.parseColor("#CF7001")
            colorStateList = ColorStateList.valueOf(greenColor)

        }else{
            val greenColor = Color.parseColor("#01B45E")
            colorStateList = ColorStateList.valueOf(greenColor)

        }
        val  valorDescontoMedio = valorDesconto / lista.size

        var infoCarrinhoDetalhe = InfoCarrinhoDetalhe(valorDesconto,
            quantidade,valorTotalFun,colorStateList,resultado, valorDescontoMedio)


       return  infoCarrinhoDetalhe
    }
}
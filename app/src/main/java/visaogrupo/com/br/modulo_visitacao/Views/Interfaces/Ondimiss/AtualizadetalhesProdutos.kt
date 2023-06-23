package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho

interface AtualizadetalhesProdutos {
    fun detalhes(lista:MutableList<Carrinho>,atualiza:Boolean,position :Int,valor:Double)
}
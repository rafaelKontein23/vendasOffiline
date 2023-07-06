package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho

interface AtualizadetalhesProdutos {
    fun detalhes(lista:MutableList<Carrinho>, atualiza:Boolean, position :Int, valor:Double)
}
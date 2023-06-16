package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Produtos

interface AtualizaProgressiva {

    fun ProgressivaAtualiza(ProtudoCodigo:Int,protudoseleciona: ProdutoProgressiva,quatidade:Int,valor:Double,desconto:Double)
}
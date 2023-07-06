package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva

interface AtualizaProgressiva {

    fun ProgressivaAtualiza(ProtudoCodigo:Int, protudoseleciona: ProdutoProgressiva, quatidade:Int, valor:Double, desconto:Double)
}
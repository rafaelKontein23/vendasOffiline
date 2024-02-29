package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva

interface AtualizaProgressiva {

    fun ProgressivaAtualiza(ProtudoCodigo:Int, protudoseleciona: ProdutoProgressiva, quatidade:Int, valor:Double, desconto:Double)
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoAB
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoValorAB

interface AtualizaProgress {

    fun atualizaprogress(valorTotalA:MutableList<ProdutoValorAB>, valorTotalB:MutableList<ProdutoValorAB>,
                         TiapoA:Boolean, mais:Boolean,
                         itemProdutoAB: ProdutoAB,
                         getQuantidade:Int,
                         temNaLista:Boolean,
                         valorTotal:Double)
}
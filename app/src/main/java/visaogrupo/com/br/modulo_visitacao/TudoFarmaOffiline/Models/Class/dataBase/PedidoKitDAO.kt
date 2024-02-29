package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FormaDePagaemnto

class PedidoKitDAO (context: Context){
    val dbPedidoKIt =  DataBaseHelber(context).writableDatabase

    fun insert(carrinhokit: CarrinhoKit, data:String, hora:String, chave:String,
               numeroPedido:String, opls:String, justificativaAnr :String,
               formaDePagamento: FormaDePagaemnto,
               RegraPrazo:Int, QuantidadeMaxima:Int){

        try {
            val valoresPedidos = ContentValues().apply {
                put("loja_id", carrinhokit.lojaId)
                put("cliente_id", carrinhokit.clienteId)
                put("OperadorLogistigo", carrinhokit.operadorLogistigo)
                put("Usuario_id", carrinhokit.usuarioId)
                put("UF", carrinhokit.uf)
                put("kitCodigo", carrinhokit.kitCodigo)
                put("Quantidade", carrinhokit.quantidade)
                put("De", carrinhokit.valorDe)
                put("por", carrinhokit.por)
                put("Desconto", 0.0)
                put("CODLISTAPRECOSYNC", carrinhokit.codListaPrecoSync)
                put("ValorTotal", carrinhokit.valortotal)
                put("NomeKit", carrinhokit.nomeKit)
                put("nomeLoja", carrinhokit.nomeLoja)
                put("razaosocial", carrinhokit.razaoSolcial)
                put("cnpj", carrinhokit.cnpj)
                put("dataPedido", data)
                put("valorminimoLoja", carrinhokit.valorMinimoLoja)
                put("Qtd_Minima_Operador", carrinhokit.QtdMinima_Operador)
                put("Qtd_Maxima_Operador", carrinhokit.QtdMaxima_Operador)
                put("PedidoEnviado", 0)
                put("formaDePagemento",formaDePagamento.Cod_FormaPgto)
                put("NumeroPedido", numeroPedido)
                put("OperadoresPedidos", opls)
                put("FormaPagamentoExclusiva", formaDePagamento.exlusiva)
                put("Chave", chave)
                put("TipoLoja", carrinhokit.LojaTipo)
                put("RegraPrazo",RegraPrazo)
                put("QtdMaximaOpl",QuantidadeMaxima)



            }

            val pedidoID: Long =  dbPedidoKIt.insertOrThrow("TBPedidosFinalizadosKit",null,valoresPedidos)

            for (i in carrinhokit.listProdutoKit!!){
                val valoresProdutosPedidos = ContentValues()
                valoresProdutosPedidos.put("NomeProduto",i.produtoNome)
                valoresProdutosPedidos. put("PedidoID", pedidoID)
                valoresProdutosPedidos. put("Barra", i.barra)
                valoresProdutosPedidos. put("Produto_codigo", i.produtoCodigo)
                valoresProdutosPedidos. put("Desconto", i.desconto)
                valoresProdutosPedidos. put("PF", i.valorTotal)
                valoresProdutosPedidos. put("Quantidade", i.quantidade)
                try {
                    dbPedidoKIt.insertOrThrow("TB_ProdutosKit_Pedidos_Finalizado", null,valoresProdutosPedidos)

                }catch (e: SQLiteException){
                    println("Erroo ${e.message}")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
}
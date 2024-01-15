package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutosFinalizados

class PedidosFinalizadosDAO(context: Context) {
    val dbPedido = DataBaseHelber(context)
    fun uptadePedidoEnviado(peidoEnvia:Int, pedidoId :Int){
        val queryUp = "UPDATE TBPedidosFinalizados SET PedidoEnviado = '$peidoEnvia' WHERE PedidoID = $pedidoId"
        dbPedido.writableDatabase.execSQL(queryUp)
    }
    fun insert(carrinho:Carrinho, data:String,hora:String, chave:String,
               listaProdutduos:MutableList<Carrinho>,
               numeroPedido:String,opls:String, justificativaAnr :String, formaDePagamento:FormaDePagaemnto){

        val valoresPedidos = ContentValues().apply {
            put("loja_id", carrinho.lojaId)
            put("cliente_id", carrinho.clienteId)
            put("OperadorLogistigo", carrinho.operadorLogistigo)
            put("Usuario_id", carrinho.usuarioId)
            put("UF", carrinho.uf)
            put("Comissao", carrinho.comissao)
            put("ComissaoPorcentagem", carrinho.comissaoPorcentagem)
            put("MarcasXComissoes_id", carrinho.marcasXComissoesId)
            put("Produto_codigo", carrinho.produtoCodigo)
            put("Barra", carrinho.barra)
            put("Quantidade", carrinho.quantidade)
            put("PF", carrinho.pf)
            put("Valor", carrinho.valor)
            put("ValorOriginal", carrinho.valorOriginal)
            put("Grupo_Codigo", carrinho.grupoCodigo)
            put("Desconto", carrinho.desconto)
            put("DescontoOriginal", carrinho.descontoOriginal)
            put("ST", carrinho.st)
            put("formalizacao", carrinho.formalizacao)
            put("CODLISTAPRECOSYNC", carrinho.codListaPrecoSync)
            put("ValorTotal", carrinho.valortotal)
            put("Nome", carrinho.nomeProduto)
            put("Apontador_codigo", carrinho.apontadorCodigo)
            put("nomeLoja", carrinho.nomeLoja)
            put("razaosocial", carrinho.razaoSolcial)
            put("cnpj", carrinho.cnpj)
            put("dataPedido", data)
            put("valorminimoLoja", carrinho.valorMinimoLoja)
            put("base64", carrinho.base64)
            put("caixapadrao", carrinho.caixapadrao)
            put("pmc", carrinho.pmc)
            put("Qtd_Minima_Operador", carrinho.QtdMinima_Operador)
            put("Qtd_Maxima_Operador", carrinho.QtdMaxima_Operador)
            put("ANR", carrinho.Anr)
            put("PedidoEnviado", 0)
            put("formaDePagemento",formaDePagamento.Cod_FormaPgto)
            put("NumeroPedido", numeroPedido)
            put("OperadoresPedidos", opls)
            put("FormaPagamentoExclusiva", formaDePagamento.exlusiva)
            put("Chave", chave)
            put("justificativaANR", justificativaAnr)
            put("TipoLoja", carrinho.LojaTipo)



        }

        val pedidoID: Long =  dbPedido.writableDatabase.insertOrThrow("TBPedidosFinalizados",null,valoresPedidos)





        for (i in listaProdutduos){
            val valoresProdutosPedidos = ContentValues()
            valoresProdutosPedidos.put("NomeProduto",i.nomeProduto)
            valoresProdutosPedidos. put("PedidoID", pedidoID)
            valoresProdutosPedidos. put("Barra", i.barra)
            valoresProdutosPedidos. put("Produto_codigo", i.produtoCodigo)
            valoresProdutosPedidos. put("Desconto", i.desconto)
            valoresProdutosPedidos. put("DescontoOriginal", i.descontoOriginal)
            valoresProdutosPedidos. put("formalizacao", i.formalizacao)
            valoresProdutosPedidos. put("PF", i.pf)
            valoresProdutosPedidos. put("Quantidade", i.quantidade)
            valoresProdutosPedidos. put("ValorRepasse", i.valor)
            valoresProdutosPedidos. put("ST", i.st)
            valoresProdutosPedidos. put("Valor", i.valortotal)
            try {
                dbPedido.writableDatabase.insertOrThrow("TB_Produtos_Pedidos_Finalizado", null,valoresProdutosPedidos)

            }catch (e:SQLiteException){
                println("Erroo ${e.message}")
            }
        }
    }
    fun listarPedidos(peidoEviado:Int) :MutableList<PedidoFinalizado>{
        val query = "SELECT * FROM TBPedidosFinalizados  WHERE pedidoEnviado = ${peidoEviado}"
        val listPedidoFinalizado = mutableListOf<PedidoFinalizado>()
        val cursor = dbPedido.writableDatabase.rawQuery(query,null)

        while (cursor.moveToNext()){
            val pedidoId = cursor.getInt(0)
            val lojaId = cursor.getInt(1)
            val clienteId = cursor.getInt(2)
            val operadorLogistico = cursor.getString(3)
            val usuarioId = cursor.getInt(4)
            val uf = cursor.getString(5)
            val comissao = cursor.getFloat(6)
            val comissaoPorcentagem = cursor.getFloat(7)
            val marcasXComissoesId = cursor.getInt(8)
            val produtoCodigo = cursor.getInt(9)
            val barra = cursor.getString(10)
            val quantidade = cursor.getInt(11)
            val pf = cursor.getFloat(12)
            val valor = cursor.getDouble(13)
            val valorOriginal = cursor.getDouble(14)
            val grupoCodigo = cursor.getInt(15)
            val desconto = cursor.getDouble(16)
            val descontoOriginal = cursor.getDouble(17)
            val st = cursor.getFloat(18)
            val formalizacao = cursor.getString(19)
            val codListaPrecoSync = cursor.getInt(20)
            val valorTotal = cursor.getFloat(21)
            val nome = cursor.getString(22)
            val apontadorCodigo = cursor.getString(23)
            val nomeLoja = cursor.getString(24)
            val razaoSocial = cursor.getString(25)
            val cnpj = cursor.getString(26)
            val dataPedido = cursor.getString(27)
            val valorMinimoLoja = cursor.getFloat(28)
            val base64 = cursor.getString(29)
            val caixaPadrao = cursor.getInt(30)
            val pmc = cursor.getFloat(31)
            val qtdMinimaOperador = cursor.getInt(32)
            val qtdMaximaOperador = cursor.getInt(33)
            val anr = cursor.getInt(34)
            val pedidoEnviado = cursor.getInt(35)
            val formaDePagamento = cursor.getString(36)
            val numeroPedido = cursor.getString(37)
            val operadoresPedidos = cursor.getString(38)
            val formaPagamentoExclusiva = cursor.getInt(39)
            val chave = cursor.getString(40)
            val  justificativaANR=  cursor.getString(41)
            val  TipoLoja=  cursor.getInt(42)

            val pedido = PedidoFinalizado(
                pedidoId.toLong(),
                lojaId,
                clienteId,
                operadorLogistico,
                usuarioId,
                uf,
                comissao?.toDouble(),
                comissaoPorcentagem?.toDouble(),
                marcasXComissoesId,
                produtoCodigo,
                barra,
                quantidade,
                pf.toDouble(),
                valor,
                valorOriginal,
                grupoCodigo,
                desconto,
                descontoOriginal,
                st?.toDouble(),
                formalizacao,
                codListaPrecoSync,
                valorTotal?.toDouble(),
                nome,
                apontadorCodigo,
                nomeLoja,
                razaoSocial,
                cnpj,
                dataPedido,
                valorMinimoLoja?.toDouble(),
                base64,
                caixaPadrao,
                pmc?.toDouble(),
                qtdMinimaOperador,
                qtdMaximaOperador,
                anr != 0,
                pedidoEnviado != 0,
                formaDePagamento,
                numeroPedido,
                operadoresPedidos,
                formaPagamentoExclusiva,
                chave,
                justificativaANR,
                TipoLoja
            )
            listPedidoFinalizado.add(pedido)
        }
        return listPedidoFinalizado
    }
    fun listarPedidosProdutos(pedidoId:Long) : MutableList<ProdutosFinalizados>{
        val query = "SELECT * FROM TB_Produtos_Pedidos_Finalizado WHERE pedidoid = ${pedidoId}"
        val listaProdutosFinalizados  = mutableListOf<ProdutosFinalizados>()

        val cursor = dbPedido.writableDatabase.rawQuery(query,null)

        while (cursor.moveToNext()){
            val barra = cursor.getString(1)
            val produtoCodigo = cursor.getInt(2)
            val desconto = cursor.getDouble(3)
            val descontoOriginal = cursor.getDouble(4)
            val formalizacao = cursor.getString(5)
            val pf = cursor.getDouble(6)
            val quantidade = cursor.getInt(7)
            val valorRepasse = cursor.getDouble(8)
            val st = cursor.getString(9)
            val valor = cursor.getDouble(10)
            val NomeProduto = cursor.getString(11)
            val produtosFinalizados = ProdutosFinalizados(
                barra,
                produtoCodigo,
                desconto,
                descontoOriginal,
                formalizacao,
                pf,
                quantidade,
                valorRepasse,
                st,
                valor,
                NomeProduto
            )
            listaProdutosFinalizados.add(produtosFinalizados)
        }

        return listaProdutosFinalizados
    }

    fun somarTotalPedido(pedidoId:Long):Double{
        val query = "SELECT Valor FROM TB_Produtos_Pedidos_Finalizado WHERE pedidoid = ${pedidoId}"
        val cursor = dbPedido.writableDatabase.rawQuery(query,null)

        var valortotal = 0.0
        while (cursor.moveToNext()){
            val valor  = cursor.getDouble(0)
            valortotal = valor +valortotal
        }
        return valortotal
    }

    fun excluirItemPedido(pedidoID:Long, pedidoEnviado:Int){
        val queryCarrinho = "DELETE from TBPedidosFinalizados WHERE pedidoid = ${pedidoID} AND pedidoenviado = ${pedidoEnviado}"

        dbPedido.writableDatabase.execSQL(queryCarrinho)
    }

}
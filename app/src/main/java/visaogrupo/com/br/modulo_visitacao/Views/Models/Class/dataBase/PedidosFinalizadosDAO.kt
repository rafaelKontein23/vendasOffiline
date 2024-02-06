package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitProtudos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ValoresProgressiva

class PedidosFinalizadosDAO(context: Context) {
    val dbPedido = DataBaseHelber(context)
    fun uptadePedidoEnviado(peidoEnvia:Int, pedidoId :Int){
        val queryUp = "UPDATE TBPedidosFinalizados SET PedidoEnviado = '$peidoEnvia' WHERE PedidoID = $pedidoId"
        dbPedido.writableDatabase.execSQL(queryUp)
    }
    fun insert(carrinho:Carrinho, data:String,hora:String, chave:String,
               listaProdutduos:MutableList<Carrinho>,
               numeroPedido:String,opls:String, justificativaAnr :String,
               formaDePagamento:FormaDePagaemnto,
               RegraPrazo:Int, QuantidadeMaxima:Int){

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
            put("RegraPrazo",RegraPrazo)
            put("QtdMaximaOpl",QuantidadeMaxima)



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
    fun listarPedidos(pedidoEviado:Int) :MutableList<PedidoFinalizado>{

        val query ="SELECT \n" +
                "    PedidoID,\n" +
                "    loja_id,\n" +
                "    cliente_id,\n" +
                "    OperadorLogistigo,\n" +
                "    Usuario_id,\n" +
                "    UF,\n" +
                "    Comissao,\n" +
                "    ComissaoPorcentagem,\n" +
                "    MarcasXComissoes_id,\n" +
                "    Produto_codigo,\n" +
                "    0 AS kitCodigo,\n" +
                "    Barra,\n" +
                "    Quantidade,\n" +
                "    PF,\n" +
                "    0.0 AS De,\n" +
                "    0.0 AS Por,\n" +
                "    Desconto,\n" +
                "    DescontoOriginal,\n" +
                "    ST,\n" +
                "    formalizacao,\n" +
                "    CODLISTAPRECOSYNC,\n" +
                "    ValorTotal,\n" +
                "    Nome,\n" +
                "    Apontador_codigo,\n" +
                "    '' AS NomeKit,\n" +
                "    Valor,\n" +
                "    ValorOriginal,\n" +
                "    Grupo_Codigo,\n" +
                "    nomeLoja,\n" +
                "    razaosocial,\n" +
                "    cnpj,\n" +
                "    dataPedido,\n" +
                "    valorminimoLoja,\n" +
                "    base64,\n" +
                "    caixapadrao,\n" +
                "    pmc,\n" +
                "    Qtd_Minima_Operador,\n" +
                "    Qtd_Maxima_Operador,\n" +
                "    ANR,\n" +
                "    PedidoEnviado,\n" +
                "    formaDePagemento,\n" +
                "    NumeroPedido,\n" +
                "    OperadoresPedidos,\n" +
                "    FormaPagamentoExclusiva,\n" +
                "    Chave,\n" +
                "    justificativaANR,\n" +
                "    TipoLoja,\n" +
                "    RegraPrazo,\n" +
                "    QtdMaximaOpl,\n" +
                "   0 as Kit\n" +
                "FROM TBPedidosFinalizados WHERE PedidoEnviado = ${pedidoEviado} UNION \n" +
                "    SELECT PedidoID,    loja_id,\n" +
                "    cliente_id,\n" +
                "    OperadorLogistigo,\n" +
                "    Usuario_id,\n" +
                "    UF,\n" +
                "    0 as  Comissao,\n" +
                "    0 as    ComissaoPorcentagem,\n" +
                "    0 as    MarcasXComissoes_id,\n" +
                "    0 as    Produto_codigo,\n" +
                "    kitCodigo,\n" +
                "    '' as Barra,\n" +
                "    Quantidade,\n" +
                "    0.0 as PF,\n" +
                "    De,\n" +
                "    Por,\n" +
                "    Desconto,\n" +
                "    0.0 as DescontoOriginal,\n" +
                "    '' as ST,\n" +
                "    '' as formalizacao,\n" +
                "    CODLISTAPRECOSYNC,\n" +
                "    ValorTotal,\n" +
                "    '' as Nome,\n" +
                "    '' as Apontador_codigo,\n" +
                "    NomeKit,\n" +
                "    0.0 as Valor,\n" +
                "    0.0 as ValorOriginal,\n" +
                "     0 as Grupo_Codigo,\n" +
                "    nomeLoja,\n" +
                "    razaosocial,\n" +
                "    cnpj,\n" +
                "    dataPedido,\n" +
                "    valorminimoLoja,\n" +
                "    NULL AS base64,\n" +
                "    NULL AS caixapadrao,\n" +
                "    NULL AS pmc,\n" +
                "    Qtd_Minima_Operador,\n" +
                "    Qtd_Maxima_Operador,\n" +
                "    NULL AS ANR,\n" +
                "    PedidoEnviado,\n" +
                "    formaDePagemento,\n" +
                "    NumeroPedido,\n" +
                "    OperadoresPedidos,\n" +
                "    FormaPagamentoExclusiva,\n" +
                "    Chave,\n" +
                "    '' as justificativaANR,\n" +
                "    TipoLoja,\n" +
                "    RegraPrazo,\n" +
                "    QtdMaximaOpl,\n" +
                "    1 as Kit\n" +
                "FROM TBPedidosFinalizadosKit;"


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
            val kitCodigo = cursor.getInt(10)
            val barra = cursor.getString(11)
            val quantidade = cursor.getInt(12)
            val pf = cursor.getFloat(13)
            val de = cursor.getDouble(14)
            val por = cursor.getDouble(15)
            val desconto = cursor.getDouble(16)
            val descontoOriginal = cursor.getDouble(17)
            val st = cursor.getFloat(18)
            val formalizacao = cursor.getString(19)
            val codListaPrecoSync = cursor.getInt(20)
            val valorTotal = cursor.getFloat(21)
            val nome = cursor.getString(22)
            val apontadorCodigo = cursor.getString(23)
            val nomeKit = cursor.getString(24)
            val valor = cursor.getDouble(25)
            val valorOriginal = cursor.getDouble(26)
            val grupoCodigo = cursor.getInt(27)
            val nomeLoja = cursor.getString(28)
            val razaoSocial = cursor.getString(29)
            val cnpj = cursor.getString(30)
            val dataPedido = cursor.getString(31)
            val valorMinimoLoja = cursor.getFloat(32)
            val base64 = cursor.getString(33)
            val caixaPadrao = cursor.getInt(34)
            val pmc = cursor.getFloat(35)
            val qtdMinimaOperador = cursor.getInt(36)
            val qtdMaximaOperador = cursor.getInt(37)
            val anr = cursor.getInt(38)
            val pedidoEnviado = cursor.getInt(39)
            val formaDePagamento = cursor.getString(40)
            val numeroPedido = cursor.getString(41)
            val operadoresPedidos = cursor.getString(42)
            val formaPagamentoExclusiva = cursor.getInt(43)
            val chave = cursor.getString(44)
            val  justificativaANR=  cursor.getString(45)
            val  TipoLoja=  cursor.getInt(46)
            val regraPrazo = cursor.getInt(47)
            val quantidadeMaxima = cursor.getInt(48)
            val kit = cursor.getInt(49)


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
                TipoLoja,
                regraPrazo,
                quantidadeMaxima,
                kitCodigo,
                de,
                por,
                nomeKit,
                kit
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
    fun atualizaPedidoKit(pedidoId: Int, valorTotal:Double, quantidade:Int){
        val queryAtualiza = "UPDATE TBPedidosFinalizadosKit SET ValorTotal = ${valorTotal}, Quantidade = ${quantidade}  WHERE PedidoID = ${pedidoId};"
        dbPedido.writableDatabase.execSQL(queryAtualiza)
    }

    fun excluirItemPedio(pedidoId: Int){
        val whereClause = "PedidoID = ${pedidoId}"

        dbPedido.writableDatabase.delete("TBPedidosFinalizadosKit", whereClause,null)

    }

    fun buscaPedidoValores(produtoCodigo:Int, pedioId: Int ):ValoresProgressiva?{
        var valoresProgressiva: ValoresProgressiva? = null
        val  query = "SELECT PF, desconto,valor  FROM TB_Produtos_Pedidos_Finalizado Pedidos_Finalizado WHERE Pedidos_Finalizado.Produto_codigo = ${produtoCodigo} AND Pedidos_Finalizado.PedidoID  = ${pedioId}"
        val  cursor = dbPedido.writableDatabase.rawQuery(query,null)

        while (cursor.moveToNext()){
            val valorProgressiva = cursor.getDouble(0)
            val desconto = cursor.getDouble(1)
            val valorTotal = cursor.getString(2)
            valoresProgressiva = ValoresProgressiva(desconto.toString(),valorTotal,valorProgressiva.toString())

            break
        }
        cursor.close()

        return valoresProgressiva
    }
    fun listaPedidoKit(pedidoId:Int):MutableList<KitTituloPreco>{
        val kitTituloPrecolista = mutableListOf<KitTituloPreco>()
        val listaKitProdutos = mutableListOf<KitProtudos>()
        val query = "SELECT NomeKit, kitCodigo,De, Por, Quantidade FROM TBPedidosFinalizadosKit WHERE pedidoid = ${pedidoId}"
        val cursor = dbPedido.writableDatabase.rawQuery(query,null)
        while (cursor.moveToNext()){
              val nomeKit =    cursor.getString(0)
              val kitCodigo =  cursor.getInt(1)
              val de =         cursor.getDouble(2)
              val  por =       cursor.getDouble(3)
              val quantidade = cursor.getInt(4)
              val  kitTituloPreco = KitTituloPreco(nomeKit,kitCodigo,de,por, estaNoCarrinho = 0, quantidade = quantidade)
              kitTituloPrecolista!!.add(kitTituloPreco)
        }
        val  queryProtudosKit = "SELECT * FROM TB_ProdutosKit_Pedidos_Finalizado Kit_Pedidos_Finalizado \n" +
                "LEFT JOIN TB_Imagens imagens On imagens.barra = Kit_Pedidos_Finalizado.barra\n" +
                "WHERE pedidoid = ${pedidoId} "
        val cursorProtudoKit = dbPedido.writableDatabase.rawQuery(queryProtudosKit,null)

        while (cursorProtudoKit.moveToNext()){
               val  barra = cursorProtudoKit.getString(1)
               val  Produto_codigo = cursorProtudoKit.getString(2)
               val  Desconto = cursorProtudoKit.getDouble(3)
               val  PF = cursorProtudoKit.getDouble(4)
               val  Quantidade = cursorProtudoKit.getInt(5)
               val  NomeProduto = cursorProtudoKit.getString(6)
               val base64 = cursorProtudoKit.getString(7)
               val kitProdutos = KitProtudos(kitTituloPrecolista!![0].kitId,Produto_codigo,NomeProduto,"", Desconto , Quantidade,"",PF,barra,base64)
               listaKitProdutos.add(kitProdutos)
        }
        kitTituloPrecolista!![0].listaKitProdutos = listaKitProdutos
        return kitTituloPrecolista!!
    }

    fun listaProdutosPedidokit(pedidoId:Int,kitCodigo:Int):MutableList<KitProtudos>{
        val  queryProtudosKit = "SELECT * FROM TB_ProdutosKit_Pedidos_Finalizado Kit_Pedidos_Finalizado \n" +
                "LEFT JOIN TB_Imagens imagens On imagens.barra = Kit_Pedidos_Finalizado.barra\n" +
                "WHERE pedidoid = ${pedidoId} "
        val cursorProtudoKit = dbPedido.writableDatabase.rawQuery(queryProtudosKit,null)
        val listaKitProdutos = mutableListOf<KitProtudos>()


        while (cursorProtudoKit.moveToNext()){
            val  barra = cursorProtudoKit.getString(1)
            val  Produto_codigo = cursorProtudoKit.getString(2)
            val  Desconto = cursorProtudoKit.getDouble(3)
            val  PF = cursorProtudoKit.getDouble(4)
            val  Quantidade = cursorProtudoKit.getInt(5)
            val  NomeProduto = cursorProtudoKit.getString(6)
            val base64 = cursorProtudoKit.getString(7)
            val kitProdutos = KitProtudos(kitCodigo,Produto_codigo,NomeProduto,"", Desconto , Quantidade,"",PF,barra,base64)
            listaKitProdutos.add(kitProdutos)
        }
        return listaKitProdutos
    }
     fun  adicionaItemNoPedido(pedidoID:Int,carrinh:Carrinho){

             val valoresProdutosPedidos = ContentValues()
             valoresProdutosPedidos.put("NomeProduto",carrinh.nomeProduto)
             valoresProdutosPedidos. put("PedidoID", pedidoID)
             valoresProdutosPedidos. put("Barra", carrinh.barra)
             valoresProdutosPedidos. put("Produto_codigo", carrinh.produtoCodigo)
             valoresProdutosPedidos. put("Desconto", carrinh.desconto)
             valoresProdutosPedidos. put("DescontoOriginal", carrinh.descontoOriginal)
             valoresProdutosPedidos. put("formalizacao", carrinh.formalizacao)
             valoresProdutosPedidos. put("PF", carrinh.pf)
             valoresProdutosPedidos. put("Quantidade", carrinh.quantidade)
             valoresProdutosPedidos. put("ValorRepasse", carrinh.valor)
             valoresProdutosPedidos. put("ST", carrinh.st)
             valoresProdutosPedidos. put("Valor", carrinh.valortotal)
             try {
                 dbPedido.writableDatabase.insertOrThrow("TB_Produtos_Pedidos_Finalizado", null,valoresProdutosPedidos)

             }catch (e:SQLiteException){
                 println("Erroo ${e.message}")
             }

     }
    fun atualizaProdutoPedidoValores(pedidoId:Int, produtoCodigo:Int, carrinho:Carrinho){
        val conteudos=  ContentValues()

        conteudos.put("Desconto",carrinho.desconto)
        conteudos.put("Quantidade",carrinho.quantidade)
        conteudos.put("Valor",carrinho.valortotal)



        dbPedido.writableDatabase.update("TB_Produtos_Pedidos_Finalizado",conteudos,
            "PedidoID = ${pedidoId} AND Produto_codigo = ${produtoCodigo}", null)
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
    fun countarItensPedidos(pedidoId:Int):Int{
        val query = "SELECT COUNT (*) FROM TB_Produtos_Pedidos_Finalizado WHERE pedidoid = ${pedidoId}"
        val cursor = dbPedido.readableDatabase.rawQuery(query, null)

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        return count
    }

    fun excluirItemPedido(pedidoID:Long, pedidoEnviado:Int){
        val queryCarrinho = "DELETE from TBPedidosFinalizados WHERE pedidoid = ${pedidoID} AND pedidoenviado = ${pedidoEnviado}"

        dbPedido.writableDatabase.execSQL(queryCarrinho)
    }
    fun excluirItemPedidoProduto(pedidoID:Long, produtosCodigo:Int){
        val queryCarrinho = "DELETE from TB_Produtos_Pedidos_Finalizado WHERE pedidoid = ${pedidoID} AND Produto_codigo = ${produtosCodigo}"

        dbPedido.writableDatabase.execSQL(queryCarrinho)
    }

    fun excluirItemPedidoKit(pedidoID:Long, pedidoEnviado:Int){
        val queryCarrinho = "DELETE from TBPedidosFinalizadosKit WHERE PedidoID = ${pedidoID} AND PedidoEnviado = ${pedidoEnviado}"

        dbPedido.writableDatabase.execSQL(queryCarrinho)
    }
    fun excluirItemPedidoProdutoKit(pedidoID:Long, produtosCodigo:Int){
        val queryCarrinho = "DELETE from TB_ProdutosKit_Pedidos_Finalizado WHERE PedidoID = ${pedidoID} AND Produto_codigo = ${produtosCodigo}"

        dbPedido.writableDatabase.execSQL(queryCarrinho)
    }
    fun atualizarItem(pedidoId:Int,operadorLogistigo:String, formaDePagaemtocap:String, formaDePagaemto: FormaDePagaemnto){
        val conteudos=  ContentValues()
        if (!operadorLogistigo.isEmpty()){
            conteudos.put("OperadorLogistigo",operadorLogistigo)
        }
        if (!formaDePagaemtocap.isEmpty()){
            conteudos.put("formaDePagemento",formaDePagaemto.Cod_FormaPgto)
            conteudos.put("FormaPagamentoExclusiva",formaDePagaemto.exlusiva)
        }

        dbPedido.writableDatabase.update("TBPedidosFinalizados",conteudos,
            "PedidoID = ${pedidoId}", null) // o Interrogação serve para
    }
}
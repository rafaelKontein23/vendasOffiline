package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitProtudos

class CarrinhoKitDAO(context:Context) {
    val dbCarrinhoKit = DataBaseHelber(context).writableDatabase

    fun insert(carrinhoKit :CarrinhoKit){
        val valoresContentCarrinho = ContentValues()

        try {
            valoresContentCarrinho.put("loja_id",carrinhoKit.lojaId)
            valoresContentCarrinho.put("cliente_id",carrinhoKit.clienteId)
            valoresContentCarrinho.put("OperadorLogistigo",carrinhoKit.operadorLogistigo)
            valoresContentCarrinho.put("Usuario_id",carrinhoKit.usuarioId)
            valoresContentCarrinho.put("UF",carrinhoKit.uf)
            valoresContentCarrinho.put("Comissao",carrinhoKit.comissao)
            valoresContentCarrinho.put("ComissaoPorcentagem",carrinhoKit.comissaoPorcentagem)
            valoresContentCarrinho.put("MarcasXComissoes_id",carrinhoKit.marcasXComissoesId)
            valoresContentCarrinho.put("kitCodigo",carrinhoKit.kitCodigo)
            valoresContentCarrinho.put("Quantidade",carrinhoKit.quantidade)
            valoresContentCarrinho.put("Por",carrinhoKit.por)
            valoresContentCarrinho.put("ValorDe",carrinhoKit.valorDe)
            valoresContentCarrinho.put("Grupo_Codigo",carrinhoKit.grupoCodigo)
            valoresContentCarrinho.put("Desconto",carrinhoKit.desconto)
            valoresContentCarrinho.put("CODLISTAPRECOSYNC",carrinhoKit.codListaPrecoSync)
            valoresContentCarrinho.put("ValorTotal",carrinhoKit.valortotal)
            valoresContentCarrinho.put("NomeKIT",carrinhoKit.nomeKit)
            valoresContentCarrinho.put("nomeLoja",carrinhoKit.nomeLoja)
            valoresContentCarrinho.put("razaosocial",carrinhoKit.razaoSolcial)
            valoresContentCarrinho.put("cnpj",carrinhoKit.razaoSolcial)
            valoresContentCarrinho.put("dataPedido",carrinhoKit.data)
            valoresContentCarrinho.put("valorminimoLoja",carrinhoKit.valorMinimoLoja)
            valoresContentCarrinho.put("Qtd_Minima_Operador",carrinhoKit.QtdMinima_Operador)
            valoresContentCarrinho.put("Qtd_Maxima_Operador",carrinhoKit.QtdMaxima_Operador)
            valoresContentCarrinho.put("FormaPagamentoExclusiva",carrinhoKit.FormaPagamentoExclusiva)
            valoresContentCarrinho.put("RegraPrazo",carrinhoKit.RegraPrazo)
            valoresContentCarrinho.put("LojaTipo",carrinhoKit.LojaTipo)
            valoresContentCarrinho.put("PERCENTUALRepasse",carrinhoKit.porecentagem)

            for (i in carrinhoKit.listProdutoKit!!){
                val valoresContentProdutos = ContentValues()
                valoresContentProdutos.put("loja_id",carrinhoKit.lojaId)
                valoresContentProdutos.put("cliente_id",carrinhoKit.clienteId)
                valoresContentProdutos.put("kitCodigo",carrinhoKit.kitCodigo)
                valoresContentProdutos.put("produtoCodigo",i.produtoCodigo)
                valoresContentProdutos.put("produtoNome",i.produtoNome)
                valoresContentProdutos.put("fabricante",i.fabricante)
                valoresContentProdutos.put("desconto",i.desconto)
                valoresContentProdutos.put("quantidade",i.quantidade)
                valoresContentProdutos.put("valorTotal",i.valorTotal)
                valoresContentProdutos.put("barra",i.barra)

                dbCarrinhoKit.insertOrThrow("TB_carrinhoProdutoKit",null,valoresContentProdutos)
            }

            dbCarrinhoKit.insertOrThrow("TB_CarrinhoKit", null,valoresContentCarrinho)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }



    fun verificaExiste(carrinhoKit: CarrinhoKit){
        val queryVerifica = "SELECT * FROM TB_CarrinhoKit WHERE loja_id = ${carrinhoKit.lojaId}" +
                " AND cliente_id = ${carrinhoKit.clienteId} AND kitCodigo = ${carrinhoKit.kitCodigo}"
        val curso = dbCarrinhoKit.rawQuery(queryVerifica,null)

        if (curso.moveToNext()){
            atualizaKit(carrinhoKit)
        }else{
            insert(carrinhoKit)
        }

    }

    fun PegarValor(carrinhoKit: CarrinhoKit) :Pair<Double,Int>{
        val queryVerifica = "SELECT valortotal ,quantidade FROM TB_CarrinhoKit WHERE loja_id = ${carrinhoKit.lojaId}" +
                " AND cliente_id = ${carrinhoKit.clienteId}"
        val cursor = dbCarrinhoKit.rawQuery(queryVerifica,null)
        var valorTotal = 0.0
        var quantidade  = 0
        while (cursor.moveToNext()){
             valorTotal += cursor.getDouble(0)
             quantidade += cursor.getInt(1)

        }
        return Pair(valorTotal,quantidade)

    }

    fun atualizaKit(carrinhoKit :CarrinhoKit){
        val valoresContentCarrinho = ContentValues()

        try {
            valoresContentCarrinho.put("loja_id",carrinhoKit.lojaId)
            valoresContentCarrinho.put("cliente_id",carrinhoKit.clienteId)
            valoresContentCarrinho.put("OperadorLogistigo",carrinhoKit.operadorLogistigo)
            valoresContentCarrinho.put("Usuario_id",carrinhoKit.usuarioId)
            valoresContentCarrinho.put("UF",carrinhoKit.uf)
            valoresContentCarrinho.put("Comissao",carrinhoKit.comissao)
            valoresContentCarrinho.put("ComissaoPorcentagem",carrinhoKit.comissaoPorcentagem)
            valoresContentCarrinho.put("MarcasXComissoes_id",carrinhoKit.marcasXComissoesId)
            valoresContentCarrinho.put("kitCodigo",carrinhoKit.kitCodigo)
            valoresContentCarrinho.put("Quantidade",carrinhoKit.quantidade)
            valoresContentCarrinho.put("Por",carrinhoKit.por)
            valoresContentCarrinho.put("ValorDe",carrinhoKit.valorDe)
            valoresContentCarrinho.put("Grupo_Codigo",carrinhoKit.grupoCodigo)
            valoresContentCarrinho.put("Desconto",carrinhoKit.desconto)
            valoresContentCarrinho.put("CODLISTAPRECOSYNC",carrinhoKit.codListaPrecoSync)
            valoresContentCarrinho.put("ValorTotal",carrinhoKit.valortotal)
            valoresContentCarrinho.put("NomeKIT",carrinhoKit.nomeKit)
            valoresContentCarrinho.put("nomeLoja",carrinhoKit.nomeLoja)
            valoresContentCarrinho.put("razaosocial",carrinhoKit.razaoSolcial)
            valoresContentCarrinho.put("cnpj",carrinhoKit.razaoSolcial)
            valoresContentCarrinho.put("dataPedido",carrinhoKit.data)
            valoresContentCarrinho.put("valorminimoLoja",carrinhoKit.valorMinimoLoja)
            valoresContentCarrinho.put("Qtd_Minima_Operador",carrinhoKit.QtdMinima_Operador)
            valoresContentCarrinho.put("Qtd_Maxima_Operador",carrinhoKit.QtdMaxima_Operador)
            valoresContentCarrinho.put("FormaPagamentoExclusiva",carrinhoKit.FormaPagamentoExclusiva)
            valoresContentCarrinho.put("RegraPrazo",carrinhoKit.RegraPrazo)
            valoresContentCarrinho.put("LojaTipo",carrinhoKit.LojaTipo)
            valoresContentCarrinho.put("PERCENTUALRepasse",carrinhoKit.porecentagem)

            for (i in carrinhoKit.listProdutoKit!!){
                val valoresContentProdutos = ContentValues()
                valoresContentProdutos.put("loja_id",carrinhoKit.lojaId)
                valoresContentProdutos.put("cliente_id",carrinhoKit.clienteId)
                valoresContentProdutos.put("kitCodigo",carrinhoKit.kitCodigo)
                valoresContentProdutos.put("produtoCodigo",i.produtoCodigo)
                valoresContentProdutos.put("produtoNome",i.produtoNome)
                valoresContentProdutos.put("fabricante",i.fabricante)
                valoresContentProdutos.put("desconto",i.desconto)
                valoresContentProdutos.put("quantidade",i.quantidade)
                valoresContentProdutos.put("valorTotal",i.valorTotal)
                valoresContentProdutos.put("barra",i.barra)

                dbCarrinhoKit.update("TB_carrinhoProdutoKit",valoresContentProdutos,
                    "loja_id = ${carrinhoKit.lojaId} AND cliente_id = ${carrinhoKit.clienteId}" +
                            " AND kitCodigo = ${carrinhoKit.kitCodigo}" +
                            " AND produtoCodigo = ${i.produtoCodigo}", null)
            }
            dbCarrinhoKit.update("TB_CarrinhoKit", valoresContentCarrinho,
                "loja_id = ${carrinhoKit.lojaId} AND cliente_id = ${carrinhoKit.clienteId} AND kitCodigo = ${carrinhoKit.kitCodigo}",null)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun excluirItem(carrinhoKit: CarrinhoKit){
        val queryDelete = "DELETE FROM TB_CarrinhoKit WHERE loja_id = ${carrinhoKit.lojaId} " +
                "AND cliente_id = ${carrinhoKit.clienteId}" +
                " AND kitCodigo = ${carrinhoKit.kitCodigo} "
        dbCarrinhoKit.execSQL(queryDelete)

        for (i in carrinhoKit.listProdutoKit!!){
            val queryDeleteProtudo = "DELETE FROM TB_carrinhoProdutoKit WHERE loja_id = ${carrinhoKit.lojaId} " +
                    "AND cliente_id = ${carrinhoKit.clienteId}" +
                    " AND kitCodigo = ${carrinhoKit.kitCodigo} AND produtoCodigo = '${i.produtoCodigo}'"

            dbCarrinhoKit.execSQL(queryDeleteProtudo)
        }
    }
    fun buscaCarrinhoKit(query:String):MutableList<CarrinhoKit>{
        val listaCarrinhoKit = mutableListOf<CarrinhoKit>()
        val cursor = dbCarrinhoKit.rawQuery(query, null)
        var  count =0
        while (cursor.moveToNext()){
            count +=1
            val lojaId = cursor.getInt(0)
            val clienteId = cursor.getInt(1)
            val operadorLogisgo = cursor.getString(2)
            val userId = cursor.getInt(3)
            val uf = cursor.getString(4)
            val kitCodigo = cursor.getInt(8)
            val quantidade  = cursor.getInt(9)
            val por = cursor.getDouble(10)
            val de = cursor.getDouble(11)
            val desconto = cursor.getDouble(13)
            val codPrecoSyc = cursor.getInt(14)
            val valorTotal = cursor.getDouble(15)
            val nomeKit = cursor.getString(16)
            val nomeLoja = cursor.getString(17)
            val razaoSocial = cursor.getString(18)
            val cnpj = cursor.getString(19)

            val dataPedido = cursor.getString(20)
            val qtdMinimaOperador = cursor.getInt(22)
            val qtdmaximaOperador = cursor.getInt(23)
            val formaDePagamentoExclusiva = cursor.getInt(24)
            val regraPrazo = cursor.getInt(25)
            val lojaTipo = cursor.getInt(26)

            val  carrinhoKit = CarrinhoKit(lojaId,clienteId,
                kitCodigo,
                operadorLogisgo,userId,uf,
                0.0,
                0.0,
                0,
                quantidade,
                por,de,0,desconto,
                codPrecoSyc,
                valorTotal,
                nomeKit,
                nomeLoja,
                razaoSocial,
                cnpj,
                dataPedido,
                0.0,
                formaDePagamentoExclusiva,
                qtdMinimaOperador,
                qtdmaximaOperador,
                regraPrazo,
                lojaTipo,
                0.0, numerPedido = count)
            listaCarrinhoKit.add(carrinhoKit)

        }
        return listaCarrinhoKit
    }

    fun buscaItensProdutoCarrinhoKit(query:String,kitId:Int):MutableList<KitProtudos>{
        val listaKitProdutos = mutableListOf<KitProtudos>()
        val cursor = dbCarrinhoKit.rawQuery(query,null)

        while (cursor.moveToNext()){
            val protudoCodigo = cursor.getString(3)
            val protudoNome =   cursor.getString(4)
            val fabricante =    cursor.getString(5)
            val desconto =      cursor.getDouble(6)
            val quantidade =    cursor.getInt(7)
            val valorTotal =    cursor.getDouble(8)
            val barra =         cursor.getString(9)
            val imagembase64 =  cursor.getString(10)
            val protudoCarrinhoKit = KitProtudos(kitId,protudoCodigo, protudoNome,
                fabricante,desconto,quantidade,"", valorTotal,barra ,imagembase64 )
            listaKitProdutos.add(protudoCarrinhoKit)


        }
        return listaKitProdutos
    }
}
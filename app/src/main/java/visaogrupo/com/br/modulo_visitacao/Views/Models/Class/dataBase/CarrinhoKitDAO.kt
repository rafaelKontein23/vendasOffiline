package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CarrinhoKit

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

            for (i in carrinhoKit.listProdutoKit){
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

            for (i in carrinhoKit.listProdutoKit){
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

        for (i in carrinhoKit.listProdutoKit){
            val queryDeleteProtudo = "DELETE FROM TB_carrinhoProdutoKit WHERE loja_id = ${carrinhoKit.lojaId} " +
                    "AND cliente_id = ${carrinhoKit.clienteId}" +
                    " AND kitCodigo = ${carrinhoKit.kitCodigo} AND produtoCodigo = '${i.produtoCodigo}'"

            dbCarrinhoKit.execSQL(queryDeleteProtudo)
        }
    }
}
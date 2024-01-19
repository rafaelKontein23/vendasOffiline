package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagExclusiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual

class CarrinhoDAO (context:Context) {
    val  dbCarrinho = DataBaseHelber(context).writableDatabase
    fun insertCarrinho (carrinho: Carrinho){

        val  daataformat = DataAtual()
        val  data = daataformat.recuperaData()
        val valoresCarrinhos = ContentValues()

        valoresCarrinhos.put("loja_id",carrinho.lojaId)
        valoresCarrinhos.put("cliente_id",carrinho.clienteId)
        valoresCarrinhos.put("OperadorLogistigo",carrinho.operadorLogistigo)
        valoresCarrinhos.put("Usuario_id",carrinho.usuarioId)
        valoresCarrinhos.put("UF",carrinho.uf)
        valoresCarrinhos.put("Comissao",carrinho.comissao)
        valoresCarrinhos.put("ComissaoPorcentagem",carrinho.comissaoPorcentagem)
        valoresCarrinhos.put("MarcasXComissoes_id",carrinho.marcasXComissoesId)
        valoresCarrinhos.put("Produto_codigo",carrinho.produtoCodigo)
        valoresCarrinhos.put("Barra",carrinho.barra)
        valoresCarrinhos.put("Quantidade",carrinho.quantidade)
        valoresCarrinhos.put("PF",carrinho.pf)
        valoresCarrinhos.put("Valor",carrinho.valor)
        valoresCarrinhos.put("ValorOriginal",carrinho.valorOriginal)
        valoresCarrinhos.put("Grupo_Codigo",carrinho.grupoCodigo)
        valoresCarrinhos.put("cliente_id",carrinho.clienteId)
        valoresCarrinhos.put("Desconto",carrinho.desconto)
        valoresCarrinhos.put("DescontoOriginal",carrinho.descontoOriginal)
        valoresCarrinhos.put("ST",carrinho.st)
        valoresCarrinhos.put("formalizacao",carrinho.formalizacao)
        valoresCarrinhos.put("CODLISTAPRECOSYNC",carrinho.codListaPrecoSync)
        valoresCarrinhos.put("ValorTotal",carrinho.valortotal)
        valoresCarrinhos.put("Nome",carrinho.nomeProduto)
        valoresCarrinhos.put("nomeLoja",carrinho.nomeLoja)
        valoresCarrinhos.put("razaosocial",carrinho.razaoSolcial)
        valoresCarrinhos.put("cnpj",carrinho.cnpj)
        valoresCarrinhos.put("dataPedido",data)
        valoresCarrinhos.put("valorminimoLoja",carrinho.valorMinimoLoja)
        valoresCarrinhos.put("base64",carrinho.base64)
        valoresCarrinhos.put("caixapadrao",carrinho.caixapadrao)
        valoresCarrinhos.put("pmc",carrinho.pmc)
        valoresCarrinhos.put("FormaPagamentoExclusiva", carrinho.FormaPagamentoExclusiva)
        valoresCarrinhos.put("Qtd_Minima_Operador",carrinho.QtdMinima_Operador)
        valoresCarrinhos.put("Qtd_Maxima_Operador",carrinho.QtdMaxima_Operador)
        valoresCarrinhos.put("ANR",carrinho.Anr)
        valoresCarrinhos.put("RegraPrazo",carrinho.RegraPrazo)
        valoresCarrinhos.put("LojaTipo",carrinho.LojaTipo)
        valoresCarrinhos.put("PERCENTUALRepasse",carrinho.porecentagem)


        dbCarrinho.insert("TB_Carrinho",null,valoresCarrinhos)
    }

    fun excluirItem(loja_id :Int,cliente_id:Int,produto_codigo:Int){
        val  where = "loja_id = ${loja_id} AND cliente_id = ${cliente_id} AND produto_codigo = ${produto_codigo} "
        dbCarrinho.delete("TB_Carrinho",where,null)
    }
    fun atualizaItemCarrinho( carrinho: Carrinho){
        val  daataformat = DataAtual()
        val  data = daataformat.recuperaData()
        val valoresCarrinhos=  ContentValues()
        valoresCarrinhos.put("loja_id",carrinho.lojaId)
        valoresCarrinhos.put("cliente_id",carrinho.clienteId)
        valoresCarrinhos.put("OperadorLogistigo",carrinho.operadorLogistigo)
        valoresCarrinhos.put("Usuario_id",carrinho.usuarioId)
        valoresCarrinhos.put("UF",carrinho.uf)
        valoresCarrinhos.put("Comissao",carrinho.comissao)
        valoresCarrinhos.put("ComissaoPorcentagem",carrinho.comissaoPorcentagem)
        valoresCarrinhos.put("MarcasXComissoes_id",carrinho.marcasXComissoesId)
        valoresCarrinhos.put("Produto_codigo",carrinho.produtoCodigo)
        valoresCarrinhos.put("Barra",carrinho.barra)
        valoresCarrinhos.put("Quantidade",carrinho.quantidade)
        valoresCarrinhos.put("PF",carrinho.pf)
        valoresCarrinhos.put("Valor",carrinho.valor)
        valoresCarrinhos.put("ValorOriginal",carrinho.valorOriginal)
        valoresCarrinhos.put("Grupo_Codigo",carrinho.grupoCodigo)
        valoresCarrinhos.put("cliente_id",carrinho.clienteId)
        valoresCarrinhos.put("Desconto",carrinho.desconto)
        valoresCarrinhos.put("DescontoOriginal",carrinho.descontoOriginal)
        valoresCarrinhos.put("ST",carrinho.st)
        valoresCarrinhos.put("formalizacao",carrinho.formalizacao)
        valoresCarrinhos.put("CODLISTAPRECOSYNC",carrinho.codListaPrecoSync)
        valoresCarrinhos.put("ValorTotal",carrinho.valortotal)
        valoresCarrinhos.put("Nome",carrinho.nomeProduto)
        valoresCarrinhos.put("nomeLoja",carrinho.nomeLoja)
        valoresCarrinhos.put("razaosocial",carrinho.razaoSolcial)
        valoresCarrinhos.put("cnpj",carrinho.cnpj)
        valoresCarrinhos.put("dataPedido",data)
        valoresCarrinhos.put("valorminimoLoja",carrinho.valorMinimoLoja)
        valoresCarrinhos.put("base64",carrinho.base64)
        valoresCarrinhos.put("caixapadrao",carrinho.caixapadrao)
        valoresCarrinhos.put("pmc",carrinho.pmc)
        valoresCarrinhos.put("FormaPagamentoExclusiva", carrinho.FormaPagamentoExclusiva)
        valoresCarrinhos.put("Qtd_Minima_Operador",carrinho.QtdMinima_Operador)
        valoresCarrinhos.put("Qtd_Maxima_Operador",carrinho.QtdMaxima_Operador)
        valoresCarrinhos.put("ANR",carrinho.Anr)
        valoresCarrinhos.put("RegraPrazo",carrinho.RegraPrazo)
        valoresCarrinhos.put("LojaTipo",carrinho.LojaTipo)

        dbCarrinho.update("TB_Carrinho",valoresCarrinhos,
            "loja_id = ${carrinho.lojaId} AND cliente_id = ${carrinho.clienteId} AND Produto_codigo = ${carrinho.produtoCodigo}",null)
    }

    fun countarItenscarrinho(query:String):Int{
       val  curso = dbCarrinho.rawQuery(query,null,null)
        var  count = 0
       while (curso.moveToNext()){
            count +=1
       }

      curso.close()

      return count

    }

    fun buscaProgressivavalor(produtoCodigo:Int):String{
        val  query = "SELECT valor, desconto FROM TB_Carrinho carrinho WHERE carrinho.produto_codigo = ${produtoCodigo}"
        val  cursor = dbCarrinho.rawQuery(query,null)
        var valores =""
        while (cursor.moveToNext()){
            val valorProgressiva = cursor.getDouble(0)
            val desconto = cursor.getDouble(1)
            valores = "R$ ${valorProgressiva}|  ${desconto} %"
            break
        }
        cursor.close()

        return valores
    }

    fun quantidadeItens(cliente_id: Int,loja_id: Int):Int{
        val query ="SELECT SUM(quantidade) AS total " +
                "FROM TB_Carrinho " +
                "WHERE cliente_id = ${cliente_id} AND loja_id = ${loja_id}"

        val  contar = dbCarrinho.rawQuery(query,null)
        var countagem = 0
        if (contar.moveToFirst()) {
            countagem = contar.getInt(0)
        }

        contar.close()

        return  countagem
    }

    fun listaritensCarrinho (loja_id: Int,cliente_id: Int):MutableList<Carrinho>{

        val  query = "SELECT * FROM TB_Carrinho WHERE loja_id = ${loja_id} and cliente_id = ${cliente_id}  "
        val  cursor = dbCarrinho.rawQuery(query,null)
        val listaProdutosCArrinhos : MutableList<Carrinho> = mutableListOf<Carrinho>()



        while (cursor.moveToNext()){
            val  loja_id  = cursor.getInt(0)
            val cliente_id = cursor.getInt(1)
            val opf = cursor.getString(2)
            val usuario_if = cursor.getInt(3)
            val UF = cursor.getString(4)
            val Comiisao = cursor.getDouble(5)
            val Comississaocporcentafgem = cursor.getDouble(6)
            val  marcaXCoissao_id = cursor.getInt(7)
            val  produto_codigo = cursor.getInt(8)
            val  barra = cursor.getString(9)
            val quantidade = cursor.getInt(10)
            val PF = cursor.getDouble(11)
            val valor = cursor.getDouble(12)
            val valoorOriginal  = cursor.getDouble(13)
            val  grupo_codigo = cursor.getInt(14)
            val desconto = cursor.getDouble(15)
            val  descontoOriginal = cursor.getDouble(16)
            val  St = cursor.getDouble(17)
            val  formalizacao = cursor.getString(18)
            val codListaOrecosSync =cursor.getInt(19)
            val valorTotal = cursor.getDouble(20)
            val nome = cursor.getString(21)
            val nomeLoja =  cursor.getString(23)
            val razaoSocial = cursor.getString(24)
            val cnpj = cursor.getString(25)
            val apontador =""
            val data  = cursor.getString(26)
            val valoloja = cursor.getDouble(27)
            val base64 = cursor.getString(28)
            val caixapadrao = cursor.getInt(29)
            val pmc  = cursor.getDouble(30)
            val Qtd_Minima_Operador  = cursor.getInt(31)
            val Qtd_Maxima_Operador  = cursor.getInt(32)
            val anr  = cursor.getInt(33)
            val RegraPrazo  = cursor.getInt(35)
            val FormaDePagExclusiva = cursor.getInt(34)
            val lojaTipo = cursor.getInt(36)
            val porcentagem = cursor.getDouble(37)
            val  carrinho= Carrinho(loja_id,cliente_id,produto_codigo,
                opf,usuario_if,UF,
                Comiisao,
                Comississaocporcentafgem,
                marcaXCoissao_id,
                barra,quantidade,
                PF,valor,valoorOriginal,
                grupo_codigo,desconto,
                descontoOriginal,St,formalizacao,
                codListaOrecosSync,apontador,
                valorTotal,nome,nomeLoja,razaoSocial,cnpj,data,
                valoloja,base64,pmc,FormaDePagExclusiva,caixapadrao,
                Qtd_Minima_Operador,Qtd_Maxima_Operador,anr,RegraPrazo,lojaTipo,0,porcentagem)

            listaProdutosCArrinhos.add(carrinho)
        }
        cursor.close()

        return listaProdutosCArrinhos
    }
    fun excluirItemCarrinho(cliente_id: Int,loja_id: Int){
        val queryCarrinho = "DELETE from TB_Carrinho WHERE cliente_id = ${cliente_id} And loja_id = ${loja_id}"
        dbCarrinho.execSQL(queryCarrinho)
    }
}
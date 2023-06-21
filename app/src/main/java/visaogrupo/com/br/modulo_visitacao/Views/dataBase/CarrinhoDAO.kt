package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho

class CarrinhoDAO (context:Context) {
    val  dbCarrinho = DataBaseHelber(context).writableDatabase
    fun insertCarrinho (carrinho: Carrinho){
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
        dbCarrinho.insert("TB_Carrinho",null,valoresCarrinhos)
    }

    fun excluirItem(loja_id :Int,cliente_id:Int,produto_codigo:Int){
        val  where = "loja_id = ${loja_id} AND cliente_id = ${cliente_id} AND produto_codigo = ${produto_codigo} "
        dbCarrinho.delete("TB_Carrinho",where,null)
    }

    fun countarItenscarrinho(query:String):Int{
       val  curso = dbCarrinho.rawQuery(query,null,null)
        var  count = 0
       while (curso.moveToNext()){
            count +=1
       }

      return count

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
}
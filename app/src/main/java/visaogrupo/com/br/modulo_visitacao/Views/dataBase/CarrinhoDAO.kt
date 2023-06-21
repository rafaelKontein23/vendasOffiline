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
}
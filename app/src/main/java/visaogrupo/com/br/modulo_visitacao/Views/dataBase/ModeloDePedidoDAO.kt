package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.ModeloPedido

class ModeloDePedidoDAO (context: Context) {
    val dbModelo = DataBaseHelber(context)
    fun insert(modeloPedido: ModeloPedido){
        val valores = ContentValues()

        valores.put("CodigoUsuario",modeloPedido.codigoUsuario)
        valores.put("NomeModeloPedido",modeloPedido.nomeModeloPedido)
        valores.put("ClienteId",modeloPedido.clienteId)
        valores.put("CodigoLoja",modeloPedido.codigoLoja)
        valores.put("CodigoProduto",modeloPedido.codigoProduto)
        valores.put("Quantidade",modeloPedido.quantidade)
        valores.put("CodigoProgressiva",modeloPedido.codigoProgressiva)
        valores.put("Desconto",modeloPedido.desconto)

        dbModelo.writableDatabase.insert("TB_ModeloPedido",null,valores)

    }
}
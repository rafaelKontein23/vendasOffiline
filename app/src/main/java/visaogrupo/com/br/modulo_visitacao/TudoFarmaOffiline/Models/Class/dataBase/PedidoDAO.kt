package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Pedido

class PedidoDAO (context:Context) {

    val dbPedido = DataBaseHelber(context)
     fun listaPedido():MutableList<Pedido>{
         val  listaPedido:MutableList<Pedido> = mutableListOf()
         val  query = "SELECT cliente_id, loja_id,cnpj,razaosocial,SUM (valortotal),dataPedido,uf,valorminimoLoja, COUNT(DISTINCT barra) AS total \n " +
                 "FROM TB_Carrinho \n"+
                 "GROUP BY cliente_id, loja_id"

         val cursor = dbPedido.writableDatabase.rawQuery(query,null)

         while (cursor.moveToNext()){
           val cliente_id = cursor.getInt(0)
           val loja_id    = cursor.getInt(1)
           val cnpj =  cursor.getString(2)
           val razaoSocial = cursor.getString(3)
           val valortotal = cursor.getDouble(4)
           val qtdtotal = cursor.getInt(8)
           val data = cursor.getString(5)
           val ufcliente = cursor.getString(6)
           val valorminimoloja = cursor.getDouble(7)
           val pedido = Pedido(cliente_id,loja_id,cnpj,razaoSocial,valortotal,qtdtotal,data,ufcliente,valorminimoloja)
           listaPedido.add(pedido)

         }
         cursor.close()

         return listaPedido;
     }


}
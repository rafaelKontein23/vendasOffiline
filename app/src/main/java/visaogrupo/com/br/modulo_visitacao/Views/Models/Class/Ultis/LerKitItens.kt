package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Kit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitProtudos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitxCliente
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitxLoja
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitxPreco
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.KitDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.KitxClienteDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.KitxPrecoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.kitxLojaDAO

class LerKitItens {

    fun lerJsonKit(jsonArrayKit:JSONArray,context: Context){

        for (i in 0 until  jsonArrayKit.length()){
            val jsonItem = jsonArrayKit.optJSONObject(i)

            val kitid = jsonItem.getInt("Kit_id")
            val kitNome = jsonItem.getString("kit_nome")
            val Kit_Codigo = jsonItem.getInt("Kit_Codigo")
            val kit  = Kit(kitid,Kit_Codigo,kitNome)
            val kitDAO = KitDAO(context = context)
            kitDAO.insertKit(kit)

            val jssonArrayProdutoKit = jsonItem.getJSONArray("KITITENS")
            for (k in 0 until jssonArrayProdutoKit.length()){
                val jsonProdutoKit = jssonArrayProdutoKit.getJSONObject(k)
                val produtoCodigo = jsonProdutoKit.getString("Produto_Codigo")
                val produtoNome = jsonProdutoKit.getString("Produto_Nome")
                val fabricante = jsonProdutoKit.getString("Fabricante")
                val desconto = jsonProdutoKit.getDouble("Desconto")
                val quantidade = jsonProdutoKit.getInt("Quantidade")
                val imagem = jsonProdutoKit.getString("Imagem")
                val kitProtudos  = KitProtudos(Kit_Codigo,produtoCodigo,produtoNome,fabricante,desconto,quantidade,imagem,0.0,"","")
                kitDAO.insertKitProduto(kitProtudos)
            }
        }
    }
    fun  lerJsonKitxPreco(jsonArray: JSONArray,context: Context){
        for (i in 0 until  jsonArray.length()){
            val  jsonItemPreco = jsonArray.getJSONObject(i)
            val  produtoC0digo = jsonItemPreco.getString("Produto_codigo")
            val  PF = jsonItemPreco.getDouble("PF")
            val  PMC = jsonItemPreco.getDouble("PMC")
            val  UF = jsonItemPreco.getString("UF")
            val  Nome = jsonItemPreco.getString("Nome")
            val  Apresentacao = jsonItemPreco.getString("Apresentacao")
            val  CODLISTAPRECOSYNC = jsonItemPreco.getInt("CODLISTAPRECOSYNC")
            val  portfolio = jsonItemPreco.getString("Portfolio")

            val kitxPreco = KitxPreco(Apresentacao,CODLISTAPRECOSYNC,Nome,PF,PMC,portfolio,produtoC0digo,UF)
            val kitDAO = KitxPrecoDAO(context)
            kitDAO.insert(kitxPreco)
        }

    }

    fun lerJsonkitCliente(jsonArrayKit: JSONArray, context: Context){
        for (i in 0 until jsonArrayKit.length()){
            val jsonItemKitCliente  = jsonArrayKit.optJSONObject(i)
            val  kit_Cod = jsonItemKitCliente.getInt("Kit_cod")
            val cnpj = jsonItemKitCliente.getString("CNPJ")
            val kitxXliente = KitxCliente(cnpj,kit_Cod)
            val kitxClienteDAO = KitxClienteDAO(context)
            kitxClienteDAO.insert(kitxXliente)
        }
    }

    fun lerJsonkitxLoja(jsonArrayKit: JSONArray, context: Context){
        for (i in 0  until  jsonArrayKit.length()){
            val jsonItemKitxLoja =  jsonArrayKit.getJSONObject(i)
            val KitId = jsonItemKitxLoja.getInt("Kit_id")
            val kitCod = jsonItemKitxLoja.getInt("Kit_cod")
            val lojaId = jsonItemKitxLoja.getInt("Loja_id")
            val kitxLojas = KitxLoja(KitId,kitCod,lojaId)
            val kitxLojaDAO = kitxLojaDAO(context)
            kitxLojaDAO.insert(kitxLojas)
        }
    }
}
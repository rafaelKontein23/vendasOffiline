package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IProtudos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Produtos

class ProdutosDAO(context:Context):IProtudos {
    val DBProtudos = DataBaseHelber(context).writableDatabase
    val valoresProtudos = ContentValues()
    override fun insert(jsonProtudos: JSONArray): Boolean {
        try {
            for (i in  0  until jsonProtudos.length()){
                val jsonProtudoRetono = jsonProtudos.optJSONObject(i)
                valoresProtudos.put("Produto_codigo",jsonProtudoRetono.optInt("Produto_codigo"))
                valoresProtudos.put("Nome",jsonProtudoRetono.optString("Nome"))
                valoresProtudos.put("Apresentacao",jsonProtudoRetono.optString("Apresentacao"))
                valoresProtudos.put("ListaICMS",jsonProtudoRetono.optString("ListaICMS"))
                valoresProtudos.put("CaixaPadrao",jsonProtudoRetono.optInt("CaixaPadrao"))
                valoresProtudos.put("Imagem",jsonProtudoRetono.optString("Imagem"))
                valoresProtudos.put("Barra",jsonProtudoRetono.optString("Barra"))
                valoresProtudos.put("Unidade_id",jsonProtudoRetono.optInt("Unidade_id"))
                valoresProtudos.put("Categoria",jsonProtudoRetono.optString("Categoria"))
                valoresProtudos.put("RegistroMS",jsonProtudoRetono.optString("RegistroMS"))

                DBProtudos.insert("TB_produtos",null,valoresProtudos)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

      return true
    }

    override fun atualizar(produtos: Produtos): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(produtos: Produtos): Boolean {
        TODO("Not yet implemented")
    }
}
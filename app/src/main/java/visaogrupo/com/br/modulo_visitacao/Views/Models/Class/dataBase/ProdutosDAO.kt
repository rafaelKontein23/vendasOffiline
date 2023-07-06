package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Produtos

class ProdutosDAO(context:Context):
    visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.IProtudos {
    val DBProtudos = DataBaseHelber(context).writableDatabase
    val dblistaproudos = DataBaseHelber(context).readableDatabase
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

    override fun litar(query: String): MutableList<ProdutoProgressiva> {
        var listaprotudos = mutableListOf<ProdutoProgressiva>()

        val curso = dblistaproudos.rawQuery(query,null)
        while (curso.moveToNext()){
            val Produto_codigo = curso.getInt(4)
            val Nome = curso.getString(0)
            val Apresentacao = curso.getString(1)
            val Imagem = curso.getString(3)
            val Barra = curso.getString(2)
            val valor = curso.getString(8)

            val PMC = curso.getDouble(6)
            val Quantidade = curso.getInt(7)
            val Caixapadrao = curso.getInt(5)

            val valorcarrinho = curso.getDouble(9)
            val quantidadeCarrinho = curso.getInt(10)
            val  valor_total = curso.getDouble(11)
            var imagebase64 =""
            var estaCarrinhp = 0
            if (query.contains("TB_Imagens")) {
                imagebase64 = curso.getString(12)
                estaCarrinhp = curso.getInt(13)
            }else{
                imagebase64 = ""
                estaCarrinhp = curso.getInt(12)
            }



            val produtos = ProdutoProgressiva(Nome,Apresentacao, Barra,Imagem,Produto_codigo,valor,PMC,Quantidade,Caixapadrao,estaCarrinhp,valorcarrinho,quantidadeCarrinho,valor_total, imagebase64)

            listaprotudos.add(produtos)
        }
        return listaprotudos

    }

    fun countar(context:Context): Int {
        val dbHelper = DataBaseHelber(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT COUNT(*) FROM TB_produtos"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val count = cursor.getInt(0)

        cursor.close()
        db.close()

        return count
    }
}
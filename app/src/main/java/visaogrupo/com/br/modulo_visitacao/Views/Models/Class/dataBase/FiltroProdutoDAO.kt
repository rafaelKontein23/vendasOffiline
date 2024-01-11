package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FiltroProduto

class FiltroProdutoDAO(context: Context) {
    val dbFiltroProduto =  DataBaseHelber(context).writableDatabase
    fun insert(filtroProduto: FiltroProduto){
        try {
            val valoresFiltrosProdutos = ContentValues()
            valoresFiltrosProdutos.put("FiltroID",filtroProduto.Filtro_id)
            valoresFiltrosProdutos.put("Pares",filtroProduto.Pares)
            valoresFiltrosProdutos.put("FiltroCategoriaID",filtroProduto.FiltroCategoria_id)
            valoresFiltrosProdutos.put("Barra",filtroProduto.Barra)
            valoresFiltrosProdutos.put("Produto_codigo",filtroProduto.Produto_codigo)
            valoresFiltrosProdutos.put("LojaID",filtroProduto.Loja_id)
            dbFiltroProduto.insertOrThrow("TB_FiltroProdutos",null,valoresFiltrosProdutos)
        }catch (e:Exception){
            e.printStackTrace()
        }





    }
}
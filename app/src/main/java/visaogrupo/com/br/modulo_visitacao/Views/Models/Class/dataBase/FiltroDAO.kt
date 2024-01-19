package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FiltroPrincipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Filtros

class FiltroDAO (context: Context){
    val dbFiltro = DataBaseHelber(context).writableDatabase
    fun insert(filtro:Filtros){
        try {
            val valoresFiltro = ContentValues()

            valoresFiltro.put("FiltroID",filtro.Filtro_id)
            valoresFiltro.put("Pares",filtro.Pares)
            valoresFiltro.put("FiltroCategoriaID",filtro.FiltroCategoria_id)
            valoresFiltro.put("Descricao",filtro.Descricao)
            valoresFiltro.put("Quantidade",filtro.Qtd)
            dbFiltro.insertOrThrow("TB_Filtros",null,valoresFiltro)

        }catch (e:Exception){
            e.printStackTrace()
        }



    }

    fun listar(lojaid:Int):MutableList<Filtros>{
        val query = "SELECT DISTINCT Filtros.* \n" +
                "FROM TB_FiltroPricipal FiltroPrincipal\n" +
                "inner join TB_Filtros Filtros on FiltroPrincipal.FiltroCategoriaID = Filtros.filtrocategoriaid\n" +
                "inner join TB_FiltroProdutos FiltroPrd on FiltroPrd.FiltroCategoriaID =FiltroPrincipal.filtrocategoriaid \n" +
                "and FiltroPrd.FiltroID = Filtros.filtroid\n" +
                "INNER JOIN TB_produtos Prod on Prod.barra = FiltroPrd.barra\n" +
                "inner join TB_Progressiva Prog on prog.prod_cod = prod.Produto_codigo and loja_id = ${lojaid}"
        val listaFiltro = mutableListOf <Filtros>()
        val cursor = dbFiltro.rawQuery(query,null)
        while (cursor.moveToNext()){
            val filtroId = cursor.getInt(1)
            val pares = cursor.getString(2)
            val filtroCatecoriaId = cursor.getInt(3)
            val descricao = cursor.getString(4)
            val quantidae = cursor.getInt(5)
            val filtros =  Filtros(descricao,filtroCatecoriaId,filtroId,pares,quantidae)
            listaFiltro.add(filtros)


        }
        cursor.close()

        return listaFiltro
    }
}
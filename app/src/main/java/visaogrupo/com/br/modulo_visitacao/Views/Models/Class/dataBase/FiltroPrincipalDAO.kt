package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FiltroPrincipal

class FiltroPrincipalDAO (context: Context){


    val dbFiltroPrincipal = DataBaseHelber(context).writableDatabase

    fun insert(filtroPrincipal:FiltroPrincipal){
        try {
            val valoresFiltroPrincipa = ContentValues()

            valoresFiltroPrincipa.put("FiltroCategoriaID",filtroPrincipal.FiltroCategoria_id)
            valoresFiltroPrincipa.put("Descricao",filtroPrincipal.Descricao)

            dbFiltroPrincipal.insertOrThrow("TB_FiltroPricipal", null, valoresFiltroPrincipa)
        }catch (e:Exception){
             e.printStackTrace()
        }
    }

    fun listar (lojaid: Int):MutableList<FiltroPrincipal>{
        val query = "\n" +
                "SELECT DISTINCT FiltroPrincipal.* \n" +
                "FROM TB_FiltroPricipal FiltroPrincipal\n" +
                "inner join TB_Filtros Filtros on FiltroPrincipal.FiltroCategoriaID = Filtros.filtrocategoriaid\n" +
                "inner join TB_FiltroProdutos FiltroPrd on FiltroPrd.FiltroCategoriaID =FiltroPrincipal.filtrocategoriaid \n" +
                "and FiltroPrd.FiltroID = Filtros.filtroid\n" +
                "INNER JOIN TB_produtos Prod on Prod.barra = FiltroPrd.barra\n" +
                "inner join TB_Progressiva Prog on prog.prod_cod = prod.Produto_codigo and loja_id = ${lojaid} "

        val listaFiltroPrincipal = mutableListOf <FiltroPrincipal>()
        val cursor = dbFiltroPrincipal.rawQuery(query,null)
        while (cursor.moveToNext()){
            val filtroCategoriaID = cursor.getInt(0)
            val descricao = cursor.getString(1)
            val filtroPrincipal = FiltroPrincipal(descricao,filtroCategoriaID)
            listaFiltroPrincipal.add(filtroPrincipal)

        }
        cursor.close()

        return listaFiltroPrincipal
    }
}
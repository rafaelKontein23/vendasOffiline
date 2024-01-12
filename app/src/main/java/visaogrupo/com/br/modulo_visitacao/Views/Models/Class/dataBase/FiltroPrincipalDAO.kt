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

    fun listar ():MutableList<FiltroPrincipal>{
        val query = "SELECT * FROM TB_FiltroPricipal"
        val listaFiltroPrincipal = mutableListOf <FiltroPrincipal>()
        val cursor = dbFiltroPrincipal.rawQuery(query,null)
        while (cursor.moveToNext()){
            val filtroCategoriaID = cursor.getInt(0)
            val descricao = cursor.getString(1)
            val filtroPrincipal = FiltroPrincipal(descricao,filtroCategoriaID)
            listaFiltroPrincipal.add(filtroPrincipal)

        }
        return listaFiltroPrincipal
    }
}
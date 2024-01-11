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
}
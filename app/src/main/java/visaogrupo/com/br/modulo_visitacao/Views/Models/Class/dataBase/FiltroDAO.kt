package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
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
}
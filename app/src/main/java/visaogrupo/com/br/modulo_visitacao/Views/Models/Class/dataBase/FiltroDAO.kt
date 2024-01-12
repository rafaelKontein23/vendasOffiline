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

    fun listar():MutableList<Filtros>{
        val query = "SELECT * FROM TB_Filtros"
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
        return listaFiltro
    }
}
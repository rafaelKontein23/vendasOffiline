package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagExclusiva

class FormaDePagamentoExclusivaDAO (context: Context) {
    val  dbFomaPagmrto = DataBaseHelber(context)
    fun insert(listaFormPagExclusiva :MutableList<FormaDePagExclusiva>){
        val valoresFormPagmentoExclusiva = ContentValues()
        for (i in  listaFormPagExclusiva){
            valoresFormPagmentoExclusiva.put("CNPJ", i.CNPJ)
            valoresFormPagmentoExclusiva.put("Cod_FormaPgto", i.Cod_FormaPgto)
            valoresFormPagmentoExclusiva.put("FormaPgto", i.FormaPgto)

            dbFomaPagmrto.writableDatabase.insert("TB_FormPag", null,valoresFormPagmentoExclusiva)
        }

    }
}
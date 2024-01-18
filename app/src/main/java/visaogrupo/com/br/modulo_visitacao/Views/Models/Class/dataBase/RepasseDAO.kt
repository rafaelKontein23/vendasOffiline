package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Repasse

class RepasseDAO (context: Context){
    val dbRepasse = DataBaseHelber(context).writableDatabase
    fun insert(repasse: Repasse ){

        try {
            val contentValues = ContentValues()
            contentValues.put("MATERIAL",repasse.MATERIAL)
            contentValues.put("PERCENTUAL",repasse.PERCENTUAL)
            contentValues.put("CENTRO",repasse.centro)
            contentValues.put("UF",repasse.UF)
            dbRepasse.insertOrThrow("TB_Repasse",null,contentValues)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}
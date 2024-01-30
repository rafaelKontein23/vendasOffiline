package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitxLoja

class kitxLojaDAO (context: Context){
    val dbkitxLoja = DataBaseHelber(context).writableDatabase

    fun insert(kitxLoja: KitxLoja){
        try {
            val contentValues = ContentValues()
            contentValues.put("Kit_id", kitxLoja.Kit_id)
            contentValues.put("Kit_cod", kitxLoja.KitCod)
            contentValues.put("Loja_id", kitxLoja.Loja_id)

            dbkitxLoja.insertOrThrow("TB_KitxLoja",null,contentValues)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}
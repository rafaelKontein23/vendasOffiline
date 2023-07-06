package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.Context

class ImagensDAO {


    fun hasDataInImagesTable(context: Context): Boolean {
        val database = DataBaseHelber(context)
        val query = "SELECT COUNT(*) FROM TB_Imagens"
        val cursor = database.writableDatabase.rawQuery(query, null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        return count > 0
    }
}
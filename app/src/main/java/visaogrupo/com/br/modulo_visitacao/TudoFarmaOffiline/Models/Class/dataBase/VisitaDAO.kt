package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas

class VisitaDAO(context: Context) {
    val db = DataBaseHelber(context).writableDatabase
    fun insert(visitas: Visitas){
        try {
            val valoresInserts = ContentValues().apply {
                put("cnpj", visitas.cnpj)
                put("RazaoSocial", visitas.razaoSocial)
                put("Endereco", visitas.endereco)
                put("Telefone", visitas.telefone)
                put("Email", visitas.email)
                put("data_visita", visitas.dataVisita)
                put("status", 0)
                put("ordem", visitas.ordem)


            }
            db.insertOrThrow("TB_Visitas",null,valoresInserts)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun atualizaPositionEstatus(listaVisitas: MutableList<Visitas>, alteraStatusVisitas:Boolean = false, status:Int = 0){
        if (!alteraStatusVisitas){
            for ((i, vistas) in listaVisitas.withIndex()){
                val query = "UPDATE TB_Visitas SET ordem = '$i' WHERE data_visita = '${vistas.dataVisita}' AND visitaID = ${vistas.visitaID}"
                db.execSQL(query)
            }

        }else{
            for ((i, vistas) in listaVisitas.withIndex()){
                val query = "UPDATE TB_Visitas SET status = $status WHERE data_visita = '${vistas.dataVisita}' AND visitaID = ${vistas.visitaID}"
                db.execSQL(query)
            }
        }

    }

    fun lista(ordem:Boolean, dataVisita: String = ""):MutableList<Visitas>{
        var query = ""
        if (ordem){
            query = "SELECT * FROM TB_Visitas"
        }else{
            query = "SELECT * FROM TB_Visitas WHERE data_visita = '${dataVisita}' ORDER BY ordem ASC "
        }

        val cursor = db.rawQuery(query,null)
        val listaVisitas = mutableListOf<Visitas>()

        while (cursor.moveToNext()){
            val visitaID  = cursor.getInt(0)
            val cnpj = cursor.getString(1)
            val razaoSocial = cursor.getString(2)
            val endereco   = cursor.getString(3)
            val telefone = cursor.getString(4)
            val emal    = cursor.getString(5)
            val dataVisita = cursor.getString(6)
            val  status = cursor.getInt(7)
            val ordem     = cursor.getInt(8)

            val visitas = Visitas(visitaID, cnpj, razaoSocial, endereco, telefone, emal,dataVisita,ordem, status =  status)
            listaVisitas.add(visitas)
        }
        return listaVisitas
    }
}
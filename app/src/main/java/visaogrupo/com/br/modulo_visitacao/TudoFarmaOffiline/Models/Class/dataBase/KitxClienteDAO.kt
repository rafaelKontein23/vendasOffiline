package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitxCliente

class KitxClienteDAO (context: Context){
    val dbkitCliente = DataBaseHelber(context).writableDatabase


    fun insert(kitxCliente: KitxCliente){
        try {
            val contetValuesClientesKit = ContentValues()
            contetValuesClientesKit.put("Kit_cod",kitxCliente.Kit_cod)
            contetValuesClientesKit.put("CNPJ",kitxCliente.CNPJ)

            dbkitCliente.insertOrThrow("TB_KItXcliente",null,contetValuesClientesKit)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

}
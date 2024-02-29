package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitxPreco

class KitxPrecoDAO (context: Context) {
     val dbKitXPreco = DataBaseHelber(context).writableDatabase
    fun insert(kitxPreco: KitxPreco){
        try {
            val valoresKitxPreco = ContentValues()
            valoresKitxPreco.put("Produto_codigo",kitxPreco.Produto_codigo)
            valoresKitxPreco.put("PF",kitxPreco.PF)
            valoresKitxPreco.put("PMC",kitxPreco.PMC)
            valoresKitxPreco.put("UF",kitxPreco.UF)
            valoresKitxPreco.put("Nome",kitxPreco.Nome)
            valoresKitxPreco.put("Apresentacao",kitxPreco.Apresentacao)
            valoresKitxPreco.put("CODLISTAPRECOSYNC",kitxPreco.CODLISTAPRECOSYNC)
            valoresKitxPreco.put("Portfolio",kitxPreco.Portfolio)

            dbKitXPreco.insertOrThrow("TB_kit_x_preco",null,valoresKitxPreco)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}
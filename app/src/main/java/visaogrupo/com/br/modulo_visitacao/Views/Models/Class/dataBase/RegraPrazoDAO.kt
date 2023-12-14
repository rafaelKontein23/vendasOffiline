package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.IRegraPrazo
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PrazoMedioXValor

class RegraPrazoDAO (context: Context):IRegraPrazo{

    val DBRegra = DataBaseHelber(context).writableDatabase
    val valoresRegra = ContentValues()
    override fun insertRegraProgressiva(list: MutableList<PrazoMedioXValor>) {
        for(i in list){
            valoresRegra.put("ValorDe", i.ValorDe)
            valoresRegra.put("ValorAte", i.ValorAte)
            valoresRegra.put("PrazoMedio",i.PrazoMedio)

            DBRegra.insert("TB_PrazoMedioXValor",null,valoresRegra)
        }


    }
}
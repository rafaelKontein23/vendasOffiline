package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import android.content.Context
import android.util.Log
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber

class ExcluiDados(context: Context) {
     val db = DataBaseHelber(context).writableDatabase
    fun exluidados():MutableList<String>{
        val listaNomeTabela:MutableList<String> = mutableListOf<String>()

        listaNomeTabela.add("TB_lojas")
        listaNomeTabela.add("TB_clientes")
        listaNomeTabela.add("TB_lojaporcliente")
        listaNomeTabela.add("TB_produtos")
        for (nomes in listaNomeTabela){
            try {
                val excluiTabela = "DELETE FROM ${nomes}"
                db.execSQL(excluiTabela)
                Log.d("Excluiu dados de :","${nomes}")
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("Falho a exclusão de","Dados")
            }

        }

        return listaNomeTabela
    }


}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.content.Context
import android.util.Log
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.DataBaseHelber

class ExcluiDados(context: Context) {
     val db = DataBaseHelber(context).writableDatabase
    fun exluidados():MutableList<String>{
        val listaNomeTabela:MutableList<String> = mutableListOf<String>()

        listaNomeTabela.add("TB_lojas")
        listaNomeTabela.add("TB_clientes")
        listaNomeTabela.add("TB_lojaporcliente")
        listaNomeTabela.add("TB_produtos")
        listaNomeTabela.add("TB_fornaDePagamento")
        listaNomeTabela.add("TB_OperadorLogistico")
        listaNomeTabela.add("TB_Progressiva")
        listaNomeTabela.add("TB_Estoque")
        listaNomeTabela.add("TB_formaDePagamento")
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
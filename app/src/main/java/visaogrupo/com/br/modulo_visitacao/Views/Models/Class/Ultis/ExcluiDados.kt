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
        listaNomeTabela.add("TB_Imagens")

        listaNomeTabela.add("TB_Filtros")
        listaNomeTabela.add("TB_FiltroProdutos")
        listaNomeTabela.add("TB_FiltroPricipal")

        listaNomeTabela.add("TB_Kits")
        listaNomeTabela.add("Tb_Kit_Produtos")
        listaNomeTabela.add("TB_kit_x_preco")
        listaNomeTabela.add("TB_KItXcliente")
        listaNomeTabela.add("TB_KitxLoja")

        listaNomeTabela.add("Tb_GrupoAB")
        listaNomeTabela.add("TB_grupoAB_Produtos")


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

    fun excluidaEstoque (){
        try {
            val excluiTabela = "DELETE FROM TB_Estoque"
            db.execSQL(excluiTabela)
            Log.d("Excluiu dados de : ","TB_Estoque")
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("Falho a exclusão de","Dados")
        }

    }


}
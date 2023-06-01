package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IFormaDePagamento
import visaogrupo.com.br.modulo_visitacao.Views.Models.FormaDePagaemnto

class FormaDePagamentoDAO(context: Context):IFormaDePagamento {
    val dbFormaDePagamento = DataBaseHelber(context).writableDatabase
    override fun insert(jsonFormaDePagamento: JSONArray, lojaID: Int) {
        val  valorFormaDePagaemnto = ContentValues()
        try {
            for (i in 0 until jsonFormaDePagamento.length()){

                val jsonretornoFormaDePagamento =jsonFormaDePagamento.getJSONObject(i)

                valorFormaDePagaemnto.put("loja",jsonretornoFormaDePagamento.optInt("loja"))
                valorFormaDePagaemnto.put("Cod_FormaPgto",jsonretornoFormaDePagamento.optString("Cod_FormaPgto"))
                valorFormaDePagaemnto.put("FormaPgto",jsonretornoFormaDePagamento.optString("FormaPgto"))
                valorFormaDePagaemnto.put("ValorMinimo",jsonretornoFormaDePagamento.optDouble("ValorMinimo"))
                valorFormaDePagaemnto.put("Alternativa",jsonretornoFormaDePagamento.optBoolean("Alternativa"))

                dbFormaDePagamento.insert("TB_fornaDePagamento",null,valorFormaDePagaemnto)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun atualizar(FormaDePagaemnto: FormaDePagaemnto): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(FormaDePagaemnto: FormaDePagaemnto): Boolean {
        TODO("Not yet implemented")
    }
}
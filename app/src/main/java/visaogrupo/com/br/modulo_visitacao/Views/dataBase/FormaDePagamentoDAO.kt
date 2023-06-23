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

                valorFormaDePagaemnto.put("loja_id",jsonretornoFormaDePagamento.optInt("loja"))
                valorFormaDePagaemnto.put("Cod_FormaPgto",jsonretornoFormaDePagamento.optString("Cod_FormaPgto"))
                valorFormaDePagaemnto.put("FormaPgto",jsonretornoFormaDePagamento.optString("FormaPgto"))
                valorFormaDePagaemnto.put("ValorMinimo",jsonretornoFormaDePagamento.optDouble("ValorMinimo"))
                valorFormaDePagaemnto.put("Alternativa",jsonretornoFormaDePagamento.optBoolean("Alternativa"))

                dbFormaDePagamento.insert("TB_formaDePagamento",null,valorFormaDePagaemnto)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun atualizar(FormaDePagaemnto: FormaDePagaemnto): Boolean {
        TODO("Not yet implemented")
    }

    override fun listar(loja_id:Int): MutableList<FormaDePagaemnto> {
         val query = "SELECT * FROM TB_formaDePagamento WHERE loja_id = ${loja_id} "
         val listaFprm :MutableList<FormaDePagaemnto> = mutableListOf()
         val curso = dbFormaDePagamento.rawQuery(query,null)
         while (curso.moveToNext()){


             val  loja_id = curso.getInt(0)
             val Cod_FormaPgto = curso.getString(1)
             val FormaPgto   = curso.getString(2)
             val  ValorMinimo = curso.getDouble(3)
             val  Alternativa = curso.getInt(4)

             val formaDePagaemnto = FormaDePagaemnto(Alternativa,Cod_FormaPgto,FormaPgto,ValorMinimo,loja_id)
             listaFprm.add(formaDePagaemnto)
         }
        return listaFprm
    }
}
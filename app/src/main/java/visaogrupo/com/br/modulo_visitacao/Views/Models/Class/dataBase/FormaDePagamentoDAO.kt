package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.IFormaDePagamento
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas

class FormaDePagamentoDAO(context: Context):
    IFormaDePagamento {
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
                valorFormaDePagaemnto.put("PrazoMedio",jsonretornoFormaDePagamento.optInt("PrazoMedio"))

                dbFormaDePagamento.insert("TB_formaDePagamento",null,valorFormaDePagaemnto)

            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun atualizar(FormaDePagaemnto: FormaDePagaemnto): Boolean {
        TODO("Not yet implemented")
    }

    override fun listar(
        loja_id: Int,
        valorToatalPedido: Double,
        RegraPrazo:Int,
        cnpj:String
    ): MutableList<FormaDePagaemnto> {
         var query = ""
        val listaFprm :MutableList<FormaDePagaemnto> = mutableListOf()
         if (RegraPrazo  == 1){
             query = "SELECT 1 Tipo, * FROM TB_formaDePagamento Pagamento " +
                     "WHERE pagamento.Cod_FormaPgto in ( " +
                     "  SELECT Pgto_CNPJ.Cod_FormaPgto FROM TB_FormPag Pgto_CNPJ " +
                     "  WHERE Pgto_CNPJ.cnpj = '${cnpj}' " +
                     ") and loja_id = ${loja_id} " +
                     "UNION " +
                     "SELECT 2 Tipo,* FROM TB_formaDePagamento Pagamento " +
                     "WHERE pagamento.prazomedio in ( " +
                     " SELECT DISTINCT prazomedio FROM TB_PrazoMedioXValor Pz " +
                     " where ${valorToatalPedido} BETWEEN valorde and valorate " +
                     ") and loja_id = ${loja_id}"

             val curso = dbFormaDePagamento.rawQuery(query,null)

             while (curso.moveToNext()){
                 try {
                     val tipo = curso.getInt(0)

                     val loja_id = curso.getInt(1)
                     val Cod_FormaPgto = curso.getString(2)
                     val FormaPgto   = curso.getString(3)
                     val ValorMinimo = curso.getDouble(4)
                     val Alternativa = curso.getInt(6)
                     val  PrazoMedio = curso.getInt(5)

                     val formaDePagaemnto = FormaDePagaemnto(Alternativa,Cod_FormaPgto,FormaPgto,ValorMinimo,loja_id,PrazoMedio,tipo)
                     listaFprm.add(formaDePagaemnto)
                 }catch (e:Exception){
                     e.printStackTrace()
                 }

             }
        }else {
              query = "SELECT 0 Tipo,* FROM TB_formaDePagamento WHERE loja_id = ${loja_id} " +
                      "UNION "+
                      "SELECT 2 Tipo,* FROM TB_formaDePagamento Pagamento " +
                      "WHERE pagamento.prazomedio in ( " +
                      "SELECT DISTINCT prazomedio FROM TB_PrazoMedioXValor Pz " +
                      " where ${valorToatalPedido} BETWEEN valorde and valorate " +
                      ") and loja_id = ${loja_id}"


             val curso = dbFormaDePagamento.rawQuery(query,null)
             while (curso.moveToNext()){

                 val tipo = curso.getInt(0)
                 val loja_id = curso.getInt(0)
                 val Cod_FormaPgto = curso.getString(2)
                 val FormaPgto   = curso.getString(3)
                 val ValorMinimo = curso.getDouble(4)
                 val Alternativa = curso.getInt(6)
                 val  PrazoMedio = curso.getInt(5)

                 val formaDePagaemnto = FormaDePagaemnto(Alternativa,Cod_FormaPgto,FormaPgto,ValorMinimo,loja_id,PrazoMedio,tipo)
                 listaFprm.add(formaDePagaemnto)

             }

         }

        return listaFprm
    }
}
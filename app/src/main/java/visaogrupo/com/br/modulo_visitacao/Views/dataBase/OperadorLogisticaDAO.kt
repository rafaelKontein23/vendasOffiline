package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IOperadorLogistico
import visaogrupo.com.br.modulo_visitacao.Views.Models.OperadorLogistico

class OperadorLogisticaDAO (context: Context):IOperadorLogistico {
    val sdbOperadorLogistico = DataBaseHelber(context).writableDatabase
    override fun insert(jsonFormaDePagamento: JSONArray, lojaID: Int, uf: String) {
        val valorOP = ContentValues()
         for (i in 0 until  jsonFormaDePagamento.length()){

             val jsonRetornoOP = jsonFormaDePagamento.getJSONObject(i)

             if (jsonRetornoOP.getString("Estado").equals(uf)){
                 valorOP.put("loja_id",jsonRetornoOP.optInt("Loja_id"))
                 valorOP.put("OperadorLogistico_ID",jsonRetornoOP.optInt("OperadorLogistico_ID"))
                 valorOP.put("Nome",jsonRetornoOP.optString("Nome"))
                 valorOP.put("Estado",jsonRetornoOP.optString("Estado"))
                 valorOP.put("MinimoValor",jsonRetornoOP.optDouble("MinimoValor"))
                 valorOP.put("OperadorLogistico_Grupo_id",jsonRetornoOP.optInt("OperadorLogistico_Grupo_id"))
                 valorOP.put("Grupo",jsonRetornoOP.optString("Grupo"))

                 sdbOperadorLogistico.insert("TB_OperadorLogistico",null,valorOP)

             }

         }
    }

    override fun atualizar(OperadorLogistico: OperadorLogistico): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(OperadorLogistico: OperadorLogistico): Boolean {
        TODO("Not yet implemented")
    }

    override fun listar(uf: String, loja_id: Int): MutableList<OperadorLogistico> {
        val  listaopl:MutableList<OperadorLogistico> = mutableListOf()
        val query = "SELECT * FROM TB_OperadorLogistico WHERE loja_id = ${loja_id} and estado = '${uf}' "
        val cursor = sdbOperadorLogistico.rawQuery(query,null)
        while (cursor.moveToNext()){
            val loja_id  = cursor.getInt(0)
            val operadorLogisticoid = cursor.getInt(1)
            val nome = cursor.getString(2)
            val estado = cursor.getString(3)
            val minimoValor = cursor.getDouble(4)
            val grupo_id = cursor.getInt(5)
            val grupo =cursor.getString(6)
            val OperadorLogistico = OperadorLogistico(estado,grupo,loja_id,minimoValor,nome,grupo_id,operadorLogisticoid)
            listaopl.add(OperadorLogistico)
        }

        return listaopl
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.util.Log
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagExclusiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga

class TaskFormaDePagamentoExclusiva {

    fun taskFormaDePagamentos(uf:String, cnpj:String): MutableList<FormaDePagExclusiva>{
        val listaFormaExcluisva = mutableListOf<FormaDePagExclusiva>()
        val  isync = Retrofit_Carga.createService(Isyncs_Cargas::class.java)
        val request = isync.FormaDePagExclus(uf, cnpj)
        val response  = request.execute()

        if (response.isSuccessful){
            val responseJson = response.body()?.string()
            val jsonPrincipal = JSONObject(responseJson)
            println("Json principal"+jsonPrincipal.toString())
            val  cnpj = jsonPrincipal.getString("CNPJ")
            val jsonArray = jsonPrincipal.getJSONArray("FormaPagamento")
            try {
                for (i in 0 until jsonArray.length()){
                    val jsonFormaDePag =  jsonArray.getJSONObject(i)
                    val  Cod_FormaPgto = jsonFormaDePag.getString("Cod_FormaPgto")
                    val  FormaPgto = jsonFormaDePag.getString("FormaPgto")
                    val  formaPagExclussiva = FormaDePagExclusiva(cnpj,Cod_FormaPgto,FormaPgto)
                    listaFormaExcluisva.add(formaPagExclussiva)

                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }else {
            Log.d("erro",response.errorBody().toString())
        }


     return  listaFormaExcluisva
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs

class TaskEstoque {


    fun recuperaEstoque(loja_id:Int): JSONArray? {
        val jsonArrayEstoque: JSONArray? = null
        try {
            val client =  OkHttpClient().newBuilder()
                .build();
            val request =  Request.Builder()
                .url("${visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs.urlEstoque}${loja_id}.json")
                .method("GET", null)
                .build();


            val response = client.newCall(request).execute();
            val  retornoestoque =  response.body()?.string()
            val jsonRetrono = JSONObject(retornoestoque)
            val jsonArrayestoque = JSONArray(jsonRetrono.getString("Estoque"))
            return jsonArrayestoque
        } catch (e: Exception) {
            e.printStackTrace()
            return jsonArrayEstoque
        }

    }
}
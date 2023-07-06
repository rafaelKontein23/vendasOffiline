package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs


class TaskProgressivas (context: Context) {

    fun recuperaProgressiva(loja_id:Int ,uf:String): JSONArray? {
        val jsonArrayProgressiva: JSONArray? = null
        try {
            val client =  OkHttpClient().newBuilder()
                .build();
            val request =  Request.Builder()
                .url("${visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs.urlProgressiva}${loja_id}_${uf}.json")
                .method("GET", null)
                .build();


            val response = client.newCall(request).execute();
            val  retorprogresiiva =  response.body()?.string()
            val jsonRetrono = JSONObject(retorprogresiiva)
            val jsonArrayProgressiva = JSONArray(jsonRetrono.getString("PROGRESSIVAS"))
            return jsonArrayProgressiva
        } catch (e: Exception) {
            e.printStackTrace()
            return jsonArrayProgressiva
        }

        }

}

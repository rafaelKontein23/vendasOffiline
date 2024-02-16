package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs


class TaskProgressivas (context: Context) {

    fun recuperaProgressiva(loja_id:Int ,uf:String,codigo:Int): JSONArray? {
        val jsonArrayProgressiva: JSONArray? = null
        try {

            val client =  OkHttpClient().newBuilder()
                .build();
            val request =  Request.Builder()
                .url("${URLs.urlCarga}docs/tablet/carga/Progressivas/Progressiva_${loja_id}_${uf}_${codigo}.json")
                .method("GET", null)
                .build();

            val response = client.newCall(request).execute();
            val  retorprogresiiva =  response.body()?.string()
            val jsonRetrono = JSONObject(retorprogresiiva)
            val jsonArrayProgressiva = JSONArray(jsonRetrono.getString("PROGRESSIVAS"))
            if (loja_id ==1 && uf.equals("RS")){
                println("fefs")
            }
            return jsonArrayProgressiva
        } catch (e: Exception) {
            e.printStackTrace()
            return jsonArrayProgressiva
        }

        }

}

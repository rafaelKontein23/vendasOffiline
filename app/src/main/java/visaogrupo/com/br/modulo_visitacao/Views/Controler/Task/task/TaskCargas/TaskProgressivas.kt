package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas

import android.content.ContentValues
import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request.Retrofit_Progressiva
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request.IsyncService
import visaogrupo.com.br.modulo_visitacao.Views.Models.Progressiva
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber

class TaskProgressivas (context: Context) {

    val db_Progreesivas = DataBaseHelber(context).writableDatabase
    fun recuperaProgressiva(loja_id:Int ,uf:String): JSONArray? {
        val jsonArrayProgressiva: JSONArray? = null
        try {
            val client =  OkHttpClient().newBuilder()
                .build();
            val request =  Request.Builder()
                .url("https://wwwi.catarinenseonline.com.br/Progressivas/Progressiva_${loja_id}_${uf}.json")
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

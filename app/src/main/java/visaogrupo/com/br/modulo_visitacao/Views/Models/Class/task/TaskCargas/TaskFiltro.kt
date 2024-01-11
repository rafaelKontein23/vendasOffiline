package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs

class TaskFiltro {

    fun requestFiltro():JSONArray{
        val client =  OkHttpClient().newBuilder()
            .build();
        val request =  Request.Builder()
            .url("${URLs.urlCarga}Padrao\\Filtros.json")
            .method("GET", null)
            .build();
        val response = client.newCall(request).execute();
        val  retornoFiltroPrincipal =  response.body()?.string()
        val jsonObject = JSONObject(retornoFiltroPrincipal)
        val jsonPrincipalArray = jsonObject.getJSONArray("Filtros")

        return  jsonPrincipalArray
    }
}
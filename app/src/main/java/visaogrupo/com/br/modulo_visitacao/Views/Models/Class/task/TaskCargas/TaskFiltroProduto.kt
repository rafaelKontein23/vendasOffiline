package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs

class TaskFiltroProduto {

    fun requestFiltroProduto():JSONArray{
        val client =  OkHttpClient().newBuilder()
            .build();
        val request =  Request.Builder()
            .url("${URLs.urlCarga}Padrao\\FiltroProduto.json")
            .method("GET", null)
            .build();
        val response = client.newCall(request).execute();
        val  retornoFiltroPrincipal =  response.body()?.string()
        val jsonObject = JSONObject(retornoFiltroPrincipal)
        val jsonPrincipalArray = jsonObject.getJSONArray("FiltroProduto")

        return  jsonPrincipalArray
    }
}
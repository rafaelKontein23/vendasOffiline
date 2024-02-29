package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Retrofit_Request.URLs

class TaskFiltroPrincipal {
    fun requestFiltroPrincipal():JSONArray{
        val client =  OkHttpClient().newBuilder()
            .build();
        val request =  Request.Builder()
            .url("${URLs.urlCarga}Padrao\\FiltroPrincipal.json")
            .method("GET", null)
            .build();
        val response = client.newCall(request).execute();
        val  retornoFiltroPrincipal =  response.body()?.string()
        val jsonObject = JSONObject(retornoFiltroPrincipal)
        val jsonPrincipalArray = jsonObject.getJSONArray("FiltroPrincipal")

        return  jsonPrincipalArray
    }
}
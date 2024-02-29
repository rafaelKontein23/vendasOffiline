package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs

class TaskRepasse {

    fun requestrepase(uf:String,codigo:Int, listaErr:MutableList<String>): JSONArray {
        var jsonPrincipalArray = JSONArray()
        val client =  OkHttpClient().newBuilder()
            .build();
        val request =  Request.Builder()
            .url("${URLs.urlCarga}Repasse\\00${codigo}_${uf}.json")
            .method("GET", null)
            .build();

        val response = client.newCall(request).execute();
        if (response.code() != 200){
            return jsonPrincipalArray
            if(listaErr.contains("Repasse")){
                listaErr.add("Repasse")

            }
        }else{
            val  retornoFiltroPrincipal =  response.body()?.string()
            val jsonObject = JSONObject(retornoFiltroPrincipal)
            jsonPrincipalArray = jsonObject.getJSONArray("Repasse")
            for (i in 0 until jsonPrincipalArray.length()) {
                val objeto = jsonPrincipalArray.getJSONObject(i)
                objeto.put("UF", uf)
                objeto.put("CENTRO", codigo)

            }
            return  jsonPrincipalArray
        }

    }
}
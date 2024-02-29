package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas

import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Retrofit_Request.Retrofit_Carga

class TaskGruposAB {

    fun taskbuscagrupo(lojaId:String,uf:String,codSync:String,jsonArray: MutableList<JSONArray>, list: MutableList<String>){
        val  isync = Retrofit_Carga.createService(Isyncs_Cargas::class.java)
        val request = isync.grupolojaAb(lojaId,uf,codSync)
        val response  = request.execute()
        if (response.isSuccessful){
            val  respostaRequest =response.body()?.string()
            jsonArray.add( JSONObject(respostaRequest).getJSONArray("Grupo"))
        }else{
            if (!list.contains("Grupo AB")){
                list.add("Grupo AB")

            }
        }
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga

class TaskGrupoProgressiva {

    fun taskGrupoProgressiva(lojaId:Int, jsonArray: MutableList<JSONArray>, listaErro:MutableList<String>){

        val async = Retrofit_Carga.createService(Isyncs_Cargas::class.java)

        val request = async.grupoProgressiva(lojaId)

        val response =  request.execute()

        if (response.isSuccessful){
            val responseGrupoProgrssiva = response.body()?.string()
            jsonArray.add(JSONObject(responseGrupoProgrssiva).getJSONArray("PROGRESSIVAS"))
        }else{
            if (!listaErro.contains("Grupo Progressiva AB")){
                listaErro.add("Grupo Progressiva AB")

            }
        }
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.IsyncService
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagExclusiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro

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
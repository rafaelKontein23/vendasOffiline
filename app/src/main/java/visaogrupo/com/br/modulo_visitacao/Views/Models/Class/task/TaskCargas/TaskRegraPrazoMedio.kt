package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PrazoMedioXValor
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_URL

class TaskRegraPrazoMedio {


    fun  tasKRegra(): MutableList<PrazoMedioXValor>{

        val lisotaPrazoXValo = mutableListOf<PrazoMedioXValor>()
        val isync = Retrofit_Carga.createService(Isyncs_Cargas::class.java);

        val resquest = isync.PrazoMedio()

        val response = resquest.execute()

        if (response.isSuccessful){
            val  responseJson = response.body()?.string()
            val json = JSONObject(responseJson)
            val jsonArray = json.getJSONArray("PrazoMedioXValor")
            for (i in 0 until jsonArray.length()){
                val jsonPrazo = jsonArray.getJSONObject(i)
                val  gson = Gson()
                val prazoMedio = gson.fromJson(jsonPrazo.toString(),PrazoMedioXValor::class.java)
                lisotaPrazoXValo.add(prazoMedio)
            }


            Log.d("faafs","Aloo")
        }else {
            Log.d("dada",response.errorBody()!!.string())
        }
        return lisotaPrazoXValo
    }
}
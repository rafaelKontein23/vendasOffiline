package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import visaogrupo.com.br.modulo_visitacao.Views.Models.Progressiva

interface IsyncService {


    @GET("Progressivas/Progressiva_2_SP.json")
    fun progressivas(): Response<JSONObject>?

}
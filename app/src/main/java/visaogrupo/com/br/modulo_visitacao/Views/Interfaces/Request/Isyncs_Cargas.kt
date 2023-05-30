package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request


import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url
import retrofit2.Call


interface Isyncs_Cargas {



    @Streaming
    @GET
    fun downloadFile(@Url fileUrl:String): Call<ResponseBody>
}
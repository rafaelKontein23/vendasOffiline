package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit_Carga {

    companion object {
         val url =
             visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs.Companion.urlCarga


         val builder = Retrofit.Builder().baseUrl(visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga.Companion.url)
            .addConverterFactory(GsonConverterFactory.create())

         val retrofit = visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga.Companion.builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_Carga.Companion.retrofit.create(serviceClass)
        }
    }


}
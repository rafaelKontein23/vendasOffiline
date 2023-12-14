package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit_Carga {

    companion object {
         val url =
            URLs.urlCarga


         val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())

         val retrofit = builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return retrofit.create(serviceClass)
        }
    }


}
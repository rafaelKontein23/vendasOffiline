package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit_Carga {

    companion object {
         val url = "https://wwwe.catarinenseonline.com.br/"


         val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())

         val retrofit = builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return retrofit.create(serviceClass)
        }
    }


}
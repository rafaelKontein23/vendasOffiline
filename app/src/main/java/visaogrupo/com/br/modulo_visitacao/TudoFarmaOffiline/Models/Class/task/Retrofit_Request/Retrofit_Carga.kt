package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Retrofit_Request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitModelo {

    companion object {
        val url =
            visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs.Companion.url


        val builder = Retrofit.Builder().baseUrl(visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.RetrofitModelo.Companion.url)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.RetrofitModelo.Companion.builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.RetrofitModelo.Companion.retrofit.create(serviceClass)
        }
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitModelo {

    companion object {
        val url = URLs.url


        val builder = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return retrofit.create(serviceClass)
        }
    }
}
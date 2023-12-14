package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit_URL () {

        private val url = URLs.url

        private val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build()

        private val builder = Retrofit.Builder().client(client).baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())

        private val retrofit = builder.build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return retrofit.create(serviceClass)
        }


}
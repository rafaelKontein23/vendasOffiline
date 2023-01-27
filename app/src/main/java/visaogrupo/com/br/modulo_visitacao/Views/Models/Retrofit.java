package visaogrupo.com.br.modulo_visitacao.Views.Models;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {


    private static final String url = "https://wwwi.visaogrupo.com.br/ws/";

    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();

    private static final retrofit2.Retrofit.Builder builder = new retrofit2.Retrofit.Builder().client(client).baseUrl(url).addConverterFactory(GsonConverterFactory.create());

    private static final retrofit2.Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}

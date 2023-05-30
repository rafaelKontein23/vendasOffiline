package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Insync_Task {

    @POST("ache/login")
    Call<ResponseBody> P_Login_factory(@Body RequestBody body);



}

package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Insync_Task {

    @POST("hertz/novo/login")
    Call<ResponseBody> P_Login_Pasta(@Body RequestBody body);



    @POST("catarinense/modelopedido")
    Call<ResponseBody> P_RetornaListaDesejoTablet(@Body RequestBody body);




}

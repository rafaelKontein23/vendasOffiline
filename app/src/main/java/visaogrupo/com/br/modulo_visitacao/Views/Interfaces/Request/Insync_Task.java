package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import visaogrupo.com.br.modulo_visitacao.Views.Models.Progressiva;

public interface Insync_Task {

    @POST("catarinense/login")
    Call<ResponseBody> P_Login_factory(@Body RequestBody body);



    @POST("catarinense/modelopedido")
    Call<ResponseBody> P_RetornaListaDesejoTablet(@Body RequestBody body);




}

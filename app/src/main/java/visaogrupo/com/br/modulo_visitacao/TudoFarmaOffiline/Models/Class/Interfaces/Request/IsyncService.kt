package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IsyncService {




    @POST("catarinense/envio/pedido")
    suspend fun enviarPedido(@Body body: RequestBody): Response<ResponseBody>

}
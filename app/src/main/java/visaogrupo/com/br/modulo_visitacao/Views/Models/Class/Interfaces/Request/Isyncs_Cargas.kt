package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request


import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url
import retrofit2.Call
import retrofit2.http.Path


interface Isyncs_Cargas {



    @Streaming
    @GET
    fun downloadFile(@Url fileUrl:String): Call<ResponseBody>



    @GET ("padrao/PrazoMedioXValor.json")
    fun PrazoMedio():Call<ResponseBody>

    @GET ("FormaPagamentoXCNPJ/{uf}/{cnpj}.json")
    fun FormaDePagExclus(@Path("uf") uf:String, @Path("cnpj") cnpj:String):Call<ResponseBody>
}
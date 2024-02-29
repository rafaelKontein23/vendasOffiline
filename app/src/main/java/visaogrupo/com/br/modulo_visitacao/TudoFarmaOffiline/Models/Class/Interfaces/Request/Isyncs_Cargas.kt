package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Request


import okhttp3.ResponseBody
import retrofit2.http.GET
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

    @GET ("docs/tablet/carga/Progressivas/Grupos_{lojaID}_{UF}_{codSync}.json")
    fun grupolojaAb(@Path("lojaID") lojaid:String,@Path("UF") uf :String,@Path("codSync") codSync:String):Call<ResponseBody>

    @GET ("docs/tablet/carga/Progressivas/GrupoProgressiva_{lojaId}.json")
    fun grupoProgressiva(@Path("lojaId") lojaId:Int):Call<ResponseBody>
}
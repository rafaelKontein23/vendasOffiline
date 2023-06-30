package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.ConstroiJsonPedido
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.Incript
import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*


class taskEnviaPedido {

   @RequiresApi(Build.VERSION_CODES.O)
   suspend fun eviarPedido(list: MutableList<Carrinho>, context: Context, OPL:String, Observacao :String, formPag:String){
           val constroiJsonPedido = ConstroiJsonPedido()
           val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
           val gson = Gson()
           val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
           val  clientesSelecionado =  gson.fromJson(objetoSerializadoCliente, Clientes::class.java)
           val gsonuserid = Gson()
           val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
            val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)

           var ( json,chave ) = constroiJsonPedido.envairPedidoJson(list,context,OPL,Observacao,formPag)
           chave = chave.replace("/","").replace(":","")
           val path = java.lang.String.format("%s\\%s", clientesSelecionado.CNPJ, login.Usuario_id)
           val  incript = Incript()
           json = json.replace("\n","")
           json = json.replace("\t","")
           val arquivo = encryptToBase64(json)
           val jsonObject = JSONObject()
           jsonObject.put("ServerPath",path +"\\")
           jsonObject.put("File", arquivo)
           jsonObject.put("FileName", java.lang.String.format("%s.json", chave))

           val bodyJson = incript.encryptCBC(jsonObject.toString())

          try {
              val client = OkHttpClient().newBuilder()
                  .build()
              val mediaType = MediaType.parse("application/json")
              val body = RequestBody.create(
                  mediaType,
                  bodyJson
              )
              val request: Request = Request.Builder()
                  .url("https://wwwe.visaogrupo.com.br/ws/catarinense/envio/pedido")
                  .method("POST", body)
                  .addHeader("Autorizacao", "J0#o14:*6")
                  .addHeader("Content-Type", "application/json")
                  .build()
              try {
                  val response = client.newCall(request).execute()

                  val descript = incript.decryptCBC(response.body()!!.string().toString())
                  Log.d("descript :", descript)
              } catch (e: IOException) {
                  e.printStackTrace()
              }
          }catch (e :Exception){
              e.printStackTrace()
          }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptToBase64(text: String): String {
        val textBytes = text.toByteArray(StandardCharsets.UTF_8)
        val encryptedBytes = Base64.getEncoder().encode(textBytes)
        return String(encryptedBytes, StandardCharsets.UTF_8)
    }

}
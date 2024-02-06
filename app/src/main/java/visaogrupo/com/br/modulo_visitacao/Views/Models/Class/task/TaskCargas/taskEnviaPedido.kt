package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.PedidoSucesso
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.ConstroiJsonPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Incript
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.MontaHeader
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*


class taskEnviaPedido {

   @RequiresApi(Build.VERSION_CODES.O)
   suspend fun eviarPedido(pedidoFinalizado:PedidoFinalizado, context: Context,):Triple<Int,String,String>{
           val constroiJsonPedido = ConstroiJsonPedido()
           val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

           val gsonuserid = Gson()
           val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
           val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
           val pedidoDao  =PedidosFinalizadosDAO(context)
           val listaProdutos=  pedidoDao.listarPedidosProdutos(pedidoFinalizado.pedidoID)
           val stringSeparada = pedidoFinalizado.operadorLogistico

           val listaOpls= stringSeparada.split(",")


          var ( json,chave ) = constroiJsonPedido.envairPedidoJson(listaProdutos, context,listaOpls,pedidoFinalizado)
           chave = chave.replace("/","").replace(":","")
           val path = java.lang.String.format("%s\\%s",pedidoFinalizado.cnpj, login.Usuario_id)
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
              var heraderVersion = MontaHeader.montaHeader(pedidoFinalizado.usuarioId.toString())
              heraderVersion = heraderVersion.replace("\n","")
              val request: Request = Request.Builder()
                  .url(URLs.urlEnviaPedido)
                  .method("POST", body)
                  .addHeader("Autorizacao", "J0#o14:*6")
                  .addHeader("Usuario", heraderVersion)
                  .addHeader("Content-Type", "application/json")
                  .build()
              try {
                  val response = client.newCall(request).execute()

                  val descript = incript.decryptCBC(response.body()!!.string().toString())
                  val jsonRetornoPedido = JSONObject(descript)
                  val  valido = jsonRetornoPedido.getInt("Valido")
                  val  menssagem = jsonRetornoPedido.getString("Mensagem")
                  Log.d("descript :", descript)
                  return Triple(valido, menssagem,chave)
              } catch (e: IOException) {
                  e.printStackTrace()
              }
          }catch (e :Exception){
              e.printStackTrace()
          }
       return Triple(0, "Algo deu errado no momento","")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptToBase64(text: String): String {
        val textBytes = text.toByteArray(StandardCharsets.UTF_8)
        val encryptedBytes = Base64.getEncoder().encode(textBytes)
        return String(encryptedBytes, StandardCharsets.UTF_8)
    }

}
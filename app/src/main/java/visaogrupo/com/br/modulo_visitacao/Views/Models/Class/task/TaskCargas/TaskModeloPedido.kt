package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Insync_Task
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ModeloPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Incript
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.ModeloDePedidoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.RetrofitModelo
import java.text.SimpleDateFormat
import java.util.*

class TaskModeloPedido {

    fun requestModelo(context: Context,  user_ide:String, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon: ImageView, animador: ObjectAnimator, terminouCarga: TerminouCarga){
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gsonuserid = Gson()
            val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
            val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
            val downloadService = RetrofitModelo.createService(Insync_Task::class.java)
            val json = JSONObject()
            json.put("CodigoUsuario",login.Usuario_id)

            val incript = Incript()
            val jsonincriptada = incript.encryptCBC(json.toString())
            val mediaType = MediaType.parse("")
            val responseBody = RequestBody.create(mediaType,jsonincriptada)
            val call: Call<ResponseBody> = downloadService.P_RetornaListaDesejoTablet(responseBody)
            var  response = call.execute()

            if(response.isSuccessful){
                val  respostaModelo =  response.body()?.string()
                val descript = incript.decryptCBC(respostaModelo.toString())
                val jsonRetorno = JSONObject(descript)

                //Verifica se é valido
                if (jsonRetorno.getInt("Valido") == 1) {
                    //Obtem json do usuario
                    val jsonArrayWishList = jsonRetorno.getJSONArray("Dados")

                    //Percorro todos os modelos encontrados para o usuario
                    for (i in 0 until jsonArrayWishList.length()) {
                        for (j in 0 until jsonArrayWishList.length()) {
                            val jsonWishList: JSONObject = jsonArrayWishList.getJSONObject(j)

                            val nomeModeloPedido = jsonWishList.getString("Nome")
                            val clienteId = jsonWishList.getInt("EmpresaId")

                            val produtosArray = jsonWishList.getJSONArray("Produtos")

                            for (k in 0 until produtosArray.length()) {
                                 try {
                                     val produto = produtosArray.getJSONObject(k)
                                     val codigoProduto = produto.getInt("Produto_codigo")
                                     val quantidade = produto.getInt("Quantidade")



                                     val modeloPedido = ModeloPedido(login.Usuario_id!!.toInt(),nomeModeloPedido,clienteId,0,codigoProduto,quantidade,0,0.0)

                                     val modeloPedidoDao = ModeloDePedidoDAO(context)
                                     modeloPedidoDao.insert(modeloPedido)
                                 }catch (e:Exception){
                                     e.printStackTrace()
                                 }

                            }
                        }

                    }
                    val drawable = icon.drawable
                    CoroutineScope(Dispatchers.Main).launch {

                        constrain.background = ContextCompat.getDrawable(context, R.drawable.cargaacbou)
                        texttitulocarga.setTextColor(Color.parseColor("#64BC26"))
                        subtitulocarga.setTextColor(Color.parseColor("#64BC26"))
                        subtitulocarga.text ="atualizou."
                        animador.end()

                        val color = ContextCompat.getColor(context, R.color.textacaboucarga)
                        val mutableDrawable = DrawableCompat.wrap(drawable).mutate()
                        DrawableCompat.setTint(mutableDrawable, color)
                        icon.setImageDrawable(mutableDrawable)
                        icon.background = ContextCompat.getDrawable(context, R.drawable.cargaacbou)

                    }
                    Thread.sleep(10000)
                    CoroutineScope(Dispatchers.Main).launch {

                        val colorcorazultext = ContextCompat.getColor(context, R.color.corazultext)
                        val mutableDrawableicon = DrawableCompat.wrap(drawable).mutate()
                        DrawableCompat.setTint(mutableDrawableicon, colorcorazultext)
                        icon.setImageDrawable(mutableDrawableicon)
                        icon.background = ContextCompat.getDrawable(context, R.drawable.bordasimagenscargas)
                        constrain.background = ContextCompat.getDrawable(context, R.drawable.bordascargas)
                        texttitulocarga.setTextColor(Color.parseColor("#21262F"))
                        subtitulocarga.setTextColor(Color.parseColor("#737880"))
                        val currentDate: String = SimpleDateFormat("dd/MMyyyy", Locale.getDefault()).format(
                            Date()
                        )
                        subtitulocarga.text ="atualizado em: ${currentDate} "

                    }
                    Log.d("Terminou"," carga de Modelos")
                }
            }else{
                Log.d("faa", response.errorBody().toString())
            }
        }

    }
}
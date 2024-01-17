package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task

import android.content.Context
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Enuns.Login_Erro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.Ondismiss
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Request.Insync_Task
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Incript
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.Retrofit_URL
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


class Task_Login {
    val incript = Incript()


    @Throws(
        JSONException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchPaddingException::class,
        IllegalBlockSizeException::class,
        NoSuchAlgorithmException::class,
        BadPaddingException::class,
        InvalidKeyException::class
    )
    fun Request_login(email: String?, senha: String?, device: String?, ondismiss: Ondismiss, context:Context) {
        var enum = Login_Erro.Erro
        var retro = Retrofit_URL()
        val isyncs_tasks: Insync_Task = retro.createService(Insync_Task::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("Senha", senha)
        jsonObject.put("IP", "Teste")
        jsonObject.put("Origem", "app")
        jsonObject.put("Device", "")
        val mensagemErro = arrayOf("")


        val mensagemIncript = incript.encryptCBC(jsonObject.toString())

        val mediaType = MediaType.parse("text/plain; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, mensagemIncript)
        val call = isyncs_tasks.P_Login_Pasta(requestBody)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val responsedata = incript.decryptCBC(response.body()!!.string())
                        val jsonObjectResponse = JSONObject(responsedata)
                        mensagemErro[0] = jsonObjectResponse.getString("Mensagem")
                        val jsonObjectDados = jsonObjectResponse.getJSONObject("Dados")
                        val jsonArrayresponse = jsonObjectDados.getJSONArray("LOGIN")

                        for (i in 0 until jsonArrayresponse.length()) {
                            val jsonObjectresponse = jsonArrayresponse.optJSONObject(i)
                            val mensagem = jsonObjectresponse.getString("Mensagem")
                            if (mensagem.isEmpty()) {
                                val usuarioId = jsonObjectresponse.getString("Usuario_id")
                                val nome = jsonObjectresponse.getString("Nome")
                                val login = Login(
                                    usuarioId, nome, email,senha,"",
                                    "","","","",
                                    "","","","","","" )

                                val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                                val gson = Gson()
                                val objetoSerializado = gson.toJson(login)
                                val editor = sharedPreferences.edit()
                                editor.putString("UserLogin", objetoSerializado)
                                editor.apply()
                                enum = Login_Erro.Sucesso
                                ondismiss.ondismiss(enum)
                            } else {

                                val dialogErro = DialogErro()
                                dialogErro.Dialog(context, "Atenção", mensagem,"aceitar","cancelar",){}
                            }

                            break
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        enum = Login_Erro.Erro
                        ondismiss.ondismiss(enum)

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                enum = Login_Erro.Erro
                ondismiss.ondismiss(enum)
            }
        })
    }

}



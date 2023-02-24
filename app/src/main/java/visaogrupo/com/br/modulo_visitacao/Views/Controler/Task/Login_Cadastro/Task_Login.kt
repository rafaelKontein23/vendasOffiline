package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.Login_Cadastro

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Atividade_Cadastro_Login.Act_Kotlin
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Dialogs.Dialog_erro
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Obejtos.Cadastro.Login
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.Incript
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.Ondismiss
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request.Insync_Task
import visaogrupo.com.br.modulo_visitacao.Views.Models.Enuns.Enuns_Cadastro.Login_Erro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Retrofit_Request.Retrofit_URL
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
    fun Request_login(email: String?, senha: String?, device: String?,ondismiss:Ondismiss) {
        var enum = Login_Erro.Erro
        var retro = Retrofit_URL()
        val isyncs_tasks: Insync_Task = retro.createService(Insync_Task::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("Login", email)
        jsonObject.put("Senha", senha)
        jsonObject.put("DeviceToken", device)

        val mensagemIncript = incript.encryptCBC(jsonObject.toString())

        val mediaType = MediaType.parse("text/plain; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, mensagemIncript)
        val call = isyncs_tasks.P_Login_factory(requestBody)
        call!!.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val json_resposta: String = incript.decryptCBC(response.body()!!.string())



                        Log.d("Json", json_resposta)
                        val jsonObject1 = JSONObject(json_resposta)
                        val Dados = jsonObject1.getString("Dados")
                        val jsonArray = JSONArray(Dados)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject2 = jsonArray.optJSONObject(i)
                            val Usuario_id = jsonObject2.getString("Usuario_id")
                            val Nome = jsonObject2.getString("Nome")
                            val Email = jsonObject2.getString("Email")
                            val Senha = jsonObject2.getString("Senha")
                            val UF = jsonObject2.getString("UF")
                            val Linha_id = jsonObject2.getString("Linha_id")
                            val LinhaVendedor = jsonObject2.getString("LinhaVendedor")
                            val Institucional = jsonObject2.getString("Institucional")
                            val Setor = jsonObject2.getString("Setor")
                            val FlagMerchan = jsonObject2.getString("FlagMerchan")
                            val AlterarSenha = jsonObject2.getString("AlterarSenha")
                            val Teste = jsonObject2.getString("Teste")
                            val TipoCadastro_id = jsonObject2.getString("TipoCadastro_id")
                            val Feriado = jsonObject2.getString("Feriado")
                            val FinalDeSemana = jsonObject2.getString("FinalDeSemana")
                            val login = Login(Usuario_id, Nome, Email, Senha, UF, Linha_id, LinhaVendedor, Institucional, Setor, FlagMerchan, AlterarSenha, Teste, TipoCadastro_id, Feriado, FinalDeSemana)
                            enum = Login_Erro.Sucesso
                            ondismiss.ondismiss(enum)


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



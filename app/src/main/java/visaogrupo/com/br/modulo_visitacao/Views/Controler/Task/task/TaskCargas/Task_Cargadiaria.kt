package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Request.Isyncs_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request.Retrofit_Carga
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.Funcao_erro
import java.io.*


class  Task_Cargadiaria {

    suspend fun Cargadiaria(userid:String, context:Context ):String{
        var  path =""
        try {

            val downloadService = Retrofit_Carga.createService(Isyncs_Cargas::class.java)
            val call:Call<ResponseBody> = downloadService.downloadFile("docs/tablet/carga/vendas/zip_${userid}.zip")
            var  response = call.execute()
            if (response.isSuccessful){

                path = saveToDisk(response.body()!!,context)
                Log.d("Finalizado","")

            }else{
                response.errorBody()
                var dialogerro = DialogErro()
                dialogerro.Dialog(context,"Atenção","Algo deu errado com a carga","OK","",
                    Funcao_erro()
                )
                Log.d("Response",response.errorBody().toString())
                return path
            }
        }catch (e:Exception){
            e.printStackTrace()
            return path
        }
      return path
    }

   suspend fun saveToDisk(body: ResponseBody, context: Context):String {
       var path = ""
        try {
            File("/data/data/" + context.packageName + "/carga").mkdir() // Para criar a pasta!
            var destinationFile = File("/data/data/" + context.packageName + "/carga/cargadiaria.zip")
            path= destinationFile.path


            var `is`: InputStream? = null
                var os: OutputStream? = null
                try {
                    Log.d("sa", "File Size=" + body.contentLength())
                    `is` = body.byteStream()
                    os = FileOutputStream(destinationFile)
                    val data = ByteArray(4096)
                    var count: Int
                    var progress = 0
                    while (`is`.read(data).also { count = it } != -1) {
                        os.write(data, 0, count)
                        progress += count
                        Log.d(
                            "Salavndo zip...",
                            "Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress.toFloat() / body.contentLength()
                        )
                    }
                    os.flush()
                    Log.d("Task_CargaDiaria", "Zip Salvo com sucesso")
                } catch (e: IOException) {
                    e.printStackTrace()
                    var dialogerro = DialogErro()
                    dialogerro.Dialog(context,"Atenção","Algo deu errado ao salvar o zip","OK","",
                        Funcao_erro()
                    )
                    Log.d("Task_CargaDiaria", "Failed to save the file!")
                    return ""
                } finally {
                    withContext(Dispatchers.Main) {
                        if (`is` != null) `is`.close()
                        if (os != null) os.close()
                    }
                }

        } catch (e: IOException) {
            e.printStackTrace()
            var dialogerro = DialogErro()
            dialogerro.Dialog(context,"Atenção","Algo deu errado ao salvar o zip","OK","",
                Funcao_erro()
            )
            Log.d("Task_CargaDiaria", "Failed to save the file!")
            return ""
        }
       return path
    }
}

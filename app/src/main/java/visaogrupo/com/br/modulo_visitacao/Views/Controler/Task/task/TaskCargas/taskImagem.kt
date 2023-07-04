package visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas


import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.codec.binary.Base64.encodeBase64String
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request.URLs
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class taskImagem (context:Context){


    val db = DataBaseHelber(context)

    fun requestImagem(context: Context, user_ide:String, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon: ImageView, animador: ObjectAnimator, terminouCarga: TerminouCarga){
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val excluiTabela = "DELETE FROM TB_Imagens"
                db.writableDatabase.execSQL(excluiTabela)
                Log.d("Exclui","Imagens")
                val query ="SELECT barra from TB_produtos"
                val cursor = db.writableDatabase.rawQuery(query,null)

                while (cursor.moveToNext()){
                    try {
                        val barra = cursor.getString(0)
                        val imageUrl = URL("${URLs.urlimagens}${barra}.jpg")
                        val connection = imageUrl.openConnection()

                        val inputStream = connection.getInputStream()
                        val outputStream = ByteArrayOutputStream()

                        inputStream.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }

                        val imageBytes = outputStream.toByteArray()
                        val imagembase64 = encodeBase64String(imageBytes)

                        val valores = ContentValues()

                        valores.put("imagembase64",imagembase64)
                        valores.put("barra",barra)

                        db.writableDatabase.insert("TB_Imagens",null, valores)
                        Log.d("Insert","${barra}")
                    }catch (e:Exception){
                        e.printStackTrace()
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
                Log.d("Terminou"," carga de imagens")
            }catch (e:Exception){
                e.printStackTrace()
            }


        }

    }
}
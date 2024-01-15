package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas


import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.codec.binary.Base64.encodeBase64String
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.Retrofit_Request.URLs
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.HoraAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.DataBaseHelber
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class taskImagem (context:Context){


    val db = DataBaseHelber(context)

    fun requestImagem(context: Context, user_ide:String, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon: ImageView, animador: ObjectAnimator, terminouCarga: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga){
        CoroutineScope(Dispatchers.IO).launch {
          val requestProdutoImagem = launch {
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
                          Glide.with(context)
                              .asBitmap()
                              .load(imageUrl)
                              .into(object : CustomTarget<Bitmap>() {
                                  override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                      val outputStream = ByteArrayOutputStream()
                                      resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                                      val imageBytes = outputStream.toByteArray()
                                      val imagembase64 = encodeBase64String(imageBytes)

                                      val valores = ContentValues()

                                      valores.put("imagembase64",imagembase64)
                                      valores.put("barra",barra)

                                      db.writableDatabase.insert("TB_Imagens",null, valores)
                                      Log.d("Insert", barra)
                                  }

                                  override fun onLoadCleared(placeholder: Drawable?) {
                                  }
                              })



                          Log.d("Insert","${barra}")
                      }catch (e:Exception){
                          e.printStackTrace()
                      }

                  }
              }catch (e:Exception){
                  e.printStackTrace()
              }
          }
            val requestLojasImagem = launch {
                try {

                    val excluiTabela = "DELETE FROM TB_ImagensLojas"
                    db.writableDatabase.execSQL(excluiTabela)
                    Log.d("Exclui","Imagens Lojas")
                    val query ="SELECT  DISTINCT logohome,loja_id FROM TB_lojas "
                    val cursor = db.writableDatabase.rawQuery(query,null)

                    while (cursor.moveToNext()){
                        try {
                            val logoHome = cursor.getString(0)
                            val lojaId =  cursor.getInt(1)
                            val imageUrl = URL("${URLs.urlImagensLojas}${logoHome}")
                            Glide.with(context)
                                .asBitmap()
                                .load(imageUrl)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        val outputStream = ByteArrayOutputStream()
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                                        val imageBytes = outputStream.toByteArray()
                                        val imagembase64 = encodeBase64String(imageBytes)

                                        val valores = ContentValues()

                                        valores.put("imagembase64",imagembase64)
                                        valores.put("logoHome",logoHome)
                                        valores.put("loja_id", lojaId)

                                        db.writableDatabase.insert("TB_ImagensLojas",null, valores)
                                        Log.d("Insert", logoHome)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                    }
                                })



                            Log.d("Insert","${logoHome}")
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
         requestProdutoImagem.join()
         requestLojasImagem.join()

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
                delay(5000)
                CoroutineScope(Dispatchers.Main).launch {

                    val colorcorazultext = ContextCompat.getColor(context, R.color.corazultext)
                    val mutableDrawableicon = DrawableCompat.wrap(drawable).mutate()
                    DrawableCompat.setTint(mutableDrawableicon, colorcorazultext)
                    icon.setImageDrawable(mutableDrawableicon)
                    icon.background = ContextCompat.getDrawable(context, R.drawable.bordasimagenscargas)
                    constrain.background = ContextCompat.getDrawable(context, R.drawable.bordascargas)
                    texttitulocarga.setTextColor(Color.parseColor("#21262F"))
                    subtitulocarga.setTextColor(Color.parseColor("#737880"))
                    val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                        Date()
                    )
                    subtitulocarga.text ="atualizado em: ${currentDate} ${HoraAtual.horaAtual()}"

                }
                Log.d("Terminou"," carga de imagens")


        }

    }
}
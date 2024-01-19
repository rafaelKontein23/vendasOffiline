package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas

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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.ExcluiDados
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.HoraAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.PushNativo
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.DataBaseHelber
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentCargas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskEstoqueSeparado {

    fun requestEstoque(context: Context, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon: ImageView, animador: ObjectAnimator, terminouCarga: TerminouCarga) {
        val estoqueExcluir = ExcluiDados(context)
        estoqueExcluir.excluidaEstoque()

        CoroutineScope(Dispatchers.IO).launch {

            val lendoEstoque = launch {
                val dblistaProgre = DataBaseHelber(context)

                Log.d("Começou o Estoque","")
                val lojasOP = "SELECT CodEstoque FROM TB_clientes"


                val curso = dblistaProgre.writableDatabase.rawQuery(lojasOP,null)
                var count =0
                val jsonarayEstoque: MutableList<JSONArray>? = mutableListOf()
                val coroutines = mutableListOf<Deferred<Unit>>()
                while (curso.moveToNext()){

                    val codigoEstoque = curso.getInt(0)
                    count = count +1

                    Log.d("Quantidade list estoque",count.toString())
                    val lendEstoque =   async{
                        val taskProgressivas = TaskEstoque()
                        val jsonArray =taskProgressivas.recuperaEstoque(codigoEstoque)
                        if (jsonArray != null) {
                            jsonarayEstoque?.add(jsonArray)
                        }
                    }
                    coroutines.add(lendEstoque)



                }

                runBlocking {
                    coroutines.awaitAll()
                }

                Log.d("Quantidadede estoque", jsonarayEstoque?.size.toString())

                val db_Estoque= DataBaseHelber(context).writableDatabase
                db_Estoque.beginTransaction()
                try {
                    for ( i in 0 until  jsonarayEstoque!!.size){
                        val valoresEstoque = ContentValues()
                        val jsonArrayAtual = jsonarayEstoque[i]

                        for ( j in 0 until  jsonArrayAtual.length()){
                            val jsonClientesPorLojasRetorno = jsonArrayAtual.optJSONObject(j)

                            valoresEstoque.put("EAN", jsonClientesPorLojasRetorno.optString("EAN"))
                            valoresEstoque.put("Quantidade", jsonClientesPorLojasRetorno.optInt("Quantidade"))
                            valoresEstoque.put("Centro", jsonClientesPorLojasRetorno.optInt("Centro"))
                            db_Estoque.insert("TB_Estoque",null,valoresEstoque)

                        }

                    }
                    db_Estoque.setTransactionSuccessful()
                }catch (e:Exception){
                    e.printStackTrace()
                }finally {
                    db_Estoque.endTransaction()
                }
                FragmentCargas.progresspush += 3
                PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","veja o progresso da carga")
            }
            lendoEstoque.join()
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
        }
    }
}
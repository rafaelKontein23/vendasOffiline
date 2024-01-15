package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FiltroPrincipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FiltroProduto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Filtros
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.*
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskEstoque
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskFiltro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskFiltroPrincipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskFiltroProduto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskFormaDePagamentoExclusiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskProgressivas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskRegraPrazoMedio
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.Task_Cargadiaria
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentCargas
import java.text.SimpleDateFormat
import java.util.*

class CargaDiaria {


    fun fazCargaDiaria(context: Context, user_ide:String, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon:ImageView, animador: ObjectAnimator,terminouCarga: TerminouCarga){
        CoroutineScope(Dispatchers.IO).launch {
            //Faz request de Zip

            val patch = Task_Cargadiaria().Cargadiaria(user_ide,context )

            // Atualiza progress
            FragmentCargas.progresspush += 1
            FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","procurando Zip...")


            Log.d("Caminho Zip","${patch}")
            if(!patch.isEmpty()){
                val excluiDadosTabelas = ExcluiDados(context)
                excluiDadosTabelas.exluidados()
                val lerZip = LerZip()
                val lojasDAO = LojasDAO(context)
                val clientesDAO = ClientesDAO(context)
                val protudosDAO = ProdutosDAO(context)
                val formaDePagamentoDAO = FormaDePagamentoDAO(context)
                val operadorLogisticoDAO = OperadorLogisticaDAO(context)
                var jsonLojas = ""
                var jsonClienets = ""
                var jsonProtudos = ""
                var jsonFormaDePagamento = ""
                var jsonOperadorLogistico = ""


                try {


                    //grava no banco lojas
                    val lendoLojas =launch {
                        //Lendo Arquivo de Loja
                        jsonLojas = lerZip.readTextFileFromZip(patch,"Lojas.json","Lojas").toString()
                        val jsonObjectLojas: JSONObject = JSONObject(jsonLojas)
                        val jsonArray: JSONArray = JSONArray(jsonObjectLojas.getString("LOJAS"))
                        val islojasdb = lojasDAO.insert(jsonArray)

                        if(islojasdb){
                            Log.d("Terminou lojas","")
                            // atuliza progress push
                             FragmentCargas.progresspush += 1
                             FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando lojas...")
                        }

                    }
                    //grava no banco clientes e Loja por cliente
                    val lendoClientes = launch {
                        //Lendo Arquivo de Cliente
                        jsonClienets = lerZip.readTextFileFromZip(patch,"Clientes.json","Clientes").toString()
                        val jsonObjectClientes = JSONObject(jsonClienets)
                        val jsonObjectClientesUser = JSONObject(jsonObjectClientes.getString("USUARIO"))
                        val jsonArrayCliente = JSONArray(jsonObjectClientesUser.getString("EMPRESA"))
                        val isCliente = clientesDAO.insert(jsonArrayCliente)
                        if (isCliente){
                            FragmentCargas.progresspush += 1
                            FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando clientes...")
                            Log.d("Terminou Clientes","")
                        }
                    }

                    val lendoPrazoMedio = launch {
                        val taskRegraPrazoMedio = TaskRegraPrazoMedio()
                        val listaRetorna  = taskRegraPrazoMedio.tasKRegra()
                        val regraPrazoMedio = RegraPrazoDAO(context)
                        regraPrazoMedio.insertRegraProgressiva(listaRetorna)
                    }
                    //grava no banco produtos
                    val  lendoProdutos = launch {
                        jsonProtudos = lerZip.readTextFileFromZip(patch,"Produtos.json","Protudos").toString()
                        val jsonObjectProtudos = JSONObject(jsonProtudos)
                        val jsonArrayProtudos = JSONArray(jsonObjectProtudos.getString("PRODUTOS"))
                        val isProtudos = protudosDAO.insert(jsonArrayProtudos)
                        if (isProtudos){
                            FragmentCargas.progresspush += 1
                            FragmentCargas.showNotification(context,"2","Titulo1","")
                            Log.d("Terminou Protudos","")
                        }

                    }
                    // faz as tarefas execulta juntas
                    lendoLojas.join()
                    lendoClientes.join()
                    lendoProdutos.join()
                    lendoPrazoMedio.join()







                  Log.d("Iniciou Segunda Parte","")
                  val LendoFormaDePagamentoExclusiva = launch {
                      var listaCnpj = mutableListOf<Clientes>()
                      val queryCnpjs = "SELECT * FROM TB_clientes WHERE formapagamentoexclusiva = 1"
                      val clientesDAO = ClientesDAO(context)
                      listaCnpj = clientesDAO.listar(context,queryCnpjs)
                      val taskFormaDePagamentoExclusiva = TaskFormaDePagamentoExclusiva()
                      for (i in listaCnpj){
                          val listFormaDePagamentoExclusiva = taskFormaDePagamentoExclusiva.taskFormaDePagamentos(i.UF,i.CNPJ)
                          val formaDePagamentoExcluisiv = FormaDePagamentoExclusivaDAO(context)
                          formaDePagamentoExcluisiv.insert(listFormaDePagamentoExclusiva)
                      }




                  }
                  val lendoFormasDePagamento = launch {

                        val dblista = DataBaseHelber(context)

                        val LojasPOrClientesList  = "SELECT distinct lojasPorCliente.loja_id " +
                                "FROM TB_lojas lojasPorCliente" +
                                " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id"

                        val curso = dblista.readableDatabase.rawQuery(LojasPOrClientesList,null)

                        while (curso.moveToNext()){

                            try {
                                val lojaID =  curso.getInt(0)
                                // Começp de Forma de pagamento
                                jsonFormaDePagamento = lerZip.readTextFileFromZip(patch, "FormaPagamento_${lojaID}.json", "FormaDePAgamento").toString()
                                val jsosonform = JSONObject(jsonFormaDePagamento)

                                val jsonArrayFormaDePag = JSONArray(jsosonform.getString("FORMAS"))
                                formaDePagamentoDAO.insert(jsonArrayFormaDePag,lojaID)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        FragmentCargas.progresspush += 1
                        FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando Formas de pagamentos...")

                        Log.d("Terminou"," FormaDePagamento")
                    }
                    val lendoOperadorLogistico = launch {
                        val dblistaOP = DataBaseHelber(context)

                        Log.d("Começou o OPL","")
                        val LojasOP = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf" +
                                " FROM TB_lojas lojasPorCliente" +
                                " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                " order by 1"

                        val curso = dblistaOP.readableDatabase.rawQuery(LojasOP,null)
                        while (curso.moveToNext()){
                            val lojaID = curso.getInt(0);
                            val ufs = curso.getString(1)
                            jsonOperadorLogistico = lerZip.readTextFileFromZip(patch,"OperadorLogistico_${lojaID}.json","OperdorLogistico").toString()
                            val  jsonOP = JSONObject(jsonOperadorLogistico)
                            val jsonArrayOperadorLogistico = JSONArray(jsonOP.getString("OPERADORES"))
                            operadorLogisticoDAO.insert(jsonArrayOperadorLogistico,lojaID,ufs)
                        }
                        FragmentCargas.progresspush += 1
                        FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando opls...")

                        Log.d("Terminou"," OPL")

                    }

                    lendoFormasDePagamento.join()
                    lendoOperadorLogistico.join()
                    LendoFormaDePagamentoExclusiva.join()

                    Log.d("Começou "," Terceira parte")

                     val  lendoProgressiva = launch {
                              val dblistaProgre = DataBaseHelber(context)

                              Log.d("Começou o Progressiva","")
                              val lojasOP = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf, Clientes.codigo" +
                                      " FROM TB_lojas lojasPorCliente" +
                                      " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                      " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                      " order by 1"


                              val curso = dblistaProgre.writableDatabase.rawQuery(lojasOP,null)
                              var count =0
                              val jsonarayProgressiva: MutableList<JSONArray>? = mutableListOf()
                              val coroutines = mutableListOf<Deferred<Unit>>()
                              while (curso.moveToNext()){
                                  val  uf = curso.getString(1)
                                  val loja_id = curso.getInt(0)
                                  val  codigoProgressiva = curso.getInt(2)
                                  count = count +1

                                  Log.d("Progress",count.toString())
                                  val lendpo =   async{
                                      val taskProgressivas = TaskProgressivas(context)
                                      val jsonArray =taskProgressivas.recuperaProgressiva(loja_id,uf,codigoProgressiva)


                                      if (jsonArray != null) {
                                          jsonarayProgressiva?.add(jsonArray)
                                      }
                                  }
                                  coroutines.add(lendpo)



                              }

                              runBlocking {
                                  coroutines.awaitAll()
                              }

                              Log.d("Terminou Alou", jsonarayProgressiva?.size.toString())
                              Log.d("Terminou","Progressiva")

                              FragmentCargas.progresspush += 1
                              FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando progressivas...")

                              val db_Progreesivas = DataBaseHelber(context).writableDatabase
                              db_Progreesivas.beginTransaction()
                              try {
                                  for ( i in 0 until  jsonarayProgressiva!!.size){
                                      val valoresProgresiva = ContentValues()
                                      val jsonArrayAtual = jsonarayProgressiva[i]

                                      for ( j in 0 until  jsonArrayAtual.length()){
                                          val jsonClientesPorLojasRetorno = jsonArrayAtual.optJSONObject(j)

                                          valoresProgresiva.put("Prod_cod", jsonClientesPorLojasRetorno.optInt("Prod_cod"))
                                          valoresProgresiva.put("Loja_id", jsonClientesPorLojasRetorno.optInt("Loja_id"))
                                          valoresProgresiva.put("Valor", jsonClientesPorLojasRetorno.optDouble("Valor"))
                                          valoresProgresiva.put("Quantidade", jsonClientesPorLojasRetorno.optInt("Quantidade"))
                                          valoresProgresiva.put("QuantidadeMaxima", jsonClientesPorLojasRetorno.optInt("QuantidadeMaxima"))
                                          valoresProgresiva.put("Desconto", jsonClientesPorLojasRetorno.optDouble("Desconto",0.0))
                                          valoresProgresiva.put("Desconto_Min", jsonClientesPorLojasRetorno.optDouble("Desconto_Min",0.0))
                                          valoresProgresiva.put("DescontoMaximo", jsonClientesPorLojasRetorno.optDouble("DescontoMaximo",0.0))
                                          valoresProgresiva.put("PF", jsonClientesPorLojasRetorno.optDouble("PF",0.0))
                                          valoresProgresiva.put("PMC", jsonClientesPorLojasRetorno.optDouble("PMC", 0.0))
                                          valoresProgresiva.put("ST", jsonClientesPorLojasRetorno.optDouble("ST",0.0))
                                          valoresProgresiva.put("formalizacao", jsonClientesPorLojasRetorno.optString("formalizacao","dsas"))
                                          valoresProgresiva.put("Seq_Kit", jsonClientesPorLojasRetorno.optInt("Seq_Kit",  0))
                                          valoresProgresiva.put("Seq_Cond_Coml", jsonClientesPorLojasRetorno.optInt("Seq_Cond_Coml", 0))
                                          valoresProgresiva.put("UF", jsonClientesPorLojasRetorno.optString("UF"))
                                          valoresProgresiva.put("Data_Vencimento", jsonClientesPorLojasRetorno.optString("Data_Vencimento","dveio Nada"))
                                          valoresProgresiva.put("Prioridade", jsonClientesPorLojasRetorno.optInt("Prioridade",0))
                                          valoresProgresiva.put("Promocao", jsonClientesPorLojasRetorno.optInt("Promocao",0))
                                          valoresProgresiva.put("Codigo",jsonClientesPorLojasRetorno.optInt("CODLISTAPRECOSYNC", 0))

                                          try {
                                              db_Progreesivas.insertOrThrow("TB_Progressiva",null,valoresProgresiva)

                                          }catch (e: SQLiteException){
                                              println("${e.message}")
                                          }


                                      }

                                  }
                                  db_Progreesivas.setTransactionSuccessful()
                              }finally {
                                  db_Progreesivas.endTransaction()
                              }



                          }
                          lendoProgressiva.join()
                          FragmentCargas.progresspush += 1
                    FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando estoque...")



                          // lendo estoque
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
                              FragmentCargas.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando Filtros...")
                          }
                   lendoEstoque.join()
                    // fazendo carga de filtro
                    var jsonArrayFiltroPrincipal: JSONArray? = null
                    var jsonArrayFiltro:JSONArray? = null
                    var jsonArrayFiltroProduto:JSONArray? = null
                    val lendoFiltroPrincipal = launch {
                        val  taskFiltroPrincipal = TaskFiltroPrincipal()
                        jsonArrayFiltroPrincipal =taskFiltroPrincipal.requestFiltroPrincipal()
                    }
                    val lendoFiltros= launch {
                        val taskFiltro = TaskFiltro()
                        jsonArrayFiltro = taskFiltro.requestFiltro()
                    }
                    val lendoFiltroProduto = launch {
                        val taskFiltroProduto = TaskFiltroProduto()
                        jsonArrayFiltroProduto = taskFiltroProduto.requestFiltroProduto()
                    }
                    lendoFiltroPrincipal.join()
                    lendoFiltros.join()
                    lendoFiltroProduto.join()

                    val gravandoFiltroPrincipal = launch {
                        for ( i in  0 until jsonArrayFiltroPrincipal!!.length()){
                            val jsonObjectFiltroPrincipalIndices = jsonArrayFiltroPrincipal!!.getJSONObject(i)
                            val filtroCatecoriaID = jsonObjectFiltroPrincipalIndices.getInt("FiltroCategoria_id")
                            val descricao = jsonObjectFiltroPrincipalIndices.getString("Descricao")
                            val filtroPrincipal = FiltroPrincipal(descricao,filtroCatecoriaID)
                            val filtroPrincipalDAO = FiltroPrincipalDAO(context)
                            filtroPrincipalDAO.insert(filtroPrincipal)

                        }
                    }
                    val gravandoFiltro = launch {
                        for (i in 0 until  jsonArrayFiltro!!.length()){
                            val jsonFiltroIndce = jsonArrayFiltro!!.getJSONObject(i)
                            val filtroID = jsonFiltroIndce.getInt("Filtro_id")
                            val Pares = jsonFiltroIndce.getString("Pares")
                            val FiltroCategoria_id = jsonFiltroIndce.getInt("FiltroCategoria_id")
                            val Descricao = jsonFiltroIndce.getString("Descricao")
                            val Qtd = jsonFiltroIndce.getInt("Qtd")
                            val filtro = Filtros(Descricao,FiltroCategoria_id,filtroID,Pares,Qtd)
                            val filtroDAO = FiltroDAO(context)
                            filtroDAO.insert(filtro)

                        }
                    }

                    val  gravandoFiltroProduto = launch {
                        for (i in 0 until  jsonArrayFiltroProduto!!.length()){
                            val jsonFiltroProdutoIndice = jsonArrayFiltroProduto!!.getJSONObject(i)
                            val pares = jsonFiltroProdutoIndice.getString("Pares")
                            val Produto_codigo = jsonFiltroProdutoIndice.getString("Produto_codigo")
                            val Barra = jsonFiltroProdutoIndice.getString("Barra")
                            val FiltroCategoria_id = jsonFiltroProdutoIndice.getInt("FiltroCategoria_id")
                            val Filtro_id = jsonFiltroProdutoIndice.getInt("Filtro_id")
                            val Loja_id = jsonFiltroProdutoIndice.getInt("Loja_id")

                            val filtroProduto = FiltroProduto(Barra,FiltroCategoria_id,Filtro_id,Loja_id,pares,Produto_codigo)
                            val filtroProdutoDAO = FiltroProdutoDAO(context)
                            filtroProdutoDAO.insert(filtroProduto)

                        }
                    }
                    gravandoFiltroPrincipal.join()
                    gravandoFiltro.join()
                    gravandoFiltroProduto.join()
                    Log.d("Terminou carga","")
                    FragmentCargas.showNotification(context,"TESTE1","Carga Atualizada","Tudo Pronto, Boas vendas!")

                    cargaTerminada(constrain,texttitulocarga,subtitulocarga,context,icon,animador,terminouCarga)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun cargaTerminada(constrain : ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, context: Context, icon:ImageView, animador: ObjectAnimator, terminouCarga: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga){


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
            terminouCarga.terminouCarga()
            val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean("cargafeita", true)
            editor?.apply()
            FragmentCargas.progresspush  =0
        }

        Thread.sleep(5000)
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
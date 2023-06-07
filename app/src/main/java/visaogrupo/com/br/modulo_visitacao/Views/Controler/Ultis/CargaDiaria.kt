package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

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
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.TaskEstoque
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.TaskProgressivas
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.Task_Cargadiaria
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.*
import java.text.SimpleDateFormat
import java.util.*

class CargaDiaria {


    fun fazCargaDiaria(context: Context, user_ide:String, constrain: ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, icon:ImageView, animador: ObjectAnimator){
        CoroutineScope(Dispatchers.IO).launch {
            //Faz request de Zip
            var patch = Task_Cargadiaria().Cargadiaria(user_ide,context )
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
                            Log.d("Terminou Clientes","")
                        }
                    }
                    //grava no banco produtos
                    val  lendoProdutos = launch {
                        jsonProtudos = lerZip.readTextFileFromZip(patch,"Produtos.json","Protudos").toString()
                        val jsonObjectProtudos = JSONObject(jsonProtudos)
                        val jsonArrayProtudos = JSONArray(jsonObjectProtudos.getString("PRODUTOS"))
                        val isProtudos = protudosDAO.insert(jsonArrayProtudos)
                        if (isProtudos){
                            Log.d("Terminou Protudos","")
                        }

                    }
                    // faz as tarefas execulta juntas
                    lendoLojas.join()
                    lendoClientes.join()
                    lendoProdutos.join();

                    Log.d("Iniciou Segunda Parte","")
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

                        Log.d("Terminou"," OPL")

                    }

                    lendoFormasDePagamento.join()
                    lendoOperadorLogistico.join()

                    Log.d("Começou "," Terceira parte")

                    val  lendoProgressiva = launch {
                        val dblistaProgre = DataBaseHelber(context)

                        Log.d("Começou o Progressiva","")
                        val lojasOP = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf" +
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
                            count = count +1

                            Log.d("adad",count.toString())
                            val lendpo =   async{
                                val taskProgressivas = TaskProgressivas(context)
                                val jsonArray =taskProgressivas.recuperaProgressiva(loja_id,uf)
                                if (jsonArray != null) {
                                    jsonarayProgressiva?.add(jsonArray)
                                }
                            }
                            coroutines.add(lendpo)



                        }

                        runBlocking {
                            coroutines.awaitAll()
                        }

                        Log.d("Terminou", jsonarayProgressiva?.size.toString())
                        Log.d("Terminou","Progressiva")
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
                                    valoresProgresiva.put("Desconto", jsonClientesPorLojasRetorno.optDouble("Desconto"))
                                    valoresProgresiva.put("Desconto_Min", jsonClientesPorLojasRetorno.optDouble("Desconto_Min"))
                                    valoresProgresiva.put("DescontoMaximo", jsonClientesPorLojasRetorno.optDouble("DescontoMaximo"))
                                    valoresProgresiva.put("PF", jsonClientesPorLojasRetorno.optDouble("PF"))
                                    valoresProgresiva.put("PMC", jsonClientesPorLojasRetorno.optDouble("PMC"))
                                    valoresProgresiva.put("ST", jsonClientesPorLojasRetorno.optDouble("ST"))
                                    valoresProgresiva.put("formalizacao", jsonClientesPorLojasRetorno.optString("formalizacao"))
                                    valoresProgresiva.put("Seq_Kit", jsonClientesPorLojasRetorno.optInt("Seq_Kit"))
                                    valoresProgresiva.put("Seq_Cond_Coml", jsonClientesPorLojasRetorno.optInt("Seq_Cond_Coml"))
                                    valoresProgresiva.put("UF", jsonClientesPorLojasRetorno.optString("UF"))
                                    valoresProgresiva.put("Data_Vencimento", jsonClientesPorLojasRetorno.optString("Data_Vencimento"))
                                    valoresProgresiva.put("Prioridade", jsonClientesPorLojasRetorno.optInt("Prioridade"))
                                    valoresProgresiva.put("Promocao", jsonClientesPorLojasRetorno.optInt("Promocao"))

                                    db_Progreesivas.insert("TB_Progressiva",null,valoresProgresiva)

                                }

                            }
                            db_Progreesivas.setTransactionSuccessful()
                        }finally {
                            db_Progreesivas.endTransaction()
                        }



                    }
                    lendoProgressiva.join()

                    val lendoEstoque = launch {
                        val dblistaProgre = DataBaseHelber(context)

                        Log.d("Começou o Estoque","")
                        val lojasOP = "SELECT distinct lojasPorCliente.loja_id" +
                                " FROM TB_lojas lojasPorCliente" +
                                " INNER JOIN TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                " ORDER BY 1"


                        val curso = dblistaProgre.writableDatabase.rawQuery(lojasOP,null)
                        var count =0
                        val jsonarayEstoque: MutableList<JSONArray>? = mutableListOf()
                        val coroutines = mutableListOf<Deferred<Unit>>()
                        while (curso.moveToNext()){

                            val loja_id = curso.getInt(0)
                            count = count +1

                            Log.d("Quantidade list estoque",count.toString())
                            val lendEstoque =   async{
                                val taskProgressivas = TaskEstoque()
                                val jsonArray =taskProgressivas.recuperaEstoque(loja_id)
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

                                    valoresEstoque.put("Barra", jsonClientesPorLojasRetorno.optString("Barra"))
                                    valoresEstoque.put("Quantidade", jsonClientesPorLojasRetorno.optInt("Quantidade"))
                                    valoresEstoque.put("Loja_id", jsonClientesPorLojasRetorno.optInt("Loja_id"))
                                    db_Estoque.insert("TB_Estoque",null,valoresEstoque)

                                }

                            }
                            db_Estoque.setTransactionSuccessful()
                        }finally {
                            db_Estoque.endTransaction()
                        }

                    }
                    lendoEstoque.join()

                        cargaTerminada(constrain,texttitulocarga,subtitulocarga,context,icon,animador)


                    Log.d("Terminou carga","")
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun cargaTerminada(constrain : ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView,context: Context, icon:ImageView,animador: ObjectAnimator){


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
            val currentDate: String = SimpleDateFormat("dd/MMyyyy", Locale.getDefault()).format(Date())
            subtitulocarga.text ="atualizado em: ${currentDate} "
        }


    }
}
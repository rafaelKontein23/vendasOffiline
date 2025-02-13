package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FiltroPrincipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FiltroProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Filtros
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Repasse
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.*
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskEstoque
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskFiltro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskFiltroPrincipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskFiltroProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskFormaDePagamentoExclusiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskGrupoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskGruposAB
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskProgressivas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskRegraPrazoMedio
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.TaskRepasse
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.TaskCargas.Task_Cargadiaria
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentCargas
import java.text.SimpleDateFormat
import java.util.*

class CargaDiaria {

    val listaErrosCriticos = mutableListOf<String>()
    val listaErros = mutableListOf<String>()

    fun fazCargaDiaria(context: Context,
                       user_ide:String,
                       constrain: ConstraintLayout,
                       texttitulocarga: TextView,
                       subtitulocarga: TextView,
                       icon:ImageView, animador:
                       ObjectAnimator,terminouCarga: TerminouCarga){

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val patch = Task_Cargadiaria().Cargadiaria(user_ide,context )

                FragmentCargas.progresspush += 1
                PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","procurando Zip...")
                listaErrosCriticos.clear()

                Log.d("Caminho Zip","${patch}")
                if(!patch.isEmpty()){
                    val excluiDadosTabelas = ExcluiDados(context)
                  //  excluiDadosTabelas.exluidadosGeral()
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
                    var jsonKit =""
                    var jsonKitxPreco = ""
                    var jsonkitCliente = ""
                    var jsonKitxLoja = ""
                    var jsonArrayLojaAB : MutableList<JSONArray>? = mutableListOf()
                    var jsonArrayLojaABProgressiva : MutableList<JSONArray>? = mutableListOf()
                    var jsonArrayFiltroPrincipal: JSONArray? = JSONArray()
                    var jsonArrayFiltro:JSONArray? = JSONArray()
                    var jsonArrayFiltroProduto:JSONArray?  = JSONArray()


                    val lendoLojas =launch {
                        try {
                            //Lendo Arquivo de Loja
                            jsonLojas = lerZip.readTextFileFromZip(patch,"Lojas.json","Lojas").toString()
                            val jsonObjectLojas: JSONObject = JSONObject(jsonLojas)
                            val jsonArray: JSONArray = JSONArray(jsonObjectLojas.getString("LOJAS"))
                            val islojasdb = lojasDAO.insert(jsonArray)

                            if(islojasdb){
                                Log.d("Terminou lojas","")
                                FragmentCargas.progresspush += 1
                                PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando lojas...")
                            }
                        }catch (e:Exception) {
                            e.printStackTrace()
                            listaErrosCriticos.add("loja")
                        }


                    }
                    val lendoClientes = launch {
                        try {
                            jsonClienets = lerZip.readTextFileFromZip(patch,"Clientes.json","Clientes").toString()
                            val jsonObjectClientes = JSONObject(jsonClienets)
                            val jsonObjectClientesUser = JSONObject(jsonObjectClientes.getString("USUARIO"))
                            val jsonArrayCliente = JSONArray(jsonObjectClientesUser.getString("EMPRESA"))
                            val isCliente = clientesDAO.insert(jsonArrayCliente)
                            if (isCliente){
                                FragmentCargas.progresspush += 1
                                PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando clientes...")
                                Log.d("Terminou Clientes","")
                            }
                        }catch (e:Exception) {
                            e.printStackTrace()
                            listaErrosCriticos.add("cliente")
                        }

                    }


                    val lendoPrazoMedio = launch {
                        try {
                            val taskRegraPrazoMedio = TaskRegraPrazoMedio()
                            val listaRetorna  = taskRegraPrazoMedio.tasKRegra()
                            val regraPrazoMedio = RegraPrazoDAO(context)
                            regraPrazoMedio.insertRegraProgressiva(listaRetorna)
                        }catch (e:Exception){
                            e.printStackTrace()
                            listaErrosCriticos.add("PrazoMedio")

                        }

                    }

                    val  lendoProdutos = launch {
                        try {
                            jsonProtudos = lerZip.readTextFileFromZip(patch,"Produtos.json","Protudos").toString()
                            val jsonObjectProtudos = JSONObject(jsonProtudos)
                            val jsonArrayProtudos = JSONArray(jsonObjectProtudos.getString("PRODUTOS"))
                            val isProtudos = protudosDAO.insert(jsonArrayProtudos)
                            if (isProtudos){
                                FragmentCargas.progresspush += 1
                                PushNativo.showNotification(context,"2","Titulo1","")
                                Log.d("Terminou Protudos","")
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            listaErrosCriticos.add("produtos")
                        }

                    }
                    lendoLojas.join()
                    lendoClientes.join()
                    lendoProdutos.join()
                    lendoPrazoMedio.join()

                    if(!listaErrosCriticos.isEmpty()){
                        erroNaCargaFun(icon,texttitulocarga, subtitulocarga ,context,animador,constrain)

                        return@launch
                    }

                    Log.d("Iniciou Segunda Parte","")
                    val LendoFormaDePagamentoExclusiva = launch {
                        try {
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
                        }catch (e:Exception){
                            e.printStackTrace()
                            listaErrosCriticos.add("forma de pagemnto exlusiva")

                        }

                    }

                    val lendoFormasDePagamento = launch {
                        try {
                            val dblista = DataBaseHelber(context)
                            val LojasPOrClientesList  = "SELECT distinct lojasPorCliente.loja_id " +
                                    "FROM TB_lojas lojasPorCliente" +
                                    " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id"
                            val curso = dblista.readableDatabase.rawQuery(LojasPOrClientesList,null)

                            while (curso.moveToNext()){
                                try {
                                    val lojaID =  curso.getInt(0)
                                    jsonFormaDePagamento = lerZip.readTextFileFromZip(patch, "FormaPagamento_${lojaID}.json", "FormaDePAgamento").toString()
                                    val jsosonform = JSONObject(jsonFormaDePagamento)
                                    val jsonArrayFormaDePag = JSONArray(jsosonform.getString("FORMAS"))
                                    formaDePagamentoDAO.insert(jsonArrayFormaDePag,lojaID)

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }

                            FragmentCargas.progresspush += 1
                            PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando Formas de pagamentos...")
                            Log.d("Terminou"," FormaDePagamento")
                        }catch (e:Exception){
                            e.printStackTrace()
                            listaErrosCriticos.add("forma de pagamento")
                        }

                    }

                    val lendoOperadorLogistico = launch {
                        val dblistaOP = DataBaseHelber(context)
                        try {
                            Log.d("Começou o OPL","")
                            val LojasOP = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf" +
                                    " FROM TB_lojas lojasPorCliente" +
                                    " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                    " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                    " order by 1"

                            val curso = dblistaOP.readableDatabase.rawQuery(LojasOP,null)
                            while (curso.moveToNext()){
                                try {
                                    val lojaID = curso.getInt(0);
                                    val ufs = curso.getString(1)
                                    jsonOperadorLogistico = lerZip.readTextFileFromZip(patch,"OperadorLogistico_${lojaID}.json","OperdorLogistico").toString()
                                    val  jsonOP = JSONObject(jsonOperadorLogistico)
                                    val jsonArrayOperadorLogistico = JSONArray(jsonOP.getString("OPERADORES"))
                                    operadorLogisticoDAO.insert(jsonArrayOperadorLogistico,lojaID,ufs)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }

                            FragmentCargas.progresspush += 1
                            PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando opls...")

                            Log.d("Terminou"," OPL")
                        }catch (e:Exception){
                            e.printStackTrace()
                        }


                    }

                    lendoFormasDePagamento.join()
                    lendoOperadorLogistico.join()
                    LendoFormaDePagamentoExclusiva.join()

                    if(!listaErrosCriticos.isEmpty()){

                   //     erroNaCarga(icon,texttitulocarga, subtitulocarga ,context,animador,constrain)
                        val excluiDadosTabelas = ExcluiDados(context)
                      //  excluiDadosTabelas.exluidadosGeral()
                        return@launch
                    }

                    Log.d("Começou "," Terceira parte")

                    val  lendoProgressiva = launch {
                        try {
                            val dblistaProgre = DataBaseHelber(context)

                            Log.d("Começou o Progressiva","")
                            val queryProgrssiva = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf, Clientes.codigo" +
                                    " FROM TB_lojas lojasPorCliente" +
                                    " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                    " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                    " order by 1"

                            val curso = dblistaProgre.writableDatabase.rawQuery(queryProgrssiva,null)
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

                            Log.d("Qtd PRogrssiva p json", jsonarayProgressiva?.size.toString())
                            Log.d("Terminou","Progressiva")

                            FragmentCargas.progresspush += 1
                            PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando progressivas...")

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
                        }catch (e:Exception){
                            e.printStackTrace()
                            if (!listaErros.contains("Progressiva")){
                                listaErros.add("Progressiva")

                            }
                        }

                    }

                    lendoProgressiva.join()

                    if(!listaErrosCriticos.isEmpty()){
                        erroNaCargaFun(icon,texttitulocarga, subtitulocarga ,context,animador,constrain)
                        return@launch
                    }

                    FragmentCargas.progresspush += 1
                    PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando estoque...")

                    val lendoAb = launch {
                        try {
                            val dblistaGrupo = DataBaseHelber(context)
                            val coroutines = mutableListOf<Deferred<Unit>>()

                            val query = "SELECT distinct lojasPorCliente.loja_id, Clientes.uf, Clientes.codigo" +
                                    " FROM TB_lojas lojasPorCliente" +
                                    " inner join TB_lojaporcliente ClienteFazOL on lojasPorCliente.loja_id = ClienteFazOL.loja_id" +
                                    " INNER JOIN TB_clientes Clientes on Clientes.empresa_id = ClienteFazOL.empresa_id" +
                                    " WHERE lojasPorCliente.LojaTipo = 13 "+
                                    " order by 1"
                            val cursor = dblistaGrupo.writableDatabase.rawQuery(query,null)
                            while (cursor.moveToNext()){
                                val  uf = cursor.getString(1)
                                val lojaID = cursor.getString(0)
                                val  codigoSync = cursor.getString(2)

                                val async = async {
                                    val taskGruposAB = TaskGruposAB()
                                    taskGruposAB.taskbuscagrupo(lojaID,uf,codigoSync,jsonArrayLojaAB!!, listaErros)

                                }
                                coroutines.add(async)

                                runBlocking {
                                    coroutines.awaitAll()
                                }
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }

                    val lendoGrupoProgressiva = launch {
                        val dblistaGrupoProgressiva = DataBaseHelber(context).writableDatabase
                        val coroutines = mutableListOf<Deferred<Unit>>()

                        val querylojaTipo =  "SELECT loja_id FROM TB_lojas WHERE lojatipo = 13"
                        val cursor  =   dblistaGrupoProgressiva.rawQuery(querylojaTipo,null)
                        while (cursor.moveToNext()){
                            val lojaId = cursor.getInt(0)
                            val asyncGrupoProgressiva = async {
                                val taskGrupoProgressiva = TaskGrupoProgressiva()
                                taskGrupoProgressiva.taskGrupoProgressiva(lojaId, jsonArrayLojaABProgressiva!!, listaErros)
                            }

                            coroutines.add(asyncGrupoProgressiva)
                            runBlocking {
                                coroutines.awaitAll()
                            }
                        }
                    }
                    lendoAb.join()
                    lendoGrupoProgressiva.join()

                    val inserindoGrupoAb = launch {
                        val gruppolojaAbDAO = GrupoLojaAbDAO(context)
                        for (i in jsonArrayLojaAB!!){
                            gruppolojaAbDAO.insertGrupoEProduto(i)
                        }
                    }

                    val  inserirGrupoProgressiva = launch {
                        for (i in jsonArrayLojaABProgressiva!!){
                            val gruppolojaAbDAO = GrupoLojaAbDAO(context)
                            gruppolojaAbDAO.inserirGrupoProgressiva(i)
                        }
                    }

                    inserindoGrupoAb.join()
                    inserirGrupoProgressiva.join()

                    val lendoEstoque = launch {
                        val dblistaProgre = DataBaseHelber(context)

                        Log.d("Começou o Estoque","")
                        val lojasOP = "SELECT DISTINCT CodEstoque FROM TB_clientes"

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
                        PushNativo.showNotification(context,"TESTE1","Carga Tudo Farma","Atualizando Filtros...")
                    }


                    val  lendorepasse = launch{
                        val queryrespasse =  "SELECT DISTINCT CodEstoque, uf FROM TB_clientes"
                        val dbCliente = DataBaseHelber(context)

                        val curso = dbCliente.writableDatabase.rawQuery(queryrespasse,null)
                        var count =0
                        val jsonarayRepasse: MutableList<JSONArray>? = mutableListOf()
                        val coroutines = mutableListOf<Deferred<Unit>>()
                        while (curso.moveToNext()){

                            val codigoEstoque = curso.getInt(0)
                            val uf = curso.getString(1)

                            val lendoRepasse =   async{
                                val taskRepasse = TaskRepasse()
                                val jsonArray =taskRepasse.requestrepase(uf,codigoEstoque, listaErrosCriticos)
                                if (jsonArray != null) {
                                    jsonarayRepasse?.add(jsonArray)
                                }
                            }
                            coroutines.add(lendoRepasse)

                        }
                        runBlocking {
                            coroutines.awaitAll()
                        }

                        try {
                            for ( i in 0 until  jsonarayRepasse!!.size){
                                val jsonArrayAtual = jsonarayRepasse[i]
                                val repasseDAO = RepasseDAO(context)
                                for ( j in 0 until  jsonArrayAtual.length()){
                                    val jsonClientesPorLojasRetorno = jsonArrayAtual.optJSONObject(j)

                                    val repasse = Repasse(jsonClientesPorLojasRetorno.optInt("MATERIAL",0),
                                        jsonClientesPorLojasRetorno.optDouble("PERCENTUAL",0.0),
                                        jsonClientesPorLojasRetorno.optString("UF",""),
                                        jsonClientesPorLojasRetorno.optInt("CENTRO",0))
                                    repasseDAO.insert(repasse)
                                }
                            }
                        }catch (e:Exception) {
                            e.printStackTrace()
                            if(listaErrosCriticos.contains("Repasse")){
                                listaErrosCriticos.add("Repasse")
                            }

                        }

                    }
                    lendorepasse.join()
                    lendoEstoque.join()

                    if(!listaErrosCriticos.isEmpty()){
                        erroNaCargaFun(icon,texttitulocarga, subtitulocarga ,context,animador,constrain)
                        return@launch
                    }




                    val lendoFiltroPrincipal = launch {
                        val  taskFiltroPrincipal = TaskFiltroPrincipal()
                        jsonArrayFiltroPrincipal =taskFiltroPrincipal.requestFiltroPrincipal()

                        if (jsonArrayFiltroPrincipal?.length() == 0){
                            listaErros.add("filtro principal")
                        }
                    }
                    val lendoFiltros= launch {
                        val taskFiltro = TaskFiltro()
                        jsonArrayFiltro = taskFiltro.requestFiltro()
                        if (jsonArrayFiltro?.length() == 0){
                            listaErros.add("filtro")
                        }
                    }
                    val lendoFiltroProduto = launch {
                        val taskFiltroProduto = TaskFiltroProduto()
                        jsonArrayFiltroProduto = taskFiltroProduto.requestFiltroProduto()
                        if (jsonArrayFiltroProduto?.length() == 0){
                            listaErros.add("Filtro Produto")
                        }
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

                    val lerkit = launch {
                        try {
                            jsonKit = lerZip.readTextFileFromZip(patch,"Kit.json","Kit").toString()
                            val jsonObjectKit = JSONObject(jsonKit)
                            val jsonArray = JSONArray(jsonObjectKit.getString("KITS"))
                            val lerKits = LerKitItens()
                            lerKits.lerJsonKit(jsonArray,context)
                        }catch (e:Exception){
                            e.printStackTrace()
                            if(!listaErros.contains("kit")){
                                listaErros.add("loja kit")
                            }
                        }



                    }
                    val  lerKitxPreco = launch {
                        try {
                            jsonKitxPreco = lerZip.readTextFileFromZip(patch,"KitxPreco.json","KitPreco").toString()
                            val jsonObjectKit = JSONObject(jsonKitxPreco)
                            val jsonArray = JSONArray(jsonObjectKit.getString("KITSxPRECO"))
                            val lerkits = LerKitItens()
                            lerkits.lerJsonKitxPreco(jsonArray,context)
                        }catch (e:Exception){
                            e.printStackTrace()
                            if(!listaErros.contains("kit")){
                                listaErros.add("loja kit")
                            }
                        }

                    }

                    val  lerKitXCliente = launch {
                        jsonkitCliente = lerZip.readTextFileFromZip(patch,"KitxCliente.json","KitPreco").toString()
                        val jsonObjectKit = JSONObject(jsonkitCliente)
                        val jsonArray = JSONArray(jsonObjectKit.getString("KITSxCLIENTES"))
                        val lerkits = LerKitItens()
                        lerkits.lerJsonkitCliente(jsonArray,context)
                        try {
                            jsonkitCliente = lerZip.readTextFileFromZip(patch,"KitxCliente.json","KitPreco").toString()
                            val jsonObjectKit = JSONObject(jsonkitCliente)
                            val jsonArray = JSONArray(jsonObjectKit.getString("KITSxCLIENTES"))
                            val lerkits = LerKitItens()
                            lerkits.lerJsonkitCliente(jsonArray,context)
                        }catch (e:Exception){
                            e.printStackTrace()
                            if(!listaErros.contains("kit")){
                                listaErros.add("loja kit")
                            }
                        }
                    }

                    val lerkitxLoja = launch {
                        try {
                            jsonKitxLoja = lerZip.readTextFileFromZip(patch,"KitxLoja.json","KitPreco").toString()
                            val jsonObjectKit = JSONObject(jsonKitxLoja)
                            val jsonArray = JSONArray(jsonObjectKit.getString("KITSxLOJA"))
                            val lerkits = LerKitItens()
                            lerkits.lerJsonkitxLoja(jsonArray,context)
                        }catch (e:Exception){
                            e.printStackTrace()
                            if(!listaErros.contains("kit")){
                                listaErros.add("loja kit")
                            }
                        }
                    }

                    lerkit.join()
                    lerKitxPreco.join()
                    lerKitXCliente.join()
                    lerkitxLoja.join()

                    Log.d("Terminou carga","")
                    PushNativo.showNotification(context,"TESTE1","Carga Atualizada","Tudo Pronto, Boas vendas!")
                    cargaTerminada(constrain,texttitulocarga,subtitulocarga,context,icon,animador,terminouCarga)

                }else{
                    val  dialogErro = DialogErro()
                    dialogErro.Dialog(context,"Atenção", "No momento algo deu errado, tente novamente mais tarde", "Ok",""){

                    }
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun cargaTerminada(constrain : ConstraintLayout, texttitulocarga: TextView, subtitulocarga: TextView, context: Context, icon:ImageView, animador: ObjectAnimator, terminouCarga: TerminouCarga){

        val drawable = icon.drawable

        CoroutineScope(Dispatchers.Main).launch {
            if(!listaErros.isEmpty()){
                erroNaCargaLoja(context)
                val listaNomeTabela = mutableListOf<String>()
                if (listaErros.contains("loja kit")){
                    listaNomeTabela.add("TB_Kits")
                    listaNomeTabela.add("Tb_Kit_Produtos")
                    listaNomeTabela.add("TB_kit_x_preco")
                    listaNomeTabela.add("TB_KItXcliente")
                    listaNomeTabela.add("TB_KitxLoja")

                }
                if (listaErros.contains("Filtro Produto")){
                    listaNomeTabela.add("TB_Filtros")
                    listaNomeTabela.add("TB_FiltroProdutos")
                    listaNomeTabela.add("TB_FiltroPricipal")
                }

                if (listaErros.contains("Loja Grupo AB")){
                    listaNomeTabela.add("Tb_GrupoAB")
                    listaNomeTabela.add("TB_grupoAB_Produtos")
                    listaNomeTabela.add("TB_Grupo_Progressiva")
                }
                val excluiDados = ExcluiDados(context)
                excluiDados.exluiDadosEspecifico(listaNomeTabela)
            }


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
            FezCargaPreferencias.atualizaInfoDeCarga(context, true)
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

    fun trocaCoritensCargaFeita(img:ImageView, texttitulo:TextView, descricao:TextView,
                                context: Context, animador: ObjectAnimator, constrain:ConstraintLayout
    ){
        CoroutineScope(Dispatchers.Main).launch {
            img.background = ContextCompat.getDrawable(context,R.drawable.bordasimagenscargas)
            constrain.background = ContextCompat.getDrawable(context, R.drawable.bordascargas)
            trocarCorItem(img,img.drawable,"#336B9B")
            texttitulo.setTextColor(Color.parseColor("#2A313C"))
            descricao.setTextColor(Color.parseColor("#2A313C"))
            descricao.text = "Tente novamente..."
            animador.end()

        }

    }
    fun erroNaCargaLoja(context: Context){
        var errolista = ""
        val  dialogErro = DialogErro()
        for ((i,valor) in listaErros.withIndex()){
            if (listaErros.size == 1 ){
                errolista += valor
            }else if( i +1 == listaErros.size){
                errolista += valor +". "
            }else{
                errolista += valor +", "
            }

        }
        CoroutineScope(Dispatchers.Main).launch {
            dialogErro.Dialog(context,"Atenção", "Os seguintes recursos não estará disponivel no momento: ${errolista} \nTente Novamente mais tarde! Sua carga ", "Ok",""){

            }
        }
    }
    private fun trocarCorItem(icon:ImageView, drawable: Drawable, cor:String){
        val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(cor))
        icon.setImageDrawable(wrappedDrawable)
    }
    fun erroNaCarga(img:ImageView, texttitulo:TextView, descricao:TextView,
                    context: Context, animador: ObjectAnimator, constrain:ConstraintLayout){
        var errolista = ""
        trocaCoritensCargaFeita(img,texttitulo,descricao,context, animador,constrain)
        val  dialogErro = DialogErro()
        for ((i,valor) in listaErrosCriticos.withIndex()){
            if (listaErrosCriticos.size == 1 ){
                errolista += valor
            }else if( i +1 == listaErrosCriticos.size){
                errolista += valor +". "
            }else{
                errolista += valor +", "
            }

        }
        CoroutineScope(Dispatchers.Main).launch {
            dialogErro.Dialog(context,"Atenção", "Não conseguimos encontrar as seguintes informações: ${errolista} \nTente realizar a cargar novamente em instantes ", "Ok",""){

            }
        }
    }

    fun erroNaCargaFun(img:ImageView, texttitulo:TextView, descricao:TextView,
                    context: Context, animador: ObjectAnimator, constrain:ConstraintLayout){
        erroNaCarga(img,texttitulo, descricao ,context,animador,constrain)
        val excluiDadosTabelas = ExcluiDados(context)
      //  excluiDadosTabelas.exluidadosGeral()
        FezCargaPreferencias.atualizaInfoDeCarga(context, false)
    }
}
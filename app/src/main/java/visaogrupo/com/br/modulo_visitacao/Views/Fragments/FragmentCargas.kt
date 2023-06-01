package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.Task_Cargadiaria
import visaogrupo.com.br.modulo_visitacao.Views.Controler.DAO.LerZip
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.TaskProgressivas
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.ExcluiDados
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCargas.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCargas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var  login: Login



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_cargas, container, false)
        val caragDiaria = view.findViewById<ConstraintLayout>(R.id.caragDiaria)
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("UserLogin", null)
        login =  gson.fromJson(objetoSerializado, Login::class.java)





        caragDiaria.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //Faz request de Zip
                var patch =Task_Cargadiaria().Cargadiaria(login.Usuario_id.toString(),requireContext() )
                Log.d("Caminho Zip","${patch}")
                if(!patch.isEmpty()){
                    val excluiDadosTabelas = ExcluiDados(requireContext())
                    excluiDadosTabelas.exluidados()
                    val lerZip = LerZip()
                    val lojasDAO = LojasDAO(requireContext())
                    val clientesDAO = ClientesDAO(requireContext())
                    val protudosDAO = ProdutosDAO(requireContext())
                    val formaDePagamentoDAO = FormaDePagamentoDAO(requireContext())
                    val operadorLogisticoDAO = OperadorLogisticaDAO(requireContext())
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
                            val jsonObjectLojas:JSONObject  = JSONObject(jsonLojas)
                            val jsonArray:JSONArray = JSONArray(jsonObjectLojas.getString("LOJAS"))
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

                            val dblista = DataBaseHelber(requireContext())

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
                            val dblistaOP = DataBaseHelber(requireContext())

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
                            val dblistaProgre = DataBaseHelber(requireContext())

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
                                    val taskProgressivas = TaskProgressivas(requireContext())
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
                            val db_Progreesivas = DataBaseHelber(requireContext()).writableDatabase
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


                        Log.d("Terminou carga","")
                    }catch (e:Exception){
                        e.printStackTrace()
                    }


                }
            }

        }


        return  view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCargas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}

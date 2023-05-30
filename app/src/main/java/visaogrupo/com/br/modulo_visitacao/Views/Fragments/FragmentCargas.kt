package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.Task_Cargadiaria
import visaogrupo.com.br.modulo_visitacao.Views.Controler.DAO.LerZip
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.ExcluiDados
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.LojasDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProdutosDAO

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
                      var jsonLojas = ""
                      var jsonClienets = ""
                      var jsonProtudos = ""

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
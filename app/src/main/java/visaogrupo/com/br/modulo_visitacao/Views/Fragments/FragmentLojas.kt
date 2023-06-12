package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_lojas.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.LojasAdapter
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.LojaXCliente
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.LojasDAO
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentLojasBinding
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentLojas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var  binding: FragmentLojasBinding
    lateinit var clienteSelecionado:Clientes
    lateinit var  AdapterLojas:LojasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLojasBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = binding.root
        val lojasDAO = LojasDAO(requireContext())
        //recupera cliente Selecionado
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("ClienteSelecionado", null)
        clienteSelecionado =  gson.fromJson(objetoSerializado, Clientes::class.java)


        // atualiza o cliente selecionado na view
        binding.cnpjClienteSelecionado.text = clienteSelecionado.CNPJ
        binding.textRazaosocialclienteSelecionado.text = clienteSelecionado.RazaoSocial

        // recupera lisat de lojas
        val listalojas = LojasDAO(requireContext())
        val querylojasClientes = "SELECT LojCli.empresa_id, Lojas.* FROM TB_lojas Lojas inner join TB_lojaporcliente LojCli on Lojas.Loja_id = LojCli.loja_id WHERE LojCli.empresa_id = ${clienteSelecionado.Empresa_id} "
        val listLojas =  listalojas.listarlojas(requireContext(),1,querylojasClientes)
        AdapterLojas = LojasAdapter(listLojas)
        val  layoutManager = LinearLayoutManager(requireContext())
        binding.recyLojas.layoutManager = layoutManager
        binding.recyLojas.adapter = AdapterLojas


        // listerner de busca
        binding.edtBuscaLojas.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val listaFiltroLoja = listLojas.filter { it.nome.toLowerCase().contains(p0.toString()) }
                AdapterLojas.listaLojas = listaFiltroLoja
                AdapterLojas. notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        return  view;
    }


}
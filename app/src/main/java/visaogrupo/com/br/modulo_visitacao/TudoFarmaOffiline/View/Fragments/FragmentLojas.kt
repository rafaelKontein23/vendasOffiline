package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.FragmentLojasBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.LojasAdapter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.LojasDAO

class FragmentLojas (trocarcorItem: TrocarcorItem, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho): Fragment() {

    private  lateinit var  binding: FragmentLojasBinding
    lateinit var clienteSelecionado: Clientes
    lateinit var  AdapterLojas: LojasAdapter
    val  trocarcorItem = trocarcorItem
    val  carrinhoVisible = carrinhoVisible
    val atualizaCarrinho = atualizaCarrinho
    var listLojas = mutableListOf<Lojas>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLojasBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = binding.root

        CoroutineScope(Dispatchers.Main).launch {
            val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val objetoSerializado = sharedPreferences?.getString("ClienteSelecionado", null)
            binding.progressLoja.isVisible = true
            clienteSelecionado =  gson.fromJson(objetoSerializado, Clientes::class.java)


            val  CNPJ = clienteSelecionado.CNPJ.substring(0,2)+"."+clienteSelecionado.CNPJ.substring(2,5)+"."+clienteSelecionado.CNPJ.substring(5,8)+"/"+clienteSelecionado.CNPJ.substring(8,12) +"-"+ clienteSelecionado.CNPJ.substring(12,14);
            binding.cnpjClienteSelecionado.text = CNPJ
            binding.textRazaosocialclienteSelecionado.text = clienteSelecionado.RazaoSocial
            AdapterLojas = LojasAdapter(listLojas,trocarcorItem,
                R.id.fragmentContainerViewPrincipal, getParentFragmentManager(),carrinhoVisible,atualizaCarrinho, requireContext())
            val  layoutManager = GridLayoutManager(requireContext(),2)
            binding.recyLojas.layoutManager = layoutManager
            binding.recyLojas.adapter = AdapterLojas
            val initialLojas = withContext(Dispatchers.Default) {
                // recupera lisat de lojas
                val listalojasDao = LojasDAO(requireContext())
                val querylojasClientes = " SELECT DISTINCT LojCli.empresa_id, Lojas.*,imagembase64 " +
                        "FROM TB_lojas Lojas " +
                        "INNER JOIN TB_lojaporcliente LojCli ON Lojas.Loja_id = LojCli.loja_id " +
                        "INNER JOIN TB_clientes CLIENTES ON Clientes.Empresa_id = LojCli.empresa_id " +
                        "INNER JOIN TB_OperadorLogistico Operador ON Operador.loja_id = Lojas.loja_id AND Operador.estado = Clientes.uf " +
                        "LEFT join TB_ImagensLojas ImgLj ON ImgLj.loja_id = Lojas.loja_id \n"+
                        "WHERE LojCli.empresa_id = ${clienteSelecionado.Empresa_id} "

               listalojasDao.listarlojas(requireContext(),1,querylojasClientes)
            }


            listLojas .addAll(initialLojas)
            AdapterLojas.listaLojas = initialLojas
            AdapterLojas.notifyDataSetChanged()
            binding.progressLoja.isVisible = false

            binding.fecharMensagemLoja.setOnClickListener {
                binding.view9.isVisible =false
                binding.fecharMensagemLoja.isVisible =false
                binding.verMensagem.isVisible = true
            }
            binding.verMensagem.setOnClickListener {
                binding.view9.isVisible =true
                binding.fecharMensagemLoja.isVisible =true
                binding.verMensagem.isVisible = false

            }
        }

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
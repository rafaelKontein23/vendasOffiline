package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.ProtudoAdapter
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentProtudosBinding

class FragmentProtudos (carrinhoVisible: carrinhoVisible) : Fragment() {
    private  lateinit var  binding: FragmentProtudosBinding
    val carrinhoVisible = carrinhoVisible
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProtudosBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Recupera produtos da loja Selecionada
        val view  = binding.root
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
        val   query = "SELECT Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Progressiva.valor, min(Progressiva.valor) " +
                "FROM TB_produtos Produtos " +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod " +
                "inner join TB_lojaporcliente LojxCli on LojxCli.loja_id = Progressiva.loja_id " +
                "where Progressiva.loja_id = ${lojaSelecionada.loja_id} " +
                "and LojxCli.empresa_id =${clienteSelecionado.Empresa_id} " +
                "and Progressiva.uf = '${clienteSelecionado.UF}'"+
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo"
        // configura o recycler
        val produtos = ProdutosDAO(requireContext())
        val listaProtudos = produtos.litar(query)
        val adpterProtudos = ProtudoAdapter(listaProtudos)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyProtudo.adapter = adpterProtudos
        binding.recyProtudo.layoutManager = layoutManager
        // atualiza a view
        try {
            val cnpj = clienteSelecionado.CNPJ.substring(0,2)+"."+clienteSelecionado.CNPJ.substring(2,5)+
                    "."+clienteSelecionado.CNPJ.substring(5,8)+"/"+clienteSelecionado.CNPJ.substring(8,12) +"-"+
                    clienteSelecionado.CNPJ.substring(12,14);
            binding.cnpjClienteSelecionado.text =cnpj
        }catch (e:Exception){
            e.printStackTrace()
            binding.cnpjClienteSelecionado.text =clienteSelecionado.CNPJ
        }
        binding.lojasSelecionadas.text = lojaSelecionada.nome
        binding.textRazaosocialclienteSelecionado.text = clienteSelecionado.RazaoSocial


        // esconde carrinho

        carrinhoVisible.carrinhoVisivel()
        return view
    }

}
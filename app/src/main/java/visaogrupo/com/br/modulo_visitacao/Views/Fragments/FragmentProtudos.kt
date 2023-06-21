package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.ProtudoAdapter
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.ExcluiItemcarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentProtudosBinding

class FragmentProtudos (carrinhoVisible: carrinhoVisible,atulizaCarrinho:AtualizaCarrinho) : Fragment(),StartaAtividade,ExcluiItemcarrinho {

    private  lateinit var  binding: FragmentProtudosBinding
    val carrinhoVisible = carrinhoVisible
    lateinit var   adpterProtudos :ProtudoAdapter
    var listaProtudos = mutableListOf<ProdutoProgressiva>()
    var query = ""
    val atualizaCarrinho = atulizaCarrinho
    lateinit var lojaSelecionada:Lojas
    lateinit var produtos:ProdutosDAO
    lateinit var clienteSelecionado:Clientes

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
        lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
         clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)

        query = "SELECT Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Estoque.Quantidade,Progressiva.pf,Carrinho.valor,Carrinho.quantidade,Carrinho.ValorTotal,(CASE WHEN Carrinho.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoCarrinho  " +
                "FROM TB_produtos Produtos " +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod " +
                "INNER JOIN TB_Estoque Estoque ON Estoque.Barra = Produtos.barra and ESTOQUE.Loja_id = Progressiva.loja_id " +
                "LEFT JOIN TB_Carrinho Carrinho on Carrinho.Loja_ID = Progressiva.Loja_id and Carrinho.produto_codigo = Progressiva.Prod_cod and Carrinho.UF = Progressiva.UF and carrinho.cliente_id = ${clienteSelecionado.Empresa_id} " +
                "where Progressiva.loja_id = ${lojaSelecionada.loja_id} " +
                "and Progressiva.uf = '${clienteSelecionado.UF}' " +
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo "+
                "order by 1 "

        produtos = ProdutosDAO(requireContext())
        listaProtudos = produtos.litar(query)
        adpterProtudos = ProtudoAdapter(listaProtudos, requireContext(),this,lojaSelecionada.loja_id,clienteSelecionado.Empresa_id,this)
        val layoutManager = LinearLayoutManager(requireContext())



        // atualizar o adpter
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
        binding.valorMinimo.text = "R$ " +lojaSelecionada.MinimoValor.toString()

        // esconde carrinho (Button flutuante)
        carrinhoVisible.carrinhoVisivel()
        atualizaProgressBar()
        quatidadeinCarrinho()

        return view

    }
    // Atualza os protudos que foram adiconado pela Tela ActProtudoDetalhe
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
         atualizaItemDaView()
        }
    }

    override fun atividade(intent: Intent) {
        startActivityForResult(intent,1)
    }
    fun atualizaProgressBar(){
        var valorSomado =0.0
        CoroutineScope(Dispatchers.IO).launch {
            val listasomacarrinho = listaProtudos.filter { it.valorcarrinho != null }
            for ( i  in  0 until listasomacarrinho.size){
                val soma = listasomacarrinho[i].valorTotal
                valorSomado = valorSomado + soma
            }
            // notificaça a theared de interface
            MainScope().launch {
                val valorFormatado = String.format("%.2f", valorSomado)
                binding.TotalCarrinho.text ="R$ " + valorFormatado.replace(".",",")
                binding.progressBarValorminimo.max =lojaSelecionada.MinimoValor.toInt()
                binding.progressBarValorminimo.progress = valorSomado.toInt()

                // iniciar animação
                val animationDuration = 2500L
                val finalProgress = valorSomado.toInt()
                val animator = ObjectAnimator.ofInt(binding.progressBarValorminimo, "progress", finalProgress)
                animator.duration = animationDuration
                animator.start()
            }
        }
    }
    fun atualizaItemDaView(){
        Log.d("Atualiza","Atuliza os produtos adicionados")
        produtos = ProdutosDAO(requireContext())
        listaProtudos.clear()
        listaProtudos = produtos.litar(query)
        adpterProtudos.listaProtudos =listaProtudos
        adpterProtudos.notifyDataSetChanged()

        atualizaProgressBar()
        quatidadeinCarrinho()
        atualizaCarrinho.atualizaCarrinho()
    }

    override fun exluiItem() {
        atualizaItemDaView()
        quatidadeinCarrinho()
        atualizaCarrinho.atualizaCarrinho()
    }
    fun  quatidadeinCarrinho(){
        val carrinhoDAO=  CarrinhoDAO(requireContext())
        val count = carrinhoDAO.quantidadeItens(clienteSelecionado.Empresa_id, lojaSelecionada.loja_id)
        binding.quatidadeItens.text = count.toString()
    }
}
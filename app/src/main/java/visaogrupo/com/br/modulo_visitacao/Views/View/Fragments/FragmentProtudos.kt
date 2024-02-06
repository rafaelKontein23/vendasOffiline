package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaFiltrosProduto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaLetraFiltro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.ExcluiItemcarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.LimparFiltrosProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.FormataTexto
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.ProtudoAdapter
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActCarrinhoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdapterFiltroAZ
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogFiltro
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentProtudosBinding

class FragmentProtudos (carrinhoVisible: carrinhoVisible, atulizaCarrinho: AtualizaCarrinho) : Fragment(),
    StartaAtividade,
    ExcluiItemcarrinho, AtualizaLetraFiltro,AtualizaFiltrosProduto, LimparFiltrosProdutos {

    private  lateinit var  binding: FragmentProtudosBinding
    private var limparFiltro = false
    val carrinhoVisible = carrinhoVisible
    lateinit var   adpterProtudos : ProtudoAdapter
    var listaProtudos = mutableListOf<ProdutoProgressiva>()
    var query = ""
    var queryFiltro =""
    val atualizaCarrinho = atulizaCarrinho
    lateinit var lojaSelecionada: Lojas
    lateinit var produtos: ProdutosDAO
    lateinit var clienteSelecionado: Clientes
    val contextThis = this

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
        adpterProtudos = ProtudoAdapter(listaProtudos, requireContext(),contextThis,lojaSelecionada.loja_id,clienteSelecionado.Empresa_id,contextThis,view,uf = clienteSelecionado!!.UF)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyProtudo.adapter = adpterProtudos
        binding.recyProtudo.layoutManager = layoutManager

        binding.filtros.setOnClickListener {
            val dialogFiltro = DialogFiltro()
            dialogFiltro.dialogFiltro(context,this,limparFiltro,this, lojaSelecionada.loja_id)
        }

        binding.totalCarrinho.setOnClickListener {
            irParaCarrinho()
        }
        binding.TotalCarrinho.setOnClickListener {
            irParaCarrinho()

        }
        binding.carrinhoProtudo.setOnClickListener {
            irParaCarrinho()

        }
        binding.edtBuscaProdutos.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val buscaProdutos = s.toString()
                val listaFiltroProdutos  = mutableListOf<ProdutoProgressiva>()
                for (i in listaProtudos){
                    if (i.nome.toLowerCase().contains(buscaProdutos) || i.barra.contains(buscaProdutos) || i.ProdutoCodigo.toString().contains(buscaProdutos) ){
                        listaFiltroProdutos.add(i)
                    }
                }

                if (listaFiltroProdutos.isEmpty()){
                    binding.recyProtudo.isVisible = false
                    binding.SemFiltro.isVisible = true
                }else{
                    binding.recyProtudo.isVisible = true
                    binding.SemFiltro.isVisible =false
                    adpterProtudos.listaProtudos = listaFiltroProdutos
                    adpterProtudos.notifyDataSetChanged()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        CoroutineScope(Dispatchers.IO).launch{
            MainScope().launch {
                binding.carregandoProduto.isVisible = true

            }
            protudosIniciais()
            atualizaProgressBar()
            quatidadeinCarrinho()

        }


        // atualiza a view

        val cnpj = FormataTexto.formataCnpj(clienteSelecionado.CNPJ)
        binding.cnpjClienteSelecionado.text =cnpj

        binding.lojasSelecionadas.text = lojaSelecionada.nome
        binding.textRazaosocialclienteSelecionado.text = clienteSelecionado.RazaoSocial
        binding.valorMinimo.text = "R$ " +lojaSelecionada.MinimoValor.toString()

        // esconde carrinho (Button flutuante)
        carrinhoVisible.carrinhoVisivel()




        return view

    }
    // Atualza os protudos que foram adiconado pela Tela ActProtudoDetalhe
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 6 ) && resultCode == Activity.RESULT_OK) {
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
        var queryAtualiza =""
        if (limparFiltro){
            queryAtualiza = queryFiltro
        }else{
            queryAtualiza = query

        }
        listaProtudos = produtos.litar(queryAtualiza)
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
        MainScope().launch {
            binding.quatidadeItens.text = count.toString()

        }
    }

    override fun letraFiltro(letra:String) {
        CoroutineScope(Dispatchers.IO).launch {
            MainScope().launch {
                adpterProtudos.listaProtudos = listaProtudos
                adpterProtudos.notifyDataSetChanged()

            }


            for (i in listaProtudos.indices){
                val letraPrimeira = listaProtudos[i].nome.substring(0,1).toUpperCase()
                if (letraPrimeira.equals(letra)){
                    MainScope().launch {
                        binding.recyProtudo.scrollToPosition(i)
                    }

                    break
                }
            }

        }
    }

    override fun filtraListaProdutos(listaFiltroID: MutableList<Int>,filtro:String) {
        var filtroIdNull = ""
        if (listaFiltroID != null && !listaFiltroID.isEmpty()){

        }
        CoroutineScope(Dispatchers.IO).launch {

                var listaProtudosFiltro = mutableListOf<ProdutoProgressiva>()

                var idfiltros = ""
                for (i in 0 until  listaFiltroID.size){
                    if (i+1 != listaFiltroID.size){
                        idfiltros +=listaFiltroID[i].toString() +","
                    }else{
                        idfiltros +=listaFiltroID[i].toString()
                    }

                }

                var filtroIdNull = ""
                if (listaFiltroID != null && !listaFiltroID.isEmpty()){
                    filtroIdNull= "and prod_cod In (" +
                            "SELECT produto_codigo FROM TB_Filtros Filtro \n" +
                            "inner join TB_FiltroProdutos FiltroProdutos on Filtro.filtroid = FiltroProdutos.filtroid \n" +
                            "where Filtro.filtroid in (${idfiltros})\n" +
                            ")"
                }
                 queryFiltro = "SELECT \n" +
                        "Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Estoque.Quantidade,\n" +
                        "Progressiva.pf,Carrinho.valor,Carrinho.quantidade,Carrinho.ValorTotal,imagens.imagembase64,\n" +
                        "(CASE WHEN Carrinho.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoCarrinho , Estoque.centro, Estoque.quantidade,Repasse.*\n" +
                        "FROM TB_produtos Produtos \n" +
                        "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                        "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                        "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                        "LEFT JOIN TB_Carrinho Carrinho on Carrinho.Loja_ID = Progressiva.Loja_id \n" +
                        "and Carrinho.produto_codigo = Progressiva.Prod_cod and Carrinho.UF = Progressiva.UF \n" +
                        "and carrinho.cliente_id = ${clienteSelecionado.Empresa_id}   \n" +
                        "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                        " LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+
                        "where Progressiva.loja_id = ${lojaSelecionada.loja_id} and Progressiva.uf = '${clienteSelecionado.UF}' \n" +
                        "and CLI.empresa_id = ${clienteSelecionado.Empresa_id} \n"+
                        filtroIdNull+
                        "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo order by ${filtro}"





                produtos = ProdutosDAO(requireContext())
                listaProtudosFiltro = produtos.litar(queryFiltro)


                val letrasIniciais = listaProtudosFiltro.map { it.nome.first().toUpperCase() }
                val letrasUnicas = letrasIniciais.distinct()

                val  adpterFiltroAz = AdapterFiltroAZ(letrasUnicas,this@FragmentProtudos)
                val layoutManagerAz = LinearLayoutManager(requireContext())
                println("Tamanho Filtro ${listaProtudosFiltro.size}")
                CoroutineScope(Dispatchers.Main).launch {

                    binding.recyclerFiltroAZ.adapter = adpterFiltroAz
                    binding.recyclerFiltroAZ.layoutManager = layoutManagerAz
                    adpterProtudos.count = listaProtudosFiltro.size
                    adpterProtudos.listaProtudos = listaProtudosFiltro
                    adpterProtudos.carregando =false
                    adpterProtudos.notifyDataSetChanged()
                    Toast.makeText(context,"Foram encontrados ${listaProtudosFiltro.size} Produtos!" , Toast.LENGTH_LONG).show()
                    limparFiltro= true
                    binding.quatidadefiltros.text = listaFiltroID.size.toString()


            }

        }
    }


    override fun LimparFiltro() {
        protudosIniciais()
        limparFiltro = false
        Toast.makeText(context,"Filtros Limpos com sucesso!" , Toast.LENGTH_LONG).show()
        binding.quatidadefiltros.text = "-"


    }

    fun protudosIniciais(){
        query = "SELECT \n" +
                "Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Estoque.Quantidade,\n" +
                "Progressiva.pf,Carrinho.valor,Carrinho.quantidade,Carrinho.ValorTotal,imagens.imagembase64,\n" +
                "(CASE WHEN Carrinho.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoCarrinho , Estoque.centro, Estoque.quantidade,Repasse.*\n" +
                "FROM TB_produtos Produtos \n" +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                "LEFT JOIN TB_Carrinho Carrinho on Carrinho.Loja_ID = Progressiva.Loja_id \n" +
                "and Carrinho.produto_codigo = Progressiva.Prod_cod and Carrinho.UF = Progressiva.UF \n" +
                "and carrinho.cliente_id = ${clienteSelecionado.Empresa_id}  \n" +
                "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                "LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+
                "where Progressiva.loja_id = ${lojaSelecionada.loja_id} and Progressiva.uf = '${clienteSelecionado.UF}' \n" +
                "and CLI.empresa_id = ${clienteSelecionado.Empresa_id} \n"+
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo order by 1"



        produtos = ProdutosDAO(requireContext())
        listaProtudos = produtos.litar(query)


        val letrasIniciais = listaProtudos.map { it.nome.first().toUpperCase() }
        val letrasUnicas = letrasIniciais.distinct()

        val  adpterFiltroAz = AdapterFiltroAZ(letrasUnicas,this@FragmentProtudos)
        val layoutManagerAz = LinearLayoutManager(requireContext())
        println("Tamano lista inicial ${ listaProtudos.size}")
        CoroutineScope(Dispatchers.Main).launch {

            binding.recyclerFiltroAZ.adapter = adpterFiltroAz
            binding.recyclerFiltroAZ.layoutManager = layoutManagerAz
            adpterProtudos.count = listaProtudos.size
            adpterProtudos.listaProtudos = listaProtudos
            adpterProtudos.carregando =false
            adpterProtudos.notifyDataSetChanged()
            binding.carregandoProduto.isVisible = false

        }
    }

    fun irParaCarrinho(){
        val capcarrinhoitem = binding.quatidadeItens.text

        if(capcarrinhoitem.equals("0")){
            val alertas = Alertas()
            alertas.alerta(requireActivity().supportFragmentManager,"Por favor, Adicione ao menos um item no carrinho!","#B89A00",
                R.drawable.atencao,R.drawable.bordas_amerala_alert)
        }else{
            ActPricipal.lojavalorMinimo = lojaSelecionada.MinimoValor
            ActPricipal.clienteUF = clienteSelecionado.UF
            ActPricipal.cliente_id = clienteSelecionado.Empresa_id
            ActPricipal.loja_id =lojaSelecionada.loja_id
            val intent  = Intent(requireContext(), ActCarrinhoDetalhe::class.java)
            intent.putExtra("CarrinhoDetalhe",false)
            startActivityForResult(intent, 6)
        }
    }
}
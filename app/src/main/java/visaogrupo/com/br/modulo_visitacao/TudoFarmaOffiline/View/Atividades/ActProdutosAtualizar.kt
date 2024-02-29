package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActProdutosAtualizarBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaFiltrosProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaLetraFiltro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.ExcluiItemcarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.ExcluirPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.LimparFiltrosProdutos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterFiltroAZ
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.ProtudoAdapter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogFiltro

class ActProdutosAtualizar : AppCompatActivity() , StartaAtividade,
    ExcluiItemcarrinho, AtualizaLetraFiltro, AtualizaFiltrosProduto, LimparFiltrosProdutos, ExcluirPedido {

    lateinit var binding: ActivityActProdutosAtualizarBinding
    var pedido: PedidoFinalizado? = null
    private var limparFiltro = false

    lateinit var   adpterProtudos : ProtudoAdapter
    var listaProtudos = mutableListOf<ProdutoProgressiva>()
    var query = ""
    lateinit var produtos: ProdutosDAO
    var queryFiltro =""

    val contextThis = this
    val context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActProdutosAtualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pedido = intent.getSerializableExtra("PedidoClicado") as? PedidoFinalizado


        adpterProtudos = ProtudoAdapter(listaProtudos, contextThis,contextThis,pedido!!.lojaId,pedido!!.clienteId,contextThis,binding.root,true,pedido!!.pedidoID.toInt(), true, this,pedido!!, uf = pedido!!.uf)
        val layoutManager = LinearLayoutManager(baseContext)
        binding.recyProtudo.adapter = adpterProtudos
        binding.recyProtudo.layoutManager = layoutManager

        binding.filtros.setOnClickListener {
            val dialogFiltro = DialogFiltro()
            dialogFiltro.dialogFiltro(context,this,limparFiltro,this, pedido!!.lojaId)
        }
        binding.FinalizaAlteracoes.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            intent.putExtra("excluirPedido", false)
            finish()
        }

        binding.edtBuscaProdutos.addTextChangedListener(object: TextWatcher {
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

            try {
                MainScope().launch {
                    binding.carregandoProduto.isVisible = true

                }
                protudosIniciais()
                atualizaProgressBar()
                quatidadeinCarrinho()
            }catch (e:Exception){
                e.printStackTrace()
            }


        }


        // atualiza a view
        try {
            val cnpj = pedido!!.cnpj!!.substring(0,2)+"."+pedido!!.cnpj!!.substring(2,5)+
                    "."+pedido!!.cnpj!!.substring(5,8)+"/"+pedido!!.cnpj!!.substring(8,12) +"-"+
                    pedido!!.cnpj!!.substring(12,14);
            binding.cnpjClienteSelecionado.text =cnpj
        }catch (e:Exception){
            e.printStackTrace()
            binding.cnpjClienteSelecionado.text =pedido!!.cnpj
        }
        binding.lojasSelecionadas.text = pedido!!.nomeLoja
        binding.textRazaosocialclienteSelecionado.text = pedido!!.razaoSocial
        binding.valorMinimo.text = "R$ " +pedido!!.valorMinimoLoja.toString()






    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        intent.putExtra("excluirPedido", false)
        finish()
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
            MainScope().launch {
                val valorFormatado = String.format("%.2f", valorSomado)
                binding.TotalCarrinho.setText("R$ " + valorFormatado.replace(".",","))
                binding.progressBarValorminimo.max =pedido?.valorMinimoLoja!!.toInt()
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
        produtos = ProdutosDAO(baseContext)
        listaProtudos.clear()
        var queryAtualiza =""
        if (limparFiltro){
            queryAtualiza = queryFiltro
        }else{
            queryAtualiza = query

        }
        listaProtudos = produtos.litarPedidosProdutos(queryAtualiza)
        adpterProtudos.listaProtudos =listaProtudos
        adpterProtudos.notifyDataSetChanged()

        atualizaProgressBar()
        quatidadeinCarrinho()
    }

    override fun exluiItem() {
        atualizaItemDaView()
        quatidadeinCarrinho()
    }
    fun  quatidadeinCarrinho(){
        val carrinhoDAO=  CarrinhoDAO(baseContext)
        val count = carrinhoDAO.quantidadeItens(pedido!!.clienteId, pedido!!.lojaId)
        binding.quatidadeItens.text = count.toString()
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
                    "Progressiva.pf,imagens.imagembase64, Estoque.centro, Estoque.quantidade as qtdEstoque, PedProd.valor, \n" +
                    "(CASE WHEN PedProd.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoPedido, PedProd.quantidade as QtdPedido,Repasse.*\n" +
                    "FROM TB_produtos Produtos \n" +
                    "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                    "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                    "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                    "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                    "LEFT JOIN TBPedidosFinalizados AS Ped on Ped.cliente_id = CLI.empresa_id and Ped.loja_id = Progressiva.loja_id AND PEd.pedidoid = ${pedido!!.pedidoID}\n" +
                    "LEFT JOIN TB_Produtos_Pedidos_Finalizado PedProd on PedProd.PedidoID = Ped.pedidoid \n" +
                    "                                                  and PedProd.produto_codigo = Progressiva.prod_cod \n" +
                     "LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+

                     "where Progressiva.loja_id = ${pedido!!.lojaId} and Progressiva.uf = '${pedido!!.uf}' AND ClI.Empresa_id = ${pedido!!.clienteId}\n" +
                    filtroIdNull+
                    "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo order by ${filtro}"





            produtos = ProdutosDAO(baseContext)
            listaProtudosFiltro = produtos.litarPedidosProdutos(queryFiltro)


            val letrasIniciais = listaProtudosFiltro.map { it.nome.first().toUpperCase() }
            val letrasUnicas = letrasIniciais.distinct()

            val  adpterFiltroAz = AdapterFiltroAZ(letrasUnicas,contextThis)
            val layoutManagerAz = LinearLayoutManager(baseContext)
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
                "Progressiva.pf,imagens.imagembase64, Estoque.centro, Estoque.quantidade as qtdEstoque, PedProd.valor, \n" +
                "(CASE WHEN PedProd.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoPedido, PedProd.quantidade as QtdPedido,Repasse.*\n" +
                "FROM TB_produtos Produtos \n" +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                "LEFT JOIN TBPedidosFinalizados AS Ped on Ped.cliente_id = CLI.empresa_id and Ped.loja_id = Progressiva.loja_id AND PEd.pedidoid = ${pedido!!.pedidoID}\n" +
                "LEFT JOIN TB_Produtos_Pedidos_Finalizado PedProd on PedProd.PedidoID = Ped.pedidoid \n" +
                "                                                  and PedProd.produto_codigo = Progressiva.prod_cod \n" +
                "LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+
                "where Progressiva.loja_id = ${pedido!!.lojaId} and Progressiva.uf = '${pedido!!.uf}' AND ClI.Empresa_id = ${pedido!!.clienteId}\n" +
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo \n" +
                "order by 1"



        produtos = ProdutosDAO(baseContext)
        listaProtudos = produtos.litarPedidosProdutos(query)


        val letrasIniciais = listaProtudos.map { it.nome.first().toUpperCase() }
        val letrasUnicas = letrasIniciais.distinct()

        val  adpterFiltroAz = AdapterFiltroAZ(letrasUnicas,contextThis)
        val layoutManagerAz = LinearLayoutManager(baseContext)
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

    override fun excluirPedido() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        intent.putExtra("excluirPedido", true)
        finish()
    }
}
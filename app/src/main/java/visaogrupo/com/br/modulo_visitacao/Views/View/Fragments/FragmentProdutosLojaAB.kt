package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.celula_loja_grupo_ab.nomeGrupo
import kotlinx.android.synthetic.main.celula_opl.quantidade
import kotlinx.android.synthetic.main.fragment_produtos_loja_a_b.lojaSelecionada
import kotlinx.android.synthetic.main.fragment_produtos_loja_a_b.porcentagemB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaProgress
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoAB
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoValorAB
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.GrupoLojaAbDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdpterGrupoABProdutos
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActCarrinhoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentProdutosLojaABBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentProtudosBinding


class FragmentProdutosLojaAB (carrinhoVisible: carrinhoVisible) : Fragment(),AtualizaProgress {
    lateinit var binding: FragmentProdutosLojaABBinding
    val context = this
    val carrinhoVisible = carrinhoVisible
    var listaValorTotalA = mutableListOf<ProdutoValorAB>()
    var listaValorTotalB = mutableListOf<ProdutoValorAB>()
    var listaCarrinho = mutableListOf<ProdutoAB>()
    var listaCarrinhoValida = mutableListOf<ProdutoAB>()

    lateinit var clienteSelecionado: Clientes
    var porcentagemBglobal =0.0
    var nomeGrupo =""
    var apareceAlerta = false
    lateinit var lojaSelecionada:Lojas;
    lateinit var login:Login
    var  insert= false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProdutosLojaABBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = binding.root
        carrinhoVisible.carrinhoVisivel()

        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val gsonclientes = Gson()
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val grupoLojaAbDAO = GrupoLojaAbDAO(requireContext())
        clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
        val gsonuserid = Gson()
        val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
        login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)

        val listaProtudos = grupoLojaAbDAO.listarLoja(lojaSelecionada.loja_id, clienteSelecionado.CODLISTAPRECOSYNC)
        binding.lojaSelecionada.text = lojaSelecionada.nome

        val adapterGrupoLoja = AdpterGrupoABProdutos(listaProtudos,requireContext(),
            listaValorTotalA,listaValorTotalB, this )
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyLojaAb.layoutManager = linearLayoutManager
        binding.recyLojaAb.adapter = adapterGrupoLoja

        var porcentagemA =  listaProtudos[0].Porc /100
        var porcentagemB =listaProtudos[1].Porc /100

        binding.porcentagemA.text = listaProtudos[0].Porc.toString() + "%"
        binding.porcentagemB.text = listaProtudos[1].Porc.toString() + "%"

        porcentagemBglobal = listaProtudos[1].Porc
        nomeGrupo  =   listaProtudos[1].Grupo

        val layoutParamsA = binding.contrainGrupoA.layoutParams as LinearLayout.LayoutParams
        val layoutParamsB = binding.contrainGrupoA.layoutParams as LinearLayout.LayoutParams
        layoutParamsA.weight = porcentagemA.toFloat()
        layoutParamsB.weight = porcentagemB.toFloat()

        formatarDescricao(listaProtudos[1].Porc.toString(), listaProtudos[1].Grupo)
        for (i in listaProtudos){
            for (k in i.listaProduto!!){
                if (k.estaNoCarrinho == 1){
                    binding.recyLojaAb.smoothScrollToPosition(1)
                    listaCarrinho.add(k)

                }
            }
        }


        binding.progresB.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyLojaAb.smoothScrollToPosition(1)
            }
        }

        binding.progressA.setOnClickListener {
            binding.recyLojaAb.smoothScrollToPosition(0)

        }

        binding.carrinhoProtudoAb.setOnClickListener {
              if (listaCarrinhoValida.isEmpty()){
               val dialogErro = DialogErro()
               dialogErro.Dialog(requireContext(),"Atenção","Adicione pelo menos ${porcentagemBglobal.toString().replace(".0","")}% do ${nomeGrupo} para continuar","Ok",""){

               }
              }else{
                  ActPricipal.lojavalorMinimo = lojaSelecionada.MinimoValor
                  ActPricipal.clienteUF = clienteSelecionado.UF
                  ActPricipal.cliente_id = clienteSelecionado.Empresa_id
                  ActPricipal.loja_id =lojaSelecionada.loja_id
                  val intent  = Intent(requireContext(), ActCarrinhoDetalhe::class.java)
                  intent.putExtra("CarrinhoDetalhe",false)
                  startActivityForResult(intent, 7)
              }
        }
        binding.xFechamensagem.setOnClickListener {
             binding.visualizarCodicoes.isVisible = true
             binding.viewFechado.isVisible = false
            val layoutParams = binding.recyLojaAb.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = 24
            binding.recyLojaAb.layoutParams = layoutParams
        }

        binding.visualizarCodicoes.setOnClickListener {
            binding.visualizarCodicoes.isVisible = false
            binding.viewFechado.isVisible = true
            val layoutParams = binding.recyLojaAb.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = 0
            binding.recyLojaAb.layoutParams = layoutParams
        }

        return  view;
    }


    fun formatarDescricao(porcentagemB:String, nomeGrupo:String){
        val grupoB = "${nomeGrupo}"
        val textoNegrito30 = "${porcentagemB.toString().replace(".0","")}%"

        val textoCompleto = "Faça seu pedido com pelo menos ${porcentagemB.toString().replace(".0","")}% de valor em R$ nos produtos do ${nomeGrupo} para aproveitar descontos."
        val startIndexGrupoB = textoCompleto.indexOf(grupoB)
        val endIndexGrupoB = startIndexGrupoB + grupoB.length
        val startIndex30 = textoCompleto.indexOf(textoNegrito30)
        val endIndex30 = startIndex30 + textoNegrito30.length
        val spannableBuilder = SpannableStringBuilder(textoCompleto)
        val boldStyleGrupoB = StyleSpan(Typeface.BOLD)
        spannableBuilder.setSpan(boldStyleGrupoB, startIndexGrupoB, endIndexGrupoB, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE)
        val boldStyle30 = StyleSpan(Typeface.BOLD)
        spannableBuilder.setSpan(boldStyle30, startIndex30, endIndex30, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE)

        binding.descricaoLoja.text = spannableBuilder
    }

    override fun atualizaprogress(valorTotalA:MutableList<ProdutoValorAB>,
                                  valorTotalB:MutableList<ProdutoValorAB>,
                                  tipoA:Boolean,
                                  mais:Boolean,
                                  itemProdutoAB: ProdutoAB,
                                  getQuantidade:Int,
                                  temNaLista:Boolean,
                                  valorTotal:Double) {

            var  valorTotalGrupoA = 0.0
            var  valorTotalGrupoB = 0.0
            var  temNaLista = false


            if (listaCarrinho.isEmpty()){
                 listaCarrinho.add(itemProdutoAB)
            }else{
                 for ( (i,valor) in listaCarrinho.withIndex()){
                     if (valor.Produto_codigo == itemProdutoAB.Produto_codigo ){
                         listaCarrinho[i].valorTotal = valorTotal
                         listaCarrinho[i].Quantidade =getQuantidade
                         temNaLista = true
                         break
                     }else{

                         temNaLista = false

                     }
                 }
                 if (!temNaLista){
                     itemProdutoAB.valorTotal = valorTotal
                     listaCarrinho.add(itemProdutoAB)
                 }

           }

            for ( a in valorTotalA){
                valorTotalGrupoA += a.valortotalProduto
            }
            for (b in valorTotalB){
                valorTotalGrupoB += b.valortotalProduto

            }

            val valorTotalDosGrupos = valorTotalGrupoA +valorTotalGrupoB
            val  porcentagemProgressA = (valorTotalGrupoA / valorTotalDosGrupos) * 100
            val valorFomatocentagemA = String.format("%.2f",porcentagemProgressA)
            val valorFomatTotalA = String.format("%.2f",valorTotalGrupoA)
            binding.porcentagemA.text = valorFomatocentagemA+ "%"
            binding.valorTotalA.text = "R$ " + valorFomatTotalA
          /*  val animationDuration = 200L
            val finalProgress = porcentagemProgressA.toInt()
            val animator = ObjectAnimator.ofInt(binding.progressA, "progress", finalProgress)
            animator.duration = animationDuration
            animator.start()*/

            val  porcentagemProgressB = (valorTotalGrupoB / valorTotalDosGrupos) * 100
            val valorFomatocentagemB = String.format("%.2f",porcentagemProgressB)
            val valorFomatTotalB = String.format("%.2f",valorTotalGrupoB)

            binding.imgcerto.isVisible = porcentagemProgressB >= porcentagemBglobal
            binding.porcentagemB.text = valorFomatocentagemB + "%"
            binding.valorTotalB.text = "R$ " + valorFomatTotalB


            if (porcentagemProgressB >= porcentagemBglobal) {

                if (!apareceAlerta){
                    val alertas = Alertas()
                    alertas.alerta(requireActivity().supportFragmentManager,"Você atingiu ${porcentagemBglobal.toString().replace(".0","")} do ${nomeGrupo}, aproveite os x% de desconto","#FF018786",
                        R.drawable.icone_certo_verde,R.drawable.bordas_verde_alert)
                        apareceAlerta = true

                }
                for (i in listaCarrinho){
                    binding.quatidadeItens.text = listaCarrinho.size.toString()
                    insertProdutoBanco(mais, i, i.Quantidade, temNaLista, i.valorTotal,insert)
                }
                listaCarrinhoValida.clear()
                listaCarrinhoValida.addAll(listaCarrinho)
                val color = ContextCompat.getColor(requireContext(), R.color.verdenutoon)
                binding.progresB.progressTintList =  ColorStateList.valueOf(color)
                val animationDurationB = 600L
                val animatorB = ObjectAnimator.ofInt(binding.progresB, "progress", binding.progresB.max)
                animatorB.duration = animationDurationB
                animatorB.start()
                insert = false


            } else {
                listaCarrinhoValida.clear()
                binding.quatidadeItens.text = "-"

                insert = true
                val carrinhoDAO = CarrinhoDAO(requireContext())

                carrinhoDAO.excluirItemAB(lojaSelecionada.loja_id,clienteSelecionado.Empresa_id)

                val color = ContextCompat.getColor(requireContext(), R.color.laranjalojaB)
                binding.progresB.progressTintList =  ColorStateList.valueOf(color)
                val animationDurationB = 200L
                val finalProgressB = porcentagemProgressB.toInt() +12
                val animatorB = ObjectAnimator.ofInt(binding.progresB, "progress", finalProgressB)
                animatorB.duration = animationDurationB
                animatorB.start()
                apareceAlerta = false

            }
    }
     fun insertProdutoBanco(mais:Boolean,
                                   itemProdutoAB:ProdutoAB,
                                   getQuantidade:Int,
                                   temNaLista:Boolean,
                                   valorTotal:Double,
                                    insert:Boolean){


        var valorQuantidadecarrinho = 0
        if (mais){
            valorQuantidadecarrinho = getQuantidade +1
        }else{
            valorQuantidadecarrinho = getQuantidade -1
        }

        val  daataformat = DataAtual()
        val  data = daataformat.recuperaData()


        val carrinho = Carrinho(itemProdutoAB.Loja_id,clienteSelecionado.Empresa_id,
            itemProdutoAB.Produto_codigo,
            "",
            login.Usuario_id.toString().toInt(),clienteSelecionado.UF,
            0.0,
            0.0,
            0,itemProdutoAB.Barra,valorQuantidadecarrinho.toInt(),
            itemProdutoAB.PF, valorTotal, itemProdutoAB.PF,itemProdutoAB.grupoCodigo,itemProdutoAB.Desconto,
            itemProdutoAB.Desconto,0.0,"",itemProdutoAB.CODLISTAPRECOSYNC,"",valorTotal,
            itemProdutoAB.Nome,lojaSelecionada.nome,clienteSelecionado.RazaoSocial,
            clienteSelecionado.CNPJ,data,lojaSelecionada.MinimoValor,itemProdutoAB.Imagem,
            0.0,clienteSelecionado.FormaPagamentoExclusiva,itemProdutoAB.CaixaPadrao,lojaSelecionada.Qtd_Minima_Operador,
            lojaSelecionada.Qtd_Maxima_Operador,0,lojaSelecionada.RegraPrazoMedio,lojaSelecionada.LojaTipo,0,itemProdutoAB.Desconto)

        val carrinhoDAO = CarrinhoDAO(requireContext())
         if (insert){
             carrinhoDAO.insertCarrinho(carrinho)

         }else{
             if (temNaLista){
                 carrinhoDAO.atualizaItemCarrinho(carrinho)

             }else{
                 carrinhoDAO.insertCarrinho(carrinho)
             }
         }
    }
}
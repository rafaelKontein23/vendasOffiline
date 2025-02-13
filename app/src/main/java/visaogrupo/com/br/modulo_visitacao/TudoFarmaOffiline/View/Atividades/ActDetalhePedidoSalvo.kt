package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActDetalhePedidoSalvoBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.DetalhePedidoSalvoPresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaValorPedidoKitPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.ExcluiPedidoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CustomSpinerFormDePag
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.OperadorLogistico
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.OperadorLogisticaDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterFormDePagSpiner
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterTituloKitPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdpterProdutosPedidos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.OperadorAdpter
import java.io.Serializable

class ActDetalhePedidoSalvo : AppCompatActivity() , AtualizaValorPedidoKitPedido,ExcluiPedidoKit{
    private  lateinit var  binding: ActivityActDetalhePedidoSalvoBinding
    val context = this
    var oplId = mutableListOf<String>()
    var pedido: PedidoFinalizado? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityActDetalhePedidoSalvoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pedido = intent.getSerializableExtra("PedidoClicado") as? PedidoFinalizado
        BuscaInfos()

        binding.atualizarItensCarrinho.setOnClickListener {
            val intent = Intent(context,ActProdutosAtualizar::class.java)
            intent.putExtra("PedidoClicado", pedido as Serializable)
            startActivityForResult(intent,4)
        }

        binding.voltarPedido.setOnClickListener {
            val  intent = Intent()
            intent.putExtra("excluirPedido",true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.atualizaInfos.setOnClickListener {
             val formadepagamentocap = binding.formaDepagamentospiner.selectedItem.toString()
             val detalhePedidoSalvoPresenter = DetalhePedidoSalvoPresenter()

            if (!oplId.isEmpty() && !formadepagamentocap.contains("Selecionar")){
                val pedidosFinalizadosDAO = PedidosFinalizadosDAO(context)
                val formaDePagmento = detalhePedidoSalvoPresenter.infoFormaDePagamentoSalvo(
                    context,
                    pedido!!.valorTotal!!.toDouble(),
                    pedido!!.RegraPrazo,pedido!!.cnpj!!,
                    formadepagamentocap,
                    pedido!!
                )

                pedidosFinalizadosDAO.atualizarItem(
                    pedido!!.pedidoID.toInt(),
                    oplId.toString(),
                    formadepagamentocap,
                    formaDePagmento!!,pedido!!.kit
                )

                val  intent = Intent()
                intent.putExtra("excluirPedido",true)
                setResult(Activity.RESULT_OK, intent)
                finish()
                Toast.makeText(context,"Informações atualizadas com sucesso!" , Toast.LENGTH_SHORT).show()

            }else{
                if (oplId.isEmpty() && formadepagamentocap.contains("Selecionar") ){
                    Toast.makeText(context,"Selecione o operador logístico, Selecione uma forma de pagamento " , Toast.LENGTH_SHORT).show()

                }else if (oplId.isEmpty()){
                    Toast.makeText(context,"Selecione o operador logístico" , Toast.LENGTH_SHORT).show()

                }else if (formadepagamentocap.contains("Selecionar")){
                    Toast.makeText(context,"Selecione uma forma de pagamento" , Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onBackPressed() {
        val  intent = Intent()
        intent.putExtra("excluirPedido",true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
             if (data!!.getBooleanExtra("excluirPedido",false)){
                 val  intent = Intent()
                 intent.putExtra("excluirPedido",true)
                 setResult(Activity.RESULT_OK, intent)
                 finish()
             }else{
                 BuscaInfos()
             }
        }
    }

    fun BuscaInfos(){
        CoroutineScope(Dispatchers.IO).launch {
            val DetalhePedidoSalvoPresenter = DetalhePedidoSalvoPresenter()
            val infosDetalhesPedidoSalvo = DetalhePedidoSalvoPresenter.buscaInfosDetalhePedidoSalvo(pedido!!,context)

            MainScope().launch {
                val adapterForm = AdapterFormDePagSpiner(context,1, infosDetalhesPedidoSalvo.listaFormPag)
                val oplAdapter  = OperadorAdpter(infosDetalhesPedidoSalvo.listaOplspner,pedido!!.quantidadeMaximaOpl,context, oplId  )
                val layoutMeneger = GridLayoutManager(context,2)

                binding.recyclerOpl.layoutManager = layoutMeneger
                binding.recyclerOpl.adapter= oplAdapter
                binding.formaDepagamentospiner.adapter = adapterForm
                binding.atualizarItensCarrinho.isVisible = pedido?.TipoLoja != 13

                val pedidosFinalizadosDAO = PedidosFinalizadosDAO(applicationContext)
                var listaProdutos  = mutableListOf<ProdutosFinalizados>()

                if (pedido!!.kit == 1){
                    val kitTituloLista = pedidosFinalizadosDAO.listaPedidoKit(pedido!!.pedidoID.toInt())
                    val linearlayout = LinearLayoutManager(baseContext)
                    val adapterTituloKitPedido = AdapterTituloKitPedido(kitTituloLista,baseContext, pedido!!, context, context)

                    binding.recyKIt.layoutManager = linearlayout
                    binding. recyKIt.adapter = adapterTituloKitPedido
                    binding.totalPedido.text = "Total : ${FormataValores.formatarParaMoeda(pedido!!.valorTotal!!)} "
                    binding.recyProdutos.isVisible = false
                    binding.recyKIt.isVisible = true
                    binding.descontoMedio.isVisible= false
                    binding.atualizarItensCarrinho.isVisible = false
                    val params = binding.textView35.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = binding.recyKIt.id
                    binding.textView35.layoutParams = params

                }else{
                    val valorTotalPedido = pedidosFinalizadosDAO.somarTotalPedido(pedido!!.pedidoID)
                    pedidosFinalizadosDAO.atualizaValortotalPedidoComum(valorTotalPedido,pedido!!.pedidoID)

                    if (pedido != null) {
                        listaProdutos =  pedidosFinalizadosDAO.listarPedidosProdutos(pedido!!.pedidoID)
                    }
                    val adpterProdutosPedidos = AdpterProdutosPedidos(listaProdutos,context)
                    val linearLayout = LinearLayoutManager(context)

                    binding.recyProdutos.layoutManager = linearLayout
                    binding. recyProdutos.adapter = adpterProdutosPedidos
                    binding.descontoMedio.text = pedido!!.desconto.toString() + "% méd."
                    binding.totalPedido.text = "Total : ${FormataValores.formatarParaMoeda(valorTotalPedido)}"
                    binding.recyProdutos.isVisible = true
                    binding.recyKIt.isVisible = false
                }
            }
        }
    }


    override fun atualizaValorPedidoKitPedido(valorTotal: Double) {
        binding.totalPedido.text = FormataValores.formatarParaMoeda(valorTotal)
    }

    override fun excluiPedidoKit() {
        val  intent = Intent()
        intent.putExtra("excluirPedido",true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
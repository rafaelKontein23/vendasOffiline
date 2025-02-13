package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_act_protudo_detalhe.edtQuantidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActProtudoDetalheBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.ProdutoDetalhePresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaQuantidadeProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaValorProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.ProgressivaAdpter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogProgressiva

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.*
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ActProtudoDetalhe : AppCompatActivity(), AtualizaProgressiva, AtualizaValorProduto , AtualizaQuantidadeProduto{

    var listaProgressiva :MutableList<ProgressivaLista> = mutableListOf<ProgressivaLista>()
   private  lateinit var  binding: ActivityActProtudoDetalheBinding
   lateinit var  progressivaAdpter: ProgressivaAdpter
   lateinit var login: Login
   var listaProgressivaOriginal :MutableList<ProgressivaLista> = mutableListOf<ProgressivaLista>()

    val atualizaValorProduto: AtualizaValorProduto = this
    val  atualizaQuantidadeProduto :AtualizaQuantidadeProduto = this
    lateinit  var lojaSelecionada:Lojas
    lateinit var clienteSelecionado:Clientes
    val  lojasxclienets = mutableListOf<LojaXCliente>()
    var repasse = 0.0
    val produtoDetalhePresenter = ProdutoDetalhePresenter()

    companion object {
        lateinit var progressivaSelecionada: ProgressivaSelecionada
    }

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActProtudoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // busca itens necessarios para montar a tela
        val bundle = intent.getBundleExtra("ProtudoSelecionado_bundle")
        val protudoSelecionado = bundle?.getSerializable("ProtudoSelecionado") as ProdutoProgressiva
        val bundleiamgem = intent.getBundleExtra("ImagemProd_bundle")
        val imagemBase64 = bundleiamgem?.getString("ImagemProd")
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val gsonuserid = Gson()
        val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
        login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)

        var  pedidoFinalizado = intent.getSerializableExtra("PedidoClicado") as? PedidoFinalizado
        val  estaNoCarrinho  = intent.getIntExtra("estaNoCarrinho",0)
        val  estaNoPedidoSalvo = intent.getBooleanExtra("estaNoPedido",false)
        val  pedidodID = intent.getIntExtra("pedidoID",0)
        val  pedido = intent.getBooleanExtra("Pedido",false)
        val  carrinhoDetalhe = intent.getBooleanExtra("CarrinhoDetalhe",false)


       if (pedido){
           var anr =0
           if (pedidoFinalizado!!.anr == true){
               anr = 1
           }
           lojaSelecionada = Lojas(pedidoFinalizado!!.lojaId,pedidoFinalizado.nomeLoja.toString(),0,
               pedidoFinalizado!!.valorMinimoLoja!!.toDouble(),
               pedidoFinalizado.TipoLoja,"","",0,
               "",0.0,0,0,
               "",0,"",0,"","",0,
               pedidoFinalizado!!.qtdMinimaOperador!!.toInt(),
               pedidoFinalizado!!.qtdMaximaOperador!!.toInt(),"",0.0,"",
               "",0,anr,pedidoFinalizado!!.RegraPrazo.toInt(),"")


           clienteSelecionado = Clientes("","","","",
               "",pedidoFinalizado!!.cnpj.toString(),
               "",0,0,"",
               0,"","",0, false,"","","",0,"",pedidoFinalizado!!.clienteId.toInt(),
               "","","",0,0,"","",0,lojasxclienets,"","","","",
               "","",pedidoFinalizado!!.razaoSocial!!,"","","",pedidoFinalizado!!.uf,"","",pedidoFinalizado!!.formaPagamentoExclusiva!!.toInt())
       }else {
           if (carrinhoDetalhe){
               val objetoSerializadoLoja = sharedPreferences?.getString("LojaSelecionadaCarrinho", null)
               val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionadaCarrinho", null)
               lojaSelecionada =  gson.fromJson(objetoSerializadoLoja, Lojas::class.java)
               clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
           }else{
               val objetoSerializadoLoja = sharedPreferences?.getString("LojaSelecionada", null)
               val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
               lojaSelecionada =  gson.fromJson(objetoSerializadoLoja, Lojas::class.java)
               clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
           }

       }

        binding.PMC.text = "R$ " + protudoSelecionado.PMC.toString()
        binding.PF.text = "R$ " + protudoSelecionado.valor
        binding.repasse.text =  protudoSelecionado.porcentagem.toString().replace(".",",") + "%"
        binding.estoque.text = protudoSelecionado.quatidade.toString()
        binding.caixaPadrao.text = protudoSelecionado.caixapadrao.toString()
        binding.nomeProtudo.text = protudoSelecionado.nome
        binding.barra.text=  protudoSelecionado.barra
        binding.codigoProduto.text = protudoSelecionado.ProdutoCodigo.toString()
        binding.checkCaixapadrao.text = "Caixa Padrão ${protudoSelecionado.caixapadrao.toString()} uni."
        binding.estoque.text = protudoSelecionado.quantidadeEstoque.toString()

        if(!imagemBase64?.isEmpty()!!){
            val bitmap =exibirImagemBase64(imagemBase64.toString())
            binding.imgprodto.setImageBitmap(bitmap)
        }

       binding.switANR.isVisible = lojaSelecionada.ANR ==  1

       val checkedChangeListener = object : CompoundButton.OnCheckedChangeListener {
           override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
               if (isChecked){
                   progressivaAdpter.anr = true
                   progressivaAdpter.notifyDataSetChanged()
               }else {
                   val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()
                   progressivaAdpter.listaProgrssiva = listaProgressivaOriginal
                   progressivaAdpter.soma = true
                   progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap
                   progressivaAdpter.anr = false
                   progressivaAdpter.notifyDataSetChanged()
               }
           }
       }

       binding.switANR.setOnCheckedChangeListener(checkedChangeListener)

       CoroutineScope(Dispatchers.IO).launch {
             val (listaprogressivaFun , listaProgressivaOriginalFun, quantidade) = produtoDetalhePresenter.buscaProgressiva(baseContext,
                  listaProgressiva,
                  listaProgressivaOriginal,
                  lojaSelecionada,
                  clienteSelecionado,
                  protudoSelecionado,
                  estaNoCarrinho
              )
            listaProgressiva.addAll(listaprogressivaFun)
            listaProgressivaOriginal.addAll(listaProgressivaOriginalFun)
            binding.edtQuantidade.setText(quantidade)

            repasse  = binding.repasse.text.toString().replace("%","").replace(",",".").toDouble()
            progressivaAdpter = ProgressivaAdpter(listaProgressiva,baseContext,binding.recyProgressiva,supportFragmentManager, atualizaValorProduto, binding.edtQuantidade,atualizaQuantidadeProduto,repasse)
            val layoutManager = LinearLayoutManager(baseContext)
            CoroutineScope(Dispatchers.Main).launch {
                 binding.recyProgressiva.layoutManager = layoutManager
                 binding.recyProgressiva.adapter = progressivaAdpter
                if (estaNoCarrinho == 1){
                    progressivaAdpter.quantidadeAdionada = protudoSelecionado.quantidadeCarrinho
                }else{
                    progressivaAdpter.quantidadeAdionada = listaProgressiva[0].quantidade
                }
                progressivaAdpter.clicou= false
                progressivaAdpter.notifyDataSetChanged()
             }
         }

        binding.edtQuantidade.setOnFocusChangeListener(object : OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                 if (!hasFocus){
                     if(binding.edtQuantidade.text.isEmpty()){
                         binding.edtQuantidade.setText("0")
                     }
                     val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()
                     if (quatidadeAdicionadaCap < listaProgressiva[0].quantidade){
                         Toast.makeText(baseContext,"A Quantidade minima é de ${listaProgressiva[0].quantidade}",Toast.LENGTH_SHORT).show()
                         binding.edtQuantidade.setText(listaProgressiva[0].quantidade.toString())

                     }else {
                         binding.edtQuantidade.isEnabled = false
                         val quantidade:Int = quatidadeAdicionadaCap
                         progressivaAdpter.quantidadeAdionada = quantidade
                         progressivaAdpter.clicou= false
                         progressivaAdpter.notifyDataSetChanged()
                     }
                 }
             }
        })

        binding.btnMais.setOnClickListener {
            val quantidadecaixapradrao = binding.caixaPadrao.text.toString().toInt()
            if (binding.checkCaixapadrao.isChecked){
                val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()
                val quatidadeAdicionadaCapFun = produtoDetalhePresenter.calculaCaixaparaoMais(quatidadeAdicionadaCap,quantidadecaixapradrao)

                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quatidadeAdicionadaCapFun.toString())
                progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCapFun
                progressivaAdpter.clicou= false
                progressivaAdpter.soma = true
                progressivaAdpter.notifyDataSetChanged()

            }else{
                val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString()
                val quantidade:Int = quatidadeAdicionadaCap.toInt() +1

                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quantidade.toString())
                progressivaAdpter.quantidadeAdionada = quantidade
                progressivaAdpter.clicou= false
                progressivaAdpter.soma = true
                progressivaAdpter.notifyDataSetChanged()
            }
        }

        binding.btnmenos.setOnClickListener {
            var quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()

           if (quatidadeAdicionadaCap <=listaProgressiva[0].quantidade){
               Toast.makeText(baseContext,"A Quantidade minima é de ${quatidadeAdicionadaCap}",Toast.LENGTH_SHORT).show()
           }else {
               if (binding.checkCaixapadrao.isChecked){
                   if(quatidadeAdicionadaCap < listaProgressiva[0].quantidade ){
                       Toast.makeText(baseContext,"A Quantidade minima é de ${quatidadeAdicionadaCap}",Toast.LENGTH_SHORT).show()
                       binding.edtQuantidade.setText(listaProgressiva[0].quantidade)
                   }else{
                       val quantidadecaixapradrao = binding.caixaPadrao.text.toString().toInt()

                       val quatidadeAdicionadaCapfun = produtoDetalhePresenter.calculaCaixaparaoMenos(
                           quantidadecaixapradrao,
                           quatidadeAdicionadaCap,
                           listaProgressiva
                       )

                       binding.edtQuantidade.setText(quatidadeAdicionadaCapfun.toString())
                       progressivaAdpter.soma = false
                       progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCapfun
                       progressivaAdpter.clicou= false
                       progressivaAdpter.notifyDataSetChanged()
                   }
               }else{
                   if(quatidadeAdicionadaCap == 0){
                       binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable("0")
                   }else{

                       val quantidade:Int = quatidadeAdicionadaCap -1
                       binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quantidade.toString())
                       progressivaAdpter.soma = false
                       progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap  -1
                       progressivaAdpter.clicou= false
                       progressivaAdpter.notifyDataSetChanged()

                   }
               }
           }
        }

        binding.adicionarProgressiva.setOnClickListener {
            val dialogProgressiva = DialogProgressiva()
            val valorProduto = binding.PF.text.toString().replace("R$","").replace(" ","").replace(",",".")
            dialogProgressiva.dialog(this,valorProduto.toDouble(),protudoSelecionado.nome,protudoSelecionado,this)
        }

        binding.adiconarCarrinho.setOnClickListener {
           val quantidadeCap =  binding.edtQuantidade.text.toString().toInt()
           if(quantidadeCap.toString().isEmpty() ){
               Toast.makeText(baseContext,"Por favor adicione ao menos uma quantidade para adicionar ao carrinho",Toast.LENGTH_SHORT).show()
           }else if (quantidadeCap == 0 ){
               Toast.makeText(baseContext,"Por favor adicione ao menos uma quantidade para adicionar ao carrinho",Toast.LENGTH_SHORT).show()

           } else{
               var valortotal = FormataValores.converterMoedaParaDouble(binding.valorTotal.text.toString())
               produtoDetalhePresenter.adiconarAoCarrinho(
                   baseContext,
                   listaProgressivaOriginal,
                   lojaSelecionada,
                   clienteSelecionado,
                   protudoSelecionado,
                   estaNoCarrinho,
                   pedidodID,
                   binding.switANR.isChecked,
                   valortotal!!,
                   pedido,
                   estaNoPedidoSalvo,
                   imagemBase64
                   ,quantidadeCap,
                   login.Usuario_id.toString().toInt()
               )

               val intent = Intent()
               setResult(Activity.RESULT_OK, intent)
               finish()
           }
        }

       binding.voltarProtudo.setOnClickListener {
           this.onBackPressed()
       }
    }

    override fun ProgressivaAtualiza(
        ProtudoCodigo: Int,
        protudoseleciona: ProdutoProgressiva,
        quatidade: Int,
        valor: Double,
        desconto: Double
    ) {
        val porgressiva = ProgressivaLista(
         ProtudoCodigo,
         protudoseleciona.caixapadrao,
         protudoseleciona.PMC,
         protudoseleciona.valor.toDouble(),
         valor,
         quatidade,
         desconto,
        true,
        false,
         protudoseleciona.descontoMaximo
        )

        if (desconto > protudoseleciona.descontoMaximo ){
            val alertas = Alertas()
            alertas.alerta(supportFragmentManager,"O desconto irá passar por aprovação","#B89A00",
                R.drawable.atencao, R.drawable.bordas_amerala_alert)
        }

        Toast.makeText(this,"Progressiva Adiconada com sucesso!", Toast.LENGTH_SHORT).show()
        if(!listaProgressiva.contains(porgressiva)){
            listaProgressiva.add(porgressiva)
        }

       progressivaAdpter.listaProgrssiva = listaProgressiva
       progressivaAdpter.notifyDataSetChanged()

    }
    fun exibirImagemBase64(imagemBase64: String): Bitmap {
        val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return  bitmap

    }
    override fun AtualizaValorProduto(
        quantidade: Int,
        valorProtudo: Double,
        caixapardrao: Boolean, soma:Boolean
    ) {
        if (soma){
            val valorProtudo = produtoDetalhePresenter.somaProdutos(quantidade,valorProtudo, repasse)
            binding.valorTotal.setText(valorProtudo)
        }else{
            val valorTotal =  FormataValores.converterMoedaParaDouble(binding.valorTotal.text.toString())
            val quantidade = edtQuantidade.text.toString()
            val valorProtudo =  produtoDetalhePresenter.subtrairPrutudos(valorTotal!!,valorProtudo,caixapardrao,quantidade.toInt(),repasse)
            binding.valorTotal.setText(valorProtudo)
        }
    }

    override fun AtuliazaQuantidaProduto(quantidadeProduto: Int) {
        edtQuantidade.setText(quantidadeProduto.toString())
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

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
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaQuantidadeProduto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaValorProduto
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.ProgressivaAdpter
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogProgressiva

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.*
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActProtudoDetalheBinding

class ActProtudoDetalhe : AppCompatActivity(), AtualizaProgressiva, AtualizaValorProduto , AtualizaQuantidadeProduto{

   lateinit var listaProgressiva :MutableList<ProgressivaLista>
   private  lateinit var  binding: ActivityActProtudoDetalheBinding
   lateinit var  progressivaAdpter: ProgressivaAdpter
   lateinit var login: Login
   var listaProgressivaOriginal :MutableList<ProgressivaLista>  = mutableListOf<ProgressivaLista>()

    val atualizaValorProduto: AtualizaValorProduto = this
    val  atualizaQuantidadeProduto :AtualizaQuantidadeProduto = this
    lateinit  var lojaSelecionada:Lojas
    lateinit var clienteSelecionado:Clientes
    val  lojasxclienets = mutableListOf<LojaXCliente>()
    var repasse = 0.0

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
        val estaNoCarrinho  = intent.getIntExtra("estaNoCarrinho",0)
        val estaNoPedidoSalvo = intent.getBooleanExtra("estaNoPedido",false)
        val pedidodID = intent.getIntExtra("pedidoID",0)
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



        // Atualiza as informações do front
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
               // Lógica para lidar com a mudança de estado do CheckBox
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

       // busca progresivas
        CoroutineScope(Dispatchers.IO).launch {
             // recupera progressiva do protudo Selecionado
            val ProgresivaDAO = ProgresivaDAO(baseContext)
            var  lista_progressivapresona = mutableListOf<ProgressivaLista>()
            val job1 = launch {
                  val query = "SELECT DISTINCT Produtos.Produto_codigo, Produtos.caixapadrao, Progressiva.pmc, Progressiva.pf, Progressiva.valor, Progressiva.Quantidade, Progressiva.desconto,Progressiva.DescontoMaximo\n" +
                          "FROM TB_produtos Produtos\n" +
                          "INNER JOIN TB_Progressiva Progressiva ON Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                          "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra\n" +
                          "WHERE Progressiva.loja_id = ${lojaSelecionada.loja_id}\n" +
                          "  AND Progressiva.uf = '${clienteSelecionado.UF}'\n" +
                          "  AND Produtos.Produto_codigo = ${protudoSelecionado.ProdutoCodigo}\n" +
                          "ORDER BY 6"
                  listaProgressiva = ProgresivaDAO.listarProgressiba(query,false)
                  listaProgressivaOriginal = ProgresivaDAO.listarProgressiba(query,false)
            }

            val  job2 = launch {
                val queryPersona = "SELECT * FROM Tb_Progressiva_Personalizada WHERE Tb_Progressiva_Personalizada.column_produto_codigo = ${protudoSelecionado.ProdutoCodigo} "
                lista_progressivapresona = ProgresivaDAO.listarProgressiba(queryPersona,true)
            }
            job1.join()
            job2.join()
            if (estaNoCarrinho == 1){
                edtQuantidade.setText(protudoSelecionado.quantidadeCarrinho.toString())
            }else{
                binding.edtQuantidade.setText(  listaProgressiva[0].quantidade.toString())

            }

            listaProgressiva.addAll(lista_progressivapresona)
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
                         Toast.makeText(baseContext,"A Quantidade minima é de ${      listaProgressiva[0].quantidade}",Toast.LENGTH_SHORT).show()
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
            val quantidadecaixapradrao = binding.caixaPadrao.text.toString()
            if (binding.checkCaixapadrao.isChecked){

                var quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()
                quatidadeAdicionadaCap += quantidadecaixapradrao.toInt() - ((quatidadeAdicionadaCap %   quantidadecaixapradrao.toInt() ))
                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quatidadeAdicionadaCap.toString())
                progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap
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
                       val quantidadecaixapradrao = binding.caixaPadrao.text.toString()
                       val resto = quatidadeAdicionadaCap % quantidadecaixapradrao.toInt()
                       if ( resto == 0) {
                           quatidadeAdicionadaCap -= quantidadecaixapradrao.toInt()
                       } else {
                           quatidadeAdicionadaCap -= resto
                       }
                        if ( quatidadeAdicionadaCap == 0){
                          quatidadeAdicionadaCap = listaProgressiva[0].quantidade
                        }
                       binding.edtQuantidade.setText(quatidadeAdicionadaCap.toString())
                       progressivaAdpter.soma = false
                       progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap
                       progressivaAdpter.clicou= false
                       progressivaAdpter.notifyDataSetChanged()
                   }
               }else{
                   if(quatidadeAdicionadaCap == 0){
                       binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable("0")
                   }else{
                       val valorTotal = binding.valorTotal.text.toString().replace("R$","").replace(" ","").replace(",",".")
                       var valorProduto =  binding.PF.text.toString()
                       valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")

                       val valorTotalDouble = valorTotal.toDouble()
                       val valorProdutoDouble = valorProduto.toDouble()
                       subtrairPrutudos(valorTotalDouble,valorProdutoDouble,false,0)


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
            var valorProduto =  binding.PF.text.toString()
            valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
            dialogProgressiva.dialog(this,valorProduto.toDouble(),protudoSelecionado.nome,protudoSelecionado,this)
        }


        binding.adiconarCarrinho.setOnClickListener {
           val quatidadeadd =  binding.edtQuantidade.text.toString()
           if(quatidadeadd.isEmpty() ){
               Toast.makeText(baseContext,"Por favor adicione ao menos uma quantidade para adicionar ao carrinho",Toast.LENGTH_SHORT).show()
           }else if (quatidadeadd.toInt() == 0 ){
               Toast.makeText(baseContext,"Por favor adicione ao menos uma quantidade para adicionar ao carrinho",Toast.LENGTH_SHORT).show()

           } else{
               var valortotal = binding.valorTotal.text.toString().replace(",",".").replace(" ","").replace("R$","")
               var repasse  = binding.repasse.text.toString().replace("%","").replace(",",".").toDouble()
               var valorProduto =  binding.PF.text.toString().replace("R$","").replace(" ","").replace(",",".").toDouble()
               val  daataformat = DataAtual()
               val  data = daataformat.recuperaData()
               var anr =  0
               if (binding.switANR.isChecked){
                   anr = 1
               }else{
                   anr = 0
               }
               var descontoOriginal = 0.0
               for (i in listaProgressivaOriginal){
                   if (progressivaSelecionada.quantidadeSelecionada == i.quantidade){
                       descontoOriginal = i.desconto
                       break
                   }

               }


               val  carrinho = Carrinho(lojaSelecionada.loja_id,
                   clienteSelecionado.Empresa_id,
                   protudoSelecionado.ProdutoCodigo,"",
                   login.Usuario_id.toString().toInt(),clienteSelecionado.UF,
                   0.0,0.0,0,
                   protudoSelecionado.barra,quatidadeadd.toInt(),protudoSelecionado.valor.toDouble(),
                   progressivaSelecionada.valorProgressivaSelecionada,protudoSelecionado.valor.toString().toDouble(),0,
                   progressivaSelecionada.porcentagemProgressivaSelecionda,
                   descontoOriginal,0.0,
                   "",1,"",valortotal.toDouble(),
                   protudoSelecionado.nome,lojaSelecionada.nome,clienteSelecionado.RazaoSocial,clienteSelecionado.CNPJ,data,
                   lojaSelecionada.MinimoValor,imagemBase64,protudoSelecionado.PMC,
                   clienteSelecionado.FormaPagamentoExclusiva,protudoSelecionado.caixapadrao,lojaSelecionada.Qtd_Minima_Operador,
                   lojaSelecionada.Qtd_Maxima_Operador,anr, lojaSelecionada.RegraPrazoMedio,lojaSelecionada.LojaTipo,protudoSelecionado.centro,protudoSelecionado.porcentagem)


               val carrinhoDAO = CarrinhoDAO(this)
               try {
                   if (pedido){
                       if (estaNoPedidoSalvo){
                           val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(baseContext)
                           pedidosFinalizadosDAO.atualizaProdutoPedidoValores(pedidodID,carrinho.produtoCodigo,carrinho)
                           Toast.makeText(baseContext,"Item atualizado com sucesso nos pedidos",Toast.LENGTH_SHORT).show()

                       }else{
                           val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(baseContext)
                           pedidosFinalizadosDAO.adicionaItemNoPedido(pedidodID,carrinho)
                           Toast.makeText(baseContext,"Item adicionado com sucesso nos pedidos",Toast.LENGTH_SHORT).show()
                       }
                   }else {
                       if (estaNoCarrinho  == 1){
                           carrinhoDAO.atualizaItemCarrinho(carrinho)
                           Toast.makeText(baseContext,"Item atualizado com sucesso ao carrinho",Toast.LENGTH_SHORT).show()

                       }else{
                           carrinhoDAO.insertCarrinho(carrinho)
                           Toast.makeText(baseContext,"Item adicionado com sucesso ao carrinho",Toast.LENGTH_SHORT).show()
                       }
                   }




               }catch (e:Exception){
                   e.printStackTrace()
                   val dialogErro = DialogErro()
                   dialogErro.Dialog(this,"Ops!", "Algo deu errado","Ok",""){

                   }
               }

               val intent = Intent()
               setResult(Activity.RESULT_OK, intent)
               finish()
           }

        }


       // botao voltar
       binding.voltarProtudo.setOnClickListener {
           this.onBackPressed()
       }

    }

    fun somaProdutos(quantidade:Int,valorProtudo:Double,caixapardrao:Boolean){
            var valorProdutoItem = valorProtudo
            if(repasse > 0.0){
                var discount = valorProdutoItem - (valorProdutoItem * (repasse) / 100);
                valorProdutoItem = discount;

            }else{
                valorProdutoItem = valorProtudo
            }
          val valorAdicionado = quantidade * valorProdutoItem
          val valorFormatado = String.format("%.2f", valorAdicionado)
          binding.valorTotal.text = "R$ "+ valorFormatado

    }
    fun subtrairPrutudos(valortotal:Double,valorProtudo:Double,caixapadrao:Boolean,quantidade: Int){
        var valorProdutoItem = valorProtudo
        if(repasse > 0.0){
            var discount = valorProdutoItem - (valorProdutoItem * (repasse) / 100);
            valorProdutoItem = discount;

        }else{
            valorProdutoItem = valorProtudo
        }
        if(caixapadrao){
            val valortotalCal  =  quantidade *valorProdutoItem
            val valorFormatado = String.format("%.2f", valortotalCal)
            binding.valorTotal.text = "R$ "+ valorFormatado

        }else{
            val valorAdicionadosub = valortotal - valorProdutoItem
            val valorFormatado = String.format("%.2f", valorAdicionadosub)
            binding.valorTotal.text = "R$ "+ valorFormatado

        }

    }

    override fun ProgressivaAtualiza(
        ProtudoCodigo: Int,
        protudoseleciona: ProdutoProgressiva,
        quatidade: Int,
        valor: Double,
        desconto: Double
    ) {
        val porgressiva = ProgressivaLista( ProtudoCodigo,
         protudoseleciona.caixapadrao,
         protudoseleciona.PMC,
         protudoseleciona.valor.toDouble(),
         valor,
         quatidade,
         desconto,
         true,false,0.0)

        if (desconto > 10.00 ){ // trocar esse 10.00 por um valor correto
            val alertas = Alertas()
            alertas.alerta(supportFragmentManager,"O desconto irá passar por aprovação","#B89A00",
                R.drawable.atencao,R.drawable.bordas_amerala_alert)
        }

        Toast.makeText(this,"Progressiva Adiconada com sucesso!", Toast.LENGTH_SHORT).show()
        if(!listaProgressiva.contains(porgressiva)){
            listaProgressiva.add(porgressiva)
        }

       progressivaAdpter.listaProgrssiva = listaProgressiva
       progressivaAdpter.notifyDataSetChanged()

    }
    fun exibirImagemBase64(imagemBase64: String): Bitmap {
        // Decodificar a string Base64 em um array de bytes
        val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)

        // Converter o array de bytes em um objeto Bitmap
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return  bitmap

    }

    override fun AtualizaValorProduto(
        quantidade: Int,
        valorProtudo: Double,
        caixapardrao: Boolean, soma:Boolean
    ) {
        if (soma){
            somaProdutos(quantidade,valorProtudo, caixapardrao)
        }else{
            val valorTotal = binding.valorTotal.text.toString().replace("R$","").replace(" ","").replace(",",".")

            subtrairPrutudos(valorTotal.toDouble(),valorProtudo,caixapardrao,quantidade)
        }

    }

    override fun AtuliazaQuantidaProduto(quantidadeProduto: Int) {
        edtQuantidade.setText(quantidadeProduto.toString())
    }
}
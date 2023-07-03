package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.webkit.URLUtil.decode
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.ProgressivaAdpter
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.DialogProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizaProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.*
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActProtudoDetalheBinding
import java.lang.Byte.decode
import java.util.*

class ActProtudoDetalhe : AppCompatActivity(),AtualizaProgressiva {

   lateinit var listaProgressiva :MutableList<ProgressivaLista>
   private  lateinit var  binding: ActivityActProtudoDetalheBinding
   lateinit var  progressivaAdpter:ProgressivaAdpter
   lateinit var login:Login
    companion object {
        lateinit var progressivaSelecionada:ProgressivaSelecionada
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
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)

        // Atualiza as informações do front
        binding.PMC.text = "R$ " + protudoSelecionado.PMC.toString()
        binding.PF.text = "R$ " + protudoSelecionado.valor
        binding.caixaPadrao.text = protudoSelecionado.caixapadrao.toString()
        binding.estoque.text = protudoSelecionado.quatidade.toString()
        binding.nomeProtudo.text = protudoSelecionado.nome
        binding.barra.text=  protudoSelecionado.barra
        binding.codigoProduto.text = protudoSelecionado.ProdutoCodigo.toString()
        if(!imagemBase64?.isEmpty()!!){
            val bitmap =exibirImagemBase64(imagemBase64.toString())
            binding.imgprodto.setImageBitmap(bitmap)
        }

        // busca progresivas
        CoroutineScope(Dispatchers.IO).launch {
             // recupera progressiva do protudo Selecionado
            val ProgresivaDAO = ProgresivaDAO(baseContext)
            var  lista_progressivapresona = mutableListOf<ProgressivaLista>()
            val job1 = launch {
                  val query = "SELECT Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Progressiva.pf,Progressiva.valor, Progressiva.Quantidade,Progressiva.desconto " +
                          "FROM TB_produtos Produtos " +
                          "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod " +
                          "INNER JOIN TB_Estoque Estoque ON Estoque.Barra = Produtos.barra and ESTOQUE.Loja_id = Progressiva.loja_id " +
                          "where Progressiva.loja_id = ${lojaSelecionada.loja_id} " +
                          "and Progressiva.uf = '${clienteSelecionado.UF}' " +
                          "and Produtos.Produto_codigo = ${protudoSelecionado.ProdutoCodigo} " +
                          "order by 6"
                  listaProgressiva = ProgresivaDAO.listarProgressiba(query,false)
            }

            val  job2 = launch {
                val queryPersona = "SELECT * FROM Tb_Progressiva_Personalizada WHERE Tb_Progressiva_Personalizada.column_produto_codigo = ${protudoSelecionado.ProdutoCodigo} "
                lista_progressivapresona = ProgresivaDAO.listarProgressiba(queryPersona,true)
            }
            job1.join()
            job2.join()

            listaProgressiva.addAll(lista_progressivapresona)

            progressivaAdpter = ProgressivaAdpter(listaProgressiva,baseContext,binding.recyProgressiva)
            val layoutManager = LinearLayoutManager(baseContext)

            CoroutineScope(Dispatchers.Main).launch {
                 binding.recyProgressiva.layoutManager = layoutManager
                 binding.recyProgressiva.adapter = progressivaAdpter
             }

         }

        // calcula valores
        binding.btnMais.setOnClickListener {
            val quantidadecaixapradrao = binding.caixaPadrao.text.toString()
            if (binding.checkCaixapadrao.isChecked){

                var quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()
                quatidadeAdicionadaCap += quantidadecaixapradrao.toInt() - ((quatidadeAdicionadaCap %   quantidadecaixapradrao.toInt() ))

                var valorProduto =  binding.PF.text.toString()
                valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
                val valorPrecoCovertido:Double =     valorProduto.toDouble()
                somaProdutos(quatidadeAdicionadaCap,valorPrecoCovertido,true)
                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quatidadeAdicionadaCap.toString())
                progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap -1
                progressivaAdpter.clicou= false

                progressivaAdpter.notifyDataSetChanged()

            }else{

                val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString()
                val quantidade:Int = quatidadeAdicionadaCap.toInt() +1
                var valorProduto =  binding.PF.text.toString()
                valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
                val valorPrecoCovertido:Double =     valorProduto.toDouble()
                somaProdutos(quantidade,valorPrecoCovertido,true)
                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quantidade.toString())
                progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap.toInt()
                progressivaAdpter.clicou= false
                progressivaAdpter.notifyDataSetChanged()

            }
        }
       // logica de botao menos
        binding.btnmenos.setOnClickListener {
            var quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()

            if (binding.checkCaixapadrao.isChecked){
                if(quatidadeAdicionadaCap == 0 ){
                    binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable("0")
                }else{
                    val quantidadecaixapradrao = binding.caixaPadrao.text.toString()
                    val valorTotal = binding.valorTotal.text.toString().replace("R$","").replace(" ","").replace(",",".")
                    var valorProduto =  binding.PF.text.toString()
                    valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")

                    val valorTotalDouble = valorTotal.toDouble()
                    val valorProdutoDouble = valorProduto.toDouble()
                    val resto = quatidadeAdicionadaCap % quantidadecaixapradrao.toInt()
                    if ( resto == 0) {
                        quatidadeAdicionadaCap -= quantidadecaixapradrao.toInt()
                    } else {
                        quatidadeAdicionadaCap -= resto
                    }
                    subtrairPrutudos(valorTotalDouble,valorProdutoDouble,true,quatidadeAdicionadaCap)
                    binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(
                        quatidadeAdicionadaCap.toString()
                    )

                    progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap -2
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
                    progressivaAdpter.quantidadeAdionada = quatidadeAdicionadaCap  -2
                    progressivaAdpter.clicou= false
                    progressivaAdpter.notifyDataSetChanged()
                }
            }
        }


        //Para adicionar nova Progressiva
        binding.adicionarProgressiva.setOnClickListener {
            val dialogProgressiva = DialogProgressiva()
            var valorProduto =  binding.PF.text.toString()
            valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
            dialogProgressiva.dialog(this,valorProduto.toDouble(),protudoSelecionado.nome,protudoSelecionado,this)
        }

       // Para Adiconar ao carrinho
        binding.adiconarCarrinho.setOnClickListener {
           val quatidadeadd =  binding.edtQuantidade.text.toString()
           if(quatidadeadd.isEmpty() || quatidadeadd.toInt() == 0 ){
               Toast.makeText(baseContext,"Por favor adicione ao menos uma quantidade para adicionar ao carrinho",Toast.LENGTH_SHORT).show()
           } else{
               val valortotal = binding.valorTotal.text.toString().replace(",",".").replace(" ","").replace("R$","")

               val  daataformat = DataAtual()
               val  data = daataformat.recuperaData()

               val  carrinho = Carrinho(lojaSelecionada.loja_id,
                   clienteSelecionado.Empresa_id,
                   protudoSelecionado.ProdutoCodigo,"",
                   login.Usuario_id.toString().toInt(),clienteSelecionado.UF,
                   0.0,0.0,0,
                   protudoSelecionado.barra,quatidadeadd.toInt(),protudoSelecionado.valor.toDouble(),
                   progressivaSelecionada.valorProgressivaSelecionada,protudoSelecionado.valor.toString().toDouble(),0,
                   progressivaSelecionada.porcentagemProgressivaSelecionda,
                   0.0
                   /*Lembra de colocar a faixa desconto original aqui*/,0.0,
                   "",1,"",valortotal.toDouble(),
                   protudoSelecionado.nome,lojaSelecionada.nome,clienteSelecionado.RazaoSocial,clienteSelecionado.CNPJ,data,lojaSelecionada.MinimoValor,imagemBase64,protudoSelecionado.PMC,protudoSelecionado.caixapadrao)

               val carrinhoDAO = CarrinhoDAO(this)
               carrinhoDAO.insertCarrinho(carrinho)
               val intent = Intent()
               setResult(Activity.RESULT_OK, intent)
               finish()
               Toast.makeText(baseContext,"Item adicionado com sucesso ao carrinho",Toast.LENGTH_SHORT).show()
               this.onBackPressed()
           }

        }


       // botao voltar
       binding.voltarProtudo.setOnClickListener {
           this.onBackPressed()
       }

    }

    fun somaProdutos(quantidade:Int,valorProtudo:Double,caixapardrao:Boolean){
      if(caixapardrao){

          val valorAdicionado = quantidade * valorProtudo
          val valorFormatado = String.format("%.2f", valorAdicionado)
          binding.valorTotal.text = "R$ "+ valorFormatado

      }else{

          val valorAdicionado = quantidade * valorProtudo
          val valorFormatado = String.format("%.2f", valorAdicionado)
          binding.valorTotal.text = "R$ "+ valorFormatado

      }

    }
    fun subtrairPrutudos(valortotal:Double,valorProtudo:Double,caixapadrao:Boolean,quantidade: Int){
        if(caixapadrao){

            val valortotalCal  =  quantidade *valorProtudo
            val valorFormatado = String.format("%.2f", valortotalCal)
            binding.valorTotal.text = "R$ "+ valorFormatado

        }else{

            val valorAdicionadosub = valortotal - valorProtudo
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
         true)

        if (desconto > 10.00){ // trocar esse 10.00 por um valor correto
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
}
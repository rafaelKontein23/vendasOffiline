package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.ProgressivaAdpter
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.DialogProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizaProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActProtudoDetalheBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding

class ActProtudoDetalhe : AppCompatActivity(),AtualizaProgressiva {


    private  lateinit var  binding: ActivityActProtudoDetalheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActProtudoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // busca itens necessarios para montar a tela
        val bundle = intent.getBundleExtra("ProtudoSelecionado_bundle")
        val protudoSelecionado = bundle?.getSerializable("ProtudoSelecionado") as ProdutoProgressiva
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
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

        // busca progresivas
        CoroutineScope(Dispatchers.IO).launch {
             // recupera progressiva do protudo Selecionado
             val ProgresivaDAO = ProgresivaDAO(baseContext)
             val query = "SELECT Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Progressiva.pf,Progressiva.valor, Progressiva.Quantidade,Progressiva.desconto " +
                     "FROM TB_produtos Produtos " +
                     "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod " +
                     "INNER JOIN TB_Estoque Estoque ON Estoque.Barra = Produtos.barra and ESTOQUE.Loja_id = Progressiva.loja_id " +
                     "where Progressiva.loja_id = ${lojaSelecionada.loja_id} " +
                     "and Progressiva.uf = '${clienteSelecionado.UF}' " +
                     "and Produtos.Produto_codigo = ${protudoSelecionado.ProdutoCodigo} " +
                     "order by 6"
             val listaProgressiva = ProgresivaDAO.listarProgressiba(query)

             val ProgressivaAdpter = ProgressivaAdpter(listaProgressiva)
             val layoutManager = LinearLayoutManager(baseContext)

             CoroutineScope(Dispatchers.Main).launch {
                 binding.recyProgressiva.layoutManager = layoutManager
                 binding.recyProgressiva.adapter = ProgressivaAdpter
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

            }else{

                val quatidadeAdicionadaCap = binding.edtQuantidade.text.toString()
                val quantidade:Int = quatidadeAdicionadaCap.toInt() +1
                var valorProduto =  binding.PF.text.toString()
                valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
                val valorPrecoCovertido:Double =     valorProduto.toDouble()
                somaProdutos(quantidade,valorPrecoCovertido,true)
                binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quantidade.toString())

            }
        }

        binding.btnmenos.setOnClickListener {
            var quatidadeAdicionadaCap = binding.edtQuantidade.text.toString().toInt()

            if (binding.checkCaixapadrao.isChecked){
                if(quatidadeAdicionadaCap.toInt() == 0 ){
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
                    subtrairPrutudos(valorTotalDouble,valorProdutoDouble,true,quantidadecaixapradrao.toInt())
                    binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(
                        quatidadeAdicionadaCap.toString()
                    )
                }
            }else{
                if(quatidadeAdicionadaCap.toInt() == 0){
                    binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable("0")
                }else{
                    val valorTotal = binding.valorTotal.text.toString().replace("R$","").replace(" ","").replace(",",".")
                    var valorProduto =  binding.PF.text.toString()
                    valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")

                    val valorTotalDouble = valorTotal.toDouble()
                    val valorProdutoDouble = valorProduto.toDouble()
                    subtrairPrutudos(valorTotalDouble,valorProdutoDouble,false,0)


                    val quantidade:Int = quatidadeAdicionadaCap.toInt() -1

                    binding.edtQuantidade.text = Editable.Factory.getInstance().newEditable(quantidade.toString())
                }
            }
        }


        //nova Prgressiva
        binding.adicionarProgressiva.setOnClickListener {
            val dialogProgressiva = DialogProgressiva()
            var valorProduto =  binding.PF.text.toString()
            valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
            dialogProgressiva.dialog(this,valorProduto.toDouble(),protudoSelecionado.nome,protudoSelecionado)
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

    override fun ProgressivaAtualiza() {

    }
}
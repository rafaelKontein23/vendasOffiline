package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.CarrinhoDetalheAdpter
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogOperadorLogistico

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActCarrinhoDetalheBinding

class ActCarrinhoDetalhe:  AppCompatActivity(),
    AtualizadetalhesProdutos,
    StartaAtividade {
    private  lateinit var  binding: ActivityActCarrinhoDetalheBinding
    lateinit var adpterCarrinhoDetalhes: CarrinhoDetalheAdpter
    lateinit var   listaProdutoCarrinho :MutableList<Carrinho>
    var valorminimo = 0.0
    var valorTotal = 0.0
    var  carrinhoDetalhe = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPostCreate(savedInstanceState: Bundle?) {
        binding  = ActivityActCarrinhoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onPostCreate(savedInstanceState)
        carrinhoDetalhe = intent.getBooleanExtra("CarrinhoDetalhe",false)

        binding.continuarCarrinho.setOnClickListener {
            if(valorTotal < valorminimo){
                val dialogErro  = DialogErro()
                dialogErro.Dialog(this,"Atenção", "Pedido mínimo não Atingido!","Ok",""){

                }
            }else {
                val dialogOperadorLogistico = DialogOperadorLogistico(this)
                dialogOperadorLogistico.dialog(this,listaProdutoCarrinho,valorTotal)
            }

        }

        binding.voltarCarrinho.setOnClickListener{
             finish()
        }

        atualizarInterface()
    }

    override fun detalhes(
        lista: MutableList<Carrinho>,
        atualiza: Boolean,
        position: Int,
        valor: Double
    ) {

            adpterCarrinhoDetalhes.listaProdutoCarrinho = lista
            adpterCarrinhoDetalhes.notifyDataSetChanged()
           atualzza(lista,valorminimo)
            if (lista.isEmpty()){
               onBackPressed()
           }

    }
    fun atualzza(lista: MutableList<Carrinho>, valorMinimo: Double){

        var valorDesconto = 0.0
        var quantidade = 0
        valorTotal = 0.0
        for (i in 0 until  lista.size){
            valorTotal += lista[i].valortotal.toString().toDouble()
        }
        for(k in 0 until  lista.size){
            valorDesconto += lista[k].desconto.toString().toDouble()
        }
        for(j in 0 until  lista.size){
            quantidade += lista[j].quantidade.toString().toInt()
        }
        val valorTot = String.format("%.2f",valorTotal)
        val valorTotDesc = String.format("%.2f",valorDesconto / lista.size)
        binding.valorTotalitens.text = "R$ " + valorTot
        binding.desconto.text =  valorTotDesc +"%"
        binding.unidadesTotasi.text = quantidade.toString() + " Uni."
        binding.progressBarValorminimo.max = valorMinimo.toInt()
        binding.progressBarValorminimo.progress = valorTotal.toInt()

        val resultado = (valorminimo * 40) / 100

        if(valorTotal<=resultado ){

            val greenColor = Color.parseColor("#B40101")
            val colorStateList = ColorStateList.valueOf(greenColor)

            binding.progressBarValorminimo.progressTintList = colorStateList
        }else if(valorTotal > resultado && valorTotal <valorMinimo){
            val greenColor = Color.parseColor("#CF7001")
            val colorStateList = ColorStateList.valueOf(greenColor)

            binding.progressBarValorminimo.progressTintList = colorStateList
        }else{
            val greenColor = Color.parseColor("#01B45E")
            val colorStateList = ColorStateList.valueOf(greenColor)

            binding.progressBarValorminimo.progressTintList = colorStateList
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            atualizarInterface()
        }
    }

    override fun atividade(intent: Intent) {
        startActivityForResult(intent,3)
    }

    fun atualizarInterface(){

        val  carrinhoDAO = CarrinhoDAO(this)
        listaProdutoCarrinho = carrinhoDAO.listaritensCarrinho(ActPricipal.loja_id,ActPricipal.cliente_id)

        val linearLayoutManager = LinearLayoutManager(this)
        adpterCarrinhoDetalhes =  CarrinhoDetalheAdpter(listaProdutoCarrinho,binding.root,this,this,this,carrinhoDetalhe)
        binding.recyprotudo.layoutManager = linearLayoutManager
        binding.recyprotudo.adapter = adpterCarrinhoDetalhes
        val  valorformatMinimo = String.format("%.2f",ActPricipal.lojavalorMinimo)
        binding.ValorMinimoCarrinho.text = "R$ "  + valorformatMinimo
        valorminimo = ActPricipal.lojavalorMinimo

        atualzza(listaProdutoCarrinho,ActPricipal.lojavalorMinimo)
    }
}
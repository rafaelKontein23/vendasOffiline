package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_protudos.*
import kotlinx.android.synthetic.main.fragment_protudos.view.*
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.CarrinhoDetalheAdpter
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.DialogOperadorLogistico
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActCarrinhoDetalheBinding

class Act_CarrinhoDetalhe:  AppCompatActivity(), AtualizadetalhesProdutos , StartaAtividade{
    private  lateinit var  binding: ActivityActCarrinhoDetalheBinding
    lateinit var adpterCarrinhoDetalhes:CarrinhoDetalheAdpter
    lateinit var   listaProdutoCarrinho :MutableList<Carrinho>
    var valorminimo = 0.0
    override fun onPostCreate(savedInstanceState: Bundle?) {
        binding  = ActivityActCarrinhoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onPostCreate(savedInstanceState)


        val  carrinhoDAO = CarrinhoDAO(this)
        listaProdutoCarrinho = carrinhoDAO.listaritensCarrinho(Act_Pricipal.loja_id,Act_Pricipal.cliente_id)

        val linearLayoutManager = LinearLayoutManager(this)
        adpterCarrinhoDetalhes =  CarrinhoDetalheAdpter(listaProdutoCarrinho,binding.root,this,this,this)
        binding.recyprotudo.layoutManager = linearLayoutManager
        binding.recyprotudo.adapter = adpterCarrinhoDetalhes
        val  valorformatMinimo = String.format("%.2f",Act_Pricipal.lojavalorMinimo)
        binding.ValorMinimoCarrinho.text = "R$ "  + valorformatMinimo

        binding.continuarCarrinho.setOnClickListener {
            val dialogOperadorLogistico = DialogOperadorLogistico()
            dialogOperadorLogistico.dialog(this,listaProdutoCarrinho)
        }
        valorminimo = Act_Pricipal.lojavalorMinimo
        atualzza(listaProdutoCarrinho,Act_Pricipal.lojavalorMinimo)


    }

    override fun detalhes(
        lista: MutableList<Carrinho>,
        atualiza: Boolean,
        position: Int,
        valor: Double
    ) {
        if(!atualiza){
            lista[position].valortotal = valor
            atualzza(lista,valorminimo)
        }else{
            adpterCarrinhoDetalhes.notifyDataSetChanged()

        }

    }
    fun atualzza(lista: MutableList<Carrinho>,valorMinimo: Double){
        var valorTotal = 0.0
        var valorDesconto = 0.0
        var quantidade = 0
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
        val valorTotDesc = String.format("%.2f",valorDesconto)
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

    override fun atividade(intent: Intent) {
        startActivityForResult(intent,3)
    }
}
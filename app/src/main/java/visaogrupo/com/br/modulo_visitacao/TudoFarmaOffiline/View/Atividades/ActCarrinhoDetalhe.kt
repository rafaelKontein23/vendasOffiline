package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActCarrinhoDetalheBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.CarrinhoDetalhePresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.CarrinhoDetalheAdpter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogOperadorLogistico

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro

class ActCarrinhoDetalhe:  AppCompatActivity(),
    AtualizadetalhesProdutos,
    StartaAtividade {
    private  lateinit var  binding: ActivityActCarrinhoDetalheBinding
    lateinit var adpterCarrinhoDetalhes: CarrinhoDetalheAdpter
    lateinit var   listaProdutoCarrinho :MutableList<Carrinho>
    var valorminimo = 0.0
    var valorTotal = 0.0
    var  carrinhoDetalhe = false

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
            setResult(Activity.RESULT_OK)
            finish()
        }

        atualizarInterface()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun detalhes(
        lista: MutableList<Carrinho>,
        atualiza: Boolean,
        position: Int,
        valor: Double
    ) {
       adpterCarrinhoDetalhes.listaProdutoCarrinho = lista
       adpterCarrinhoDetalhes.notifyDataSetChanged()
        Atualizanfos(lista,valorminimo)
        if (lista.isEmpty()){
           onBackPressed()
       }

    }
    fun Atualizanfos(lista: MutableList<Carrinho>, valorMinimo: Double){
        valorTotal = 0.0

        val carrinhoDetalhePresenter = CarrinhoDetalhePresenter()
        val  infoCarrinhoDetalhe = carrinhoDetalhePresenter.atualizainformacoesCarrinhoDetalhe(valorTotal,lista, valorMinimo)
        valorTotal = infoCarrinhoDetalhe.valorTotal

        binding.valorTotalitens.text =  FormataValores.formatarParaMoeda(valorTotal)
        binding.desconto.text = infoCarrinhoDetalhe.valorTotdescontoMedio.toString() +"%"
        binding.unidadesTotasi.text = infoCarrinhoDetalhe.quatidade.toString() + " Uni."

        binding.progressBarValorminimo.max = valorMinimo.toInt()
        binding.progressBarValorminimo.progress = infoCarrinhoDetalhe.valorTotal.toInt()
        binding.progressBarValorminimo.progressTintList = infoCarrinhoDetalhe.colorProgrres

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
        valorminimo = listaProdutoCarrinho[0].valorMinimoLoja
        binding.ValorMinimoCarrinho.text = FormataValores.formatarParaMoeda(valorminimo)

        Atualizanfos(listaProdutoCarrinho,ActPricipal.lojavalorMinimo)
    }
}
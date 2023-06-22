package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.CarrinhoDetalheAdpter
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActCarrinhoDetalheBinding

class Act_CarrinhoDetalhe:  AppCompatActivity(), AtualizadetalhesProdutos {
    private  lateinit var  binding: ActivityActCarrinhoDetalheBinding
    lateinit var adpterCarrinhoDetalhes:CarrinhoDetalheAdpter
    override fun onPostCreate(savedInstanceState: Bundle?) {
        binding  = ActivityActCarrinhoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onPostCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val  lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)

        val  carrinhoDAO = CarrinhoDAO(this)
        val   listaProdutoCarrinho = carrinhoDAO.listaritensCarrinho(lojaSelecionada.loja_id,clienteSelecionado.Empresa_id)

        val linearLayoutManager = LinearLayoutManager(this)
        adpterCarrinhoDetalhes =  CarrinhoDetalheAdpter(listaProdutoCarrinho,binding.root,this,this)
        binding.recyprotudo.layoutManager = linearLayoutManager
        binding.recyprotudo.adapter = adpterCarrinhoDetalhes
    }

    override fun detalhes() {
        adpterCarrinhoDetalhes.notifyDataSetChanged()
    }
}
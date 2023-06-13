package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActProtudoDetalheBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding

class ActProtudoDetalhe : AppCompatActivity() {


    private  lateinit var  binding: ActivityActProtudoDetalheBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActProtudoDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("ProtudoSelecionado_bundle")
        val protudoSelecionado = bundle?.getSerializable("ProtudoSelecionado") as ProdutoProgressiva
        binding.PMC.text = "R$ " + protudoSelecionado.PMC.toString()
        binding.PF.text = "R$ " + protudoSelecionado.valor
        binding.caixaPadrao.text = protudoSelecionado.caixapadrao.toString()
        binding.estoque.text = protudoSelecionado.quatidade.toString()
        binding.nomeProtudo.text = protudoSelecionado.nome
        binding.barra.text=  protudoSelecionado.barra
        binding.codigoProduto.text = protudoSelecionado.ProdutoCodigo.toString()


    }
}
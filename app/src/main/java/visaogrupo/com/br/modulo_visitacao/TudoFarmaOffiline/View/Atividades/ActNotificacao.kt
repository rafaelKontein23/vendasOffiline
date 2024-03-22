package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActNotificacaoBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.NotificationPresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.IniciaAticidadePrincipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterNotificacao

class ActNotificacao : AppCompatActivity(), IniciaAticidadePrincipal{
    lateinit var binding: ActivityActNotificacaoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActNotificacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.voltarTela.setOnClickListener {
             finish()
         }
        val notificacoesPresenter = NotificationPresenter()
        val listaNotificacao =  notificacoesPresenter.buscarNotificacoes(baseContext)

        binding.recyNotificaocao.isVisible = !listaNotificacao.isEmpty()
        binding.semNotificaoes.isVisible = listaNotificacao.isEmpty()

        val linearLayout = LinearLayoutManager(baseContext)
        val adapterNotificacoes = AdapterNotificacao(listaNotificacao, baseContext, this)

        binding.recyNotificaocao.hasFixedSize()
        binding.recyNotificaocao.adapter = adapterNotificacoes
        binding.recyNotificaocao.layoutManager = linearLayout

        binding.lerTodos.setOnClickListener {
            val  listaNotificacaoVisulizada = notificacoesPresenter.marcarTodosComoLido(listaNotificacao,baseContext)
            adapterNotificacoes.listaNotificacao = listaNotificacaoVisulizada
            adapterNotificacoes.notifyDataSetChanged()

         }
        binding.todos.setOnClickListener {
            adapterNotificacoes.listaNotificacao = listaNotificacao
            adapterNotificacoes.notifyDataSetChanged()
            binding.recyNotificaocao.isVisible = !listaNotificacao.isEmpty()
            binding.semNotificaoes.isVisible = listaNotificacao.isEmpty()
            selecionaItens(binding.todos)
            deslecionaItens(binding.visualizado)
            deslecionaItens(binding.NaoVisualizado)


        }
        binding.visualizado.setOnClickListener {
            val  listaNotificacaoVisulizada = notificacoesPresenter.itensVisualizados(listaNotificacao)
            binding.recyNotificaocao.isVisible = !listaNotificacaoVisulizada.isEmpty()
            binding.semNotificaoes.isVisible = listaNotificacaoVisulizada.isEmpty()
            adapterNotificacoes.listaNotificacao = listaNotificacaoVisulizada
            adapterNotificacoes.notifyDataSetChanged()
            selecionaItens(binding.visualizado)
            deslecionaItens(binding.todos)
            deslecionaItens(binding.NaoVisualizado)

        }

        binding.NaoVisualizado.setOnClickListener {
            val  listaNotificacaoVisulizada = notificacoesPresenter.itensNaoVisualizados(listaNotificacao)
            binding.recyNotificaocao.isVisible = !listaNotificacaoVisulizada.isEmpty()
            binding.semNotificaoes.isVisible = listaNotificacaoVisulizada.isEmpty()
            adapterNotificacoes.listaNotificacao = listaNotificacaoVisulizada
            adapterNotificacoes.notifyDataSetChanged()
            selecionaItens(binding.NaoVisualizado)
            deslecionaItens(binding.todos)
            deslecionaItens(binding.visualizado)
        }
    }

    override fun iniciaAtividade() {
        val  intent = Intent(baseContext, ActPricipal::class.java)
        intent.putExtra("Tela","pedido")
        startActivity(intent)
    }

    fun deslecionaItens(textView: TextView){
        textView.setTextColor(Color.parseColor("#8AAAC6"))
        textView.setBackgroundResource(R.drawable.bordas_filtro_azul_deselecionada)
    }
    fun selecionaItens(textView: TextView){
        textView.setTextColor(Color.parseColor("#FF004682"))
        textView.setBackgroundResource(R.drawable.bordas_filtro_azul_selecionada)
    }
}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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

    }

    override fun iniciaAtividade() {
        val  intent = Intent(baseContext, ActPricipal::class.java)
        intent.putExtra("Tela","pedido")
        startActivity(intent)
    }
}
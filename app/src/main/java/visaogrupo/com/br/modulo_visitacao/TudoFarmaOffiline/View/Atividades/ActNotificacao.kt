package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.NotificationPresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterNotificacao

class ActNotificacao : AppCompatActivity() {
    lateinit var binding:ActivityActNotificacaoBinding
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
        val adapterNotificacoes = AdapterNotificacao(listaNotificacao)

        binding.recyNotificaocao.hasFixedSize()
        binding.recyNotificaocao.adapter = adapterNotificacoes
        binding.recyNotificaocao.layoutManager = linearLayout

        binding.todos.setOnClickListener {

         }

    }
}
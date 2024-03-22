package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Notificacoes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.NotificacaoDAO

class NotificationPresenter {

    fun buscarNotificacoes(context: Context):MutableList<Notificacoes> {
        val notificacaoDAO = NotificacaoDAO(context)
        val listaNotificacoes = notificacaoDAO.listar()
        return listaNotificacoes
    }
    fun marcarTodosComoLido(listaNotificao:MutableList<Notificacoes>,context: Context):MutableList<Notificacoes>{
        val notificacaoDAO = NotificacaoDAO(context)
        for (i in  listaNotificao){
            notificacaoDAO.atualizar(i.notificacaoID)
            i.visualizado = 1
        }

        return listaNotificao
    }

    fun itensVisualizados(listaNotificao:MutableList<Notificacoes>):MutableList<Notificacoes>{
        val listaNotificaoVisualizado = mutableListOf <Notificacoes>()
        for (i in  listaNotificao){
            if (i.visualizado == 0){
                listaNotificaoVisualizado.add(i)
            }

        }

        return listaNotificaoVisualizado
    }


    fun itensNaoVisualizados(listaNotificao:MutableList<Notificacoes>):MutableList<Notificacoes>{
        val listaNotificaoNaoVisualizado = mutableListOf <Notificacoes>()
        for (i in  listaNotificao){
            if (i.visualizado == 1){
                listaNotificaoNaoVisualizado.add(i)
            }

        }

        return listaNotificaoNaoVisualizado
    }
}
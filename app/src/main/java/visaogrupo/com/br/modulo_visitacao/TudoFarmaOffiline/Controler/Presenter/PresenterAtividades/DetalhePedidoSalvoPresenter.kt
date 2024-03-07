package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CustomSpinerFormDePag
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ObjetosPresenters.InfosDetalhesPedidoSalvo
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.OperadorLogistico
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.OperadorLogisticaDAO

class DetalhePedidoSalvoPresenter {

    fun buscaInfosDetalhePedidoSalvo(pedido: PedidoFinalizado,context:Context): InfosDetalhesPedidoSalvo {

       val  listaFormPag = mutableListOf<CustomSpinerFormDePag>()
       val  listaOplspner :ArrayList<OperadorLogistico> = ArrayList()

       CoroutineScope(Dispatchers.IO).launch {
           val buscaOpl = launch {
               val  operadorLogisticaDAO = OperadorLogisticaDAO(context)
               val listaOpl = operadorLogisticaDAO.listar(pedido!!.uf,pedido!!.lojaId)

               for (i in 0 until listaOpl.size){
                   listaOplspner.add(listaOpl[i])
               }

           }

           val buscaFormaDePag = launch {
               val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
               val listaFormaDePagemento = formaDePagamentoDAO.listar(pedido!!.lojaId ,pedido!!.valorTotal!!.toDouble(), pedido!!.RegraPrazo, pedido!!.cnpj!!)
               listaFormPag.add(0, CustomSpinerFormDePag("Selecionar",0))
               for (i in 0 until listaFormaDePagemento.size){
                   listaFormPag.add(CustomSpinerFormDePag(listaFormaDePagemento[i].FormaPgto,listaFormaDePagemento[i].exlusiva))
               }
           }
           buscaOpl.join()
           buscaFormaDePag.join()
       }
       val infosDetalhesPedidoSlavo = InfosDetalhesPedidoSalvo(listaFormPag,listaOplspner)


       return infosDetalhesPedidoSlavo
    }
    fun infoFormaDePagamentoSalvo(context: Context, valorToatalPedido: Double, RegraPrazo:Int, cnpj:String, formaDepagaemntoSelecionada:String, pedido: PedidoFinalizado ): FormaDePagaemnto? {
        var formaDePagaemnto: FormaDePagaemnto? = null

        val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
        val listaFormaDePagemento = formaDePagamentoDAO.listar(pedido!!.lojaId, valorToatalPedido, RegraPrazo, cnpj)

        for (i in listaFormaDePagemento){
            if (formaDepagaemntoSelecionada.contains(i.FormaPgto)){
                formaDePagaemnto = i
                break
            }
        }

        return formaDePagaemnto
    }

}
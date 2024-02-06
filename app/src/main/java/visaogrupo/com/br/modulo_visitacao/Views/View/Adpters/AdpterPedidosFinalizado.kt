package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.IniciaPedidoDetalhes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.MostraLoad
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.VaiParaEnviados
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.FormataTexto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.taskEnviaPedido
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActDetalhePedidoSalvo
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPedidoEnviado
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import java.io.Serializable

class AdpterPedidosFinalizado (list:MutableList<PedidoFinalizado>, context : Context,
                               atualizaPedido: AtualizaPedido,
                               mostra:MostraLoad,
                               mostrarEnvio:Boolean,
                               vaiParaEnviados:VaiParaEnviados,
                               envioPedido:Int,
                               iniciaAtividade:IniciaPedidoDetalhes) : RecyclerView.Adapter<AdpterPedidosFinalizado.ViewHolderPedidoFinalizado>() {
    var listaPedido = list
    val context = context
    val atualizaPedido = atualizaPedido
    val mostra = mostra
    val mostrarEnvio = mostrarEnvio
    val vaiParaEnviados = vaiParaEnviados
    val envioPedido = envioPedido;
    val  iniciaPedidoDetalhes = iniciaAtividade

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPedidoFinalizado {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.pedido_enviar,parent,false)
        return ViewHolderPedidoFinalizado(
            view
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolderPedidoFinalizado, position: Int) {

        val valorTot = String.format("%.2f",listaPedido[position].valorTotal)
        val formaPag = FormaDePagamentoDAO(context)
        val codForm = listaPedido[position].formaDePagamento
        var nomeFormaPag = ""
        if (!listaPedido[position].formaDePagamento!!.isEmpty()){
             nomeFormaPag = formaPag.buscarNomeFormaPag(codForm!!)

        }


        val cnpj = FormataTexto.formataCnpj(listaPedido[position].cnpj!!)

        val PedidoFinalizado = listaPedido[position]

        holder.valorTotal.text = "R$ " + valorTot
        holder.cnpjcliente.text = cnpj
        holder.razaoSocial.text  = listaPedido[position].razaoSocial

        holder.data.text  = listaPedido[position].dataPedido+ " | ${nomeFormaPag}"

        holder.nomeloja.text = listaPedido[position].nomeLoja

        holder.celula.setOnClickListener(object :OnClickListener{
            override fun onClick(v: View?) {
                if (envioPedido == 1){
                    val pedido = listaPedido[position]
                    val intent = Intent(context, ActPedidoEnviado::class.java)
                    intent.putExtra("PedidoClicado", pedido as Serializable)
                    context.startActivity(intent)
                }else {

                    val pedido = listaPedido[position]
                    val intent = Intent(context, ActDetalhePedidoSalvo::class.java)
                    intent.putExtra("PedidoClicado", pedido as Serializable)
                    iniciaPedidoDetalhes.inicia(intent)
                }

            }

        })
         holder.enviarPedido.isVisible =mostrarEnvio

        holder.excluirItem.setOnClickListener {
            val dialogErro = DialogErro()
            dialogErro.Dialog(context,"Atenção","essa ação excluira o Pedido, tem certeza que deseja continuar","Sim","Não", cancel = true){
                if (listaPedido[position].kit == 1){
                    val pedidosFinalizadosDAO = PedidosFinalizadosDAO(context)

                    pedidosFinalizadosDAO.excluirItemPedidoKit(listaPedido[position].pedidoID,envioPedido)
                    val listaProdutoPedido = pedidosFinalizadosDAO.listarPedidosProdutos(listaPedido[position].pedidoID)
                    for (i in listaProdutoPedido){
                        pedidosFinalizadosDAO.excluirItemPedidoProdutoKit(listaPedido[position].pedidoID,i.produtoCodigo!!.toInt())
                    }
                }else{
                    val pedidosFinalizadosDAO = PedidosFinalizadosDAO(context)

                    pedidosFinalizadosDAO.excluirItemPedido(listaPedido[position].pedidoID,envioPedido)
                    val listaProdutoPedido = pedidosFinalizadosDAO.listarPedidosProdutos(listaPedido[position].pedidoID)
                    for (i in listaProdutoPedido){
                        pedidosFinalizadosDAO.excluirItemPedidoProduto(listaPedido[position].pedidoID,i.produtoCodigo!!.toInt())
                    }

                }
                Toast.makeText(context,"Item Excluido com sucesso!",Toast.LENGTH_SHORT).show()
                listaPedido.removeAt(position)
                notifyDataSetChanged()

            }

        }
         holder.enviarPedido.setOnClickListener{
             val  verificaInternet = Verifica_Internet()
             val isInternet = verificaInternet.isOnline(context)
             if(isInternet){

                     mostra.mostraLoad(true,"Enviando Pedido...")
                     val taskEnviaPedido = taskEnviaPedido()
                     CoroutineScope(Dispatchers.IO).launch {
                         val (valido, mensagem )=  taskEnviaPedido.eviarPedido(PedidoFinalizado,context)
                         if (valido == 1){

                              val pedidoDAO = PedidosFinalizadosDAO(context)
                             pedidoDAO.uptadePedidoEnviado(1,PedidoFinalizado.pedidoID.toInt())
                             atualizaPedido.atualizaPedidos()
                             CoroutineScope(Dispatchers.Main).launch {
                                 mostra.mostraLoad(false,"")
                                 val  dialogErro = DialogErro()
                                 dialogErro.Dialog(context,"Sucesso!",mensagem,"Ok",""){


                                     atualizaPedido.atualizaPedidos()
                                     vaiParaEnviados.vaiparaEnviados()



                                 }
                             }

                         }else{
                             CoroutineScope(Dispatchers.Main).launch {
                                 mostra.mostraLoad(false,"")

                                 val  dialogErro = DialogErro()
                                 dialogErro.Dialog(context,"Ops!",mensagem,"Ok",""){

                                 }
                             }

                         }
                     }

         }else {
             val dialogErro = DialogErro()
             dialogErro.Dialog(context,"Sem conexão","Tente novamente mais tarde,","Ok",""){

             }
         }
         }
    }

    override fun getItemCount(): Int {
        return listaPedido.size
    }

    class  ViewHolderPedidoFinalizado(itemView: View) : RecyclerView.ViewHolder(itemView){
        val data = itemView.findViewById<TextView>(R.id.dataFeito)
        val cnpjcliente = itemView.findViewById<TextView>(R.id.cnpjcliente)
        val razaoSocial = itemView.findViewById<TextView>(R.id.razaoSocial)
        val valorTotal = itemView.findViewById<TextView>(R.id.valorTotal)
        val unidades = itemView.findViewById<TextView>(R.id.unidades)
        val celula = itemView.findViewById<ConstraintLayout>(R.id.iempedido)
        val  enviarPedido = itemView.findViewById<ImageView>(R.id.enviarPedido)
        val  excluirItem = itemView.findViewById<ImageView>(R.id.excluirItem)
        val nomeloja = itemView.findViewById<TextView>(R.id.nomeloja)

    }
}
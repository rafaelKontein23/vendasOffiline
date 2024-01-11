package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Pedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.taskEnviaPedido
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActCarrinhoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPedidoEnviado
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import java.io.Serializable

class AdpterPedidosFinalizado (list:MutableList<PedidoFinalizado>, context : Context,atualizaPedido: AtualizaPedido) : RecyclerView.Adapter<AdpterPedidosFinalizado.ViewHolderPedidoFinalizado>() {
    var listaPedido = list
    val context = context
    val atualizaPedido = atualizaPedido

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPedidoFinalizado {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.pedido_enviar,parent,false)
        return ViewHolderPedidoFinalizado(
            view
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolderPedidoFinalizado, position: Int) {
        val valorPedidoTotalDAO = PedidosFinalizadosDAO(context)
        val valorTotalPedido = valorPedidoTotalDAO.somarTotalPedido(listaPedido[position].pedidoID)
        val valorTot = String.format("%.2f",valorTotalPedido)

        val cnpj = listaPedido[position].cnpj?.substring(0,2)+"."+
                listaPedido[position].cnpj?.substring(2,5)+"."+
                listaPedido[position].cnpj?.substring(5,8)+"/"+
                listaPedido[position].cnpj?.substring(8,12) +"-"+
                listaPedido[position].cnpj?.substring(12,14)

        val PedidoFinalizado = listaPedido[position]

        holder.valorTotal.text = "R$ " + valorTot
        holder.cnpjcliente.text = cnpj
        holder.razaoSocial.text  = listaPedido[position].razaoSocial
        holder.data.text  = listaPedido[position].dataPedido

        holder.celula.setOnClickListener(object :OnClickListener{
            override fun onClick(v: View?) {
                val pedido = listaPedido[position]
                val intent = Intent(context, ActPedidoEnviado::class.java)
                intent.putExtra("PedidoClicado", pedido as Serializable)
                context.startActivity(intent)
            }

        })
         holder.enviarPedido.setOnClickListener{
             val  verificaInternet = Verifica_Internet()
             val isInternet = verificaInternet.isOnline(context)
             if(isInternet){


                     val taskEnviaPedido = taskEnviaPedido()
                     CoroutineScope(Dispatchers.IO).launch {
                         val (valido, mensagem )=  taskEnviaPedido.eviarPedido(PedidoFinalizado,context)
                         if (valido == 1){
                              val pedidoDAO = PedidosFinalizadosDAO(context)
                             pedidoDAO.uptadePedidoEnviado(1,PedidoFinalizado.pedidoID.toInt())
                             atualizaPedido.atualizaPedidos()
                             CoroutineScope(Dispatchers.Main).launch {
                                 val  dialogErro = DialogErro()
                                 dialogErro.Dialog(context,"Sucesso!",mensagem,"Ok",""){

                                 }
                             }

                         }else{
                             CoroutineScope(Dispatchers.Main).launch {
                                 val  dialogErro = DialogErro()
                                 dialogErro.Dialog(context,"Ops!",mensagem,"Ok","",{})
                             }

                         }
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
        val img = itemView.findViewById<ImageView>(R.id.excluirItem)
        val celula = itemView.findViewById<ConstraintLayout>(R.id.iempedido)
        val  enviarPedido = itemView.findViewById<ImageView>(R.id.enviarPedido)

    }
}
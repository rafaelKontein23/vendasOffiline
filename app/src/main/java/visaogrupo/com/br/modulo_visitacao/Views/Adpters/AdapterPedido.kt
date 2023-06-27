package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Act_CarrinhoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Act_Pricipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Pedido
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.LojasDAO

class AdapterPedido (list:MutableList<Pedido>,context :Context) : RecyclerView.Adapter<AdapterPedido.ViewHolderPedido>() {
    val listaPedido = list
   val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPedido {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.celula_pedido,parent,false)
        return ViewHolderPedido(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPedido, position: Int) {
        val valorTot = String.format("%.2f",listaPedido[position].valortotal)
        val cnpj = listaPedido[position].cnpj.substring(0,2)+"."+
                listaPedido[position].cnpj.substring(2,5)+"."+
                listaPedido[position].cnpj.substring(5,8)+"/"+
                listaPedido[position].cnpj.substring(8,12) +"-"+
                listaPedido[position].cnpj.substring(12,14)

        holder.valorTotal.text = valorTot
        holder.cnpjcliente.text = cnpj
        holder.razaoSocial.text  = listaPedido[position].razaosocial
        holder.unidades.text=  listaPedido[position].qtd_Total.toString() + "Uni."
        holder.data.text  = listaPedido[position].data

        holder.celula.setOnClickListener {

                Log.d("Buscou:","vai abrir detalhes")
                Act_Pricipal.lojavalorMinimo = listaPedido[position].lojavalorMinomo
                Act_Pricipal.clienteUF = listaPedido[position].ufCliente
                Act_Pricipal.cliente_id = listaPedido[position].cliente_id
                Act_Pricipal.loja_id =listaPedido[position].loja_id
                context.startActivity(Intent(context,Act_CarrinhoDetalhe::class.java))
        }
    }

    override fun getItemCount(): Int {
        return listaPedido.size
    }

    class  ViewHolderPedido(itemView: View) :ViewHolder(itemView){
        val data = itemView.findViewById<TextView>(R.id.dataFeito)
        val cnpjcliente = itemView.findViewById<TextView>(R.id.cnpjcliente)
        val razaoSocial = itemView.findViewById<TextView>(R.id.razaoSocial)
        val valorTotal = itemView.findViewById<TextView>(R.id.valorTotal)
        val unidades = itemView.findViewById<TextView>(R.id.unidades)
        val img = itemView.findViewById<ImageView>(R.id.excluirItem)
        val celula = itemView.findViewById<ConstraintLayout>(R.id.iempedido)

    }
}
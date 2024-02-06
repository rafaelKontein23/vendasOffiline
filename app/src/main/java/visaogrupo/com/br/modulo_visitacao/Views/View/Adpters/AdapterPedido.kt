package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActCarrinhoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Pedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.SalvarLojaeClientePrefereferciaUser
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.LojasDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro

class AdapterPedido (list:MutableList<Pedido>, context :Context, atualizaCarinho:AtualizaCarrinho,activity: Activity, resultcode:Int) : RecyclerView.Adapter<AdapterPedido.ViewHolderPedido>() {
    var listaPedido = list
    val context = context
    val atualizaCarinho = atualizaCarinho
    val activity = activity
    val resultcode = resultcode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPedido {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.celula_pedido,parent,false)
        return ViewHolderPedido(
            view
        )
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

        holder.excluirItem.setOnClickListener {
             val dialogErro = DialogErro()
            dialogErro.Dialog(context,"Atenção","essa ação excluira o Pedido em aberto, tem certeza que deseja continuar","Sim","Não", cancel = true){
                val carrinhoDAO = CarrinhoDAO(context)
                carrinhoDAO.excluirItemCarrinho(listaPedido[position].cliente_id,listaPedido[position].loja_id)
                Toast.makeText(context,"Item Excluito com suceeso!",Toast.LENGTH_SHORT).show()
                listaPedido.removeAt(position)

                notifyDataSetChanged()
                if (listaPedido.isEmpty()){
                    atualizaCarinho.atualizaCarrinho()
                }

            }


        }
        holder.celula.setOnClickListener {
                SalvarLojaeClientePrefereferciaUser.salvarLoja(listaPedido[position].cliente_id,listaPedido[position].loja_id,context)
                SalvarLojaeClientePrefereferciaUser.salvaCliente(context,listaPedido[position].cliente_id)

                Log.d("Buscou:","vai abrir detalhes")
                ActPricipal.lojavalorMinimo = listaPedido[position].lojavalorMinomo
                ActPricipal.clienteUF = listaPedido[position].ufCliente
                ActPricipal.cliente_id = listaPedido[position].cliente_id
                ActPricipal.loja_id =listaPedido[position].loja_id
                val intent =Intent(context, ActCarrinhoDetalhe::class.java)
                intent.putExtra("CarrinhoDetalhe",true)
                activity.startActivityForResult(intent,resultcode)
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


        val excluirItem = itemView.findViewById<ImageView>(R.id.excluirItem)

    }
}
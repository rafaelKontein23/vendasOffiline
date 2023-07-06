package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogDetalhesClientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes

class ClientesAdpter (list: MutableList<Clientes>, val  idfram:Int, val supoortfragment:FragmentManager, trocarcoritem: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TrocarcorItem, carrinhoVisible: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible, atualizaCarrinho: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho): RecyclerView.Adapter<ClientesAdpter.ViewClientesHolders>() {
    var listaClientes = list
    val  trocarcoritem = trocarcoritem
    val carrinhoVisible = carrinhoVisible
    val atualizaCarrinho = atualizaCarrinho
    var count = 5
    var carregando = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewClientesHolders {
        val celulaClientes = LayoutInflater.from(parent.context).inflate(R.layout.celulaclientes,parent,false)
        return ViewClientesHolders(celulaClientes)
    }

    override fun onBindViewHolder(holder: ViewClientesHolders, position: Int) {
        if(!carregando){
            val cnpjstr = listaClientes[position].CNPJ
            val CNPJ =   cnpjstr.substring(0,2)+"."+cnpjstr.substring(2,5)+"."+cnpjstr.substring(5,8)+"/"+cnpjstr.substring(8,12) +"-"+ cnpjstr.substring(12,14);
            holder.cnpjcliente.text = CNPJ
            holder.razaosocialClientes.text = listaClientes[position].RazaoSocial
            holder.enderecocliente.text = listaClientes[position].Endereco


            holder.comtarincelula.setOnClickListener {

                val dialogDetalhesClientes = DialogDetalhesClientes()
                val sharedPreferences = holder.comtarincelula.context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val gson = Gson()
                val objetoSerializado = gson.toJson(listaClientes[position])
                val editor = sharedPreferences.edit()
                editor.putString("ClienteSelecionado", objetoSerializado)
                editor.apply()


                dialogDetalhesClientes.dialogDetalhe(holder.cnpjcliente.context,CNPJ,listaClientes[position], idfram,supoortfragment,trocarcoritem, carrinhoVisible, atualizaCarrinho  )
            }
        }

    }

    override fun getItemCount(): Int {

           return listaClientes.size


    }
    fun addItems(newItems: List<Clientes>) {
        listaClientes.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewClientesHolders(itemView: View) : ViewHolder(itemView){
        val cnpjcliente = itemView.findViewById<TextView>(R.id.celulaClienteCNPJ)
        val razaosocialClientes = itemView.findViewById<TextView>(R.id.textRazaosocialcliente)
        val enderecocliente = itemView.findViewById<TextView>(R.id.enderecoCliente)
        val  comtarincelula = itemView.findViewById<ConstraintLayout>(R.id.comtarincelula)
        val  linha = itemView.findViewById<View>(R.id.linhacliente)
    }

}
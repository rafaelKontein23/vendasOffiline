package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataTexto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogMarcaVisitas

class AdaterRoteiro(list:MutableList<Clientes>, context: Context): Adapter<AdaterRoteiro.ViewHolderRoteiro>() {

    var listaCliente = list
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRoteiro {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_roteiro,parent,false)
       return ViewHolderRoteiro(view)
    }

    override fun onBindViewHolder(holder: ViewHolderRoteiro, position: Int) {
         val itemCliente = listaCliente[position]
        holder.enderecoCliente.text = itemCliente.Endereco
        holder.celulaClienteCNPJ.text = FormataTexto.formataCnpj(itemCliente.CNPJ)
        holder.textRazaosocialcliente.text = itemCliente.RazaoSocial

        holder.containerRoteiro.setOnClickListener {
            val dialogMarcaVisitas = DialogMarcaVisitas(context)
            dialogMarcaVisitas.dialogDetalhe(context,FormataTexto.formataCnpj(itemCliente.CNPJ),itemCliente)
        }
    }

    override fun getItemCount(): Int {
       return listaCliente.size
    }
    class ViewHolderRoteiro(itemView: View) : ViewHolder(itemView){
        val celulaClienteCNPJ = itemView.findViewById<TextView>(R.id.celulaClienteCNPJ)
        val textRazaosocialcliente= itemView.findViewById<TextView>(R.id.textRazaosocialcliente)
        val enderecoCliente = itemView.findViewById<TextView>(R.id.enderecoCliente)
        val quantidadeVisita = itemView.findViewById<TextView>(R.id.quantidadeVisita)
        val containerRoteiro = itemView.findViewById<ConstraintLayout>(R.id.containerRoteiro)
    }
}
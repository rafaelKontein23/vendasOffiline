package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Notificacoes

class AdapterNotificacao(list: MutableList<Notificacoes>): Adapter<AdapterNotificacao.ViewHolderNotificacao>() {
    val listaNotificacao = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotificacao {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_notificacao,parent,false)
        return ViewHolderNotificacao(view)
    }
    override fun onBindViewHolder(holder: ViewHolderNotificacao, position: Int) {
        val itemNotificacao =  listaNotificacao [position]
        holder.tituloNotificao.text = itemNotificacao.titulo
        holder.mensagem.text= itemNotificacao.mensagem

    }
    override fun getItemCount(): Int {
        return listaNotificacao.size
    }

    class ViewHolderNotificacao(itemView: View) : ViewHolder(itemView){
          val tituloNotificao = itemView.findViewById<TextView>(R.id.tituloNotificao)
          val mensagem =       itemView.findViewById<TextView>(R.id.mensagem)
          val tempo =          itemView.findViewById<TextView>(R.id.tempo)
          val visualiza = itemView.findViewById<View>(R.id.visualiza)

    }

}
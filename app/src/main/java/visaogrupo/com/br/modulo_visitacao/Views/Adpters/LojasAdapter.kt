package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

class LojasAdapter (list :List<Lojas>) : Adapter<LojasAdapter.LojasViewHolder>() {
    var listaLojas = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LojasViewHolder {
     val  view = LayoutInflater.from(parent.context).inflate(R.layout.celula_loja,parent,false)
     return LojasViewHolder(view)
    }

    override fun onBindViewHolder(holder: LojasViewHolder, position: Int) {
        holder.nomeloja.text = listaLojas[position].nome.toString()
        holder.valorMinimo.text = listaLojas[position].MinimoValor.toString()
    }

    override fun getItemCount(): Int {
       return listaLojas.size
    }

    class LojasViewHolder(itemView: View) : ViewHolder(itemView){
        val containerLoja = itemView.findViewById<ConstraintLayout>(R.id.containerloja)
        val nomeloja = itemView.findViewById<TextView>(R.id.nomeloja)
        val valorMinimo = itemView.findViewById<TextView>(R.id.valorminimo)
    }


}
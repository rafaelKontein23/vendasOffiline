package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaLista

class ProgressivaAdpter (list :List<ProgressivaLista>): Adapter<ProgressivaAdpter.ProgressivaViewholder>() {
     val listaProgrssiva = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressivaViewholder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_progressiva,parent,false)

        return ProgressivaViewholder(view)
    }

    override fun onBindViewHolder(holder: ProgressivaViewholder, position: Int) {

        holder.desconto.text = listaProgrssiva[position].desconto.toString() +"%"
        holder.quatidade.text = "00" +listaProgrssiva[position].quantidade.toString() +" Desc."
        holder.valorProgressiva.text = "R$ "  + listaProgrssiva[position].valor.toString()
        holder.checkbox.setOnClickListener {
            if (holder.checkbox.isChecked) {
                holder.checkbox.isChecked = true
                trocabackgroun(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )
            } else {
                holder.checkbox.isChecked = false
                trocabackgrounpadrao(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )
            }
        }

        holder.container.setOnClickListener {
            if(holder.checkbox.isChecked){
                holder.checkbox.isChecked = false
                trocabackgrounpadrao(holder.quatidade, holder.desconto,holder.valorProgressiva, holder.container)
            }else{
                holder.checkbox.isChecked = true
                trocabackgroun(holder.quatidade, holder.desconto,holder.valorProgressiva, holder.container)
            }
        }

    }

    override fun getItemCount(): Int {
       return listaProgrssiva.size
    }

    class ProgressivaViewholder(itemView: View) : ViewHolder(itemView){
        val quatidade = itemView.findViewById<TextView>(R.id.qtdprgressiva)
        val desconto = itemView.findViewById<TextView>(R.id.porCento)
        val valorProgressiva  = itemView.findViewById<TextView>(R.id.valorProgressiva)
        val container = itemView.findViewById<ConstraintLayout>(R.id.containerProgressiva)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkBoxprogressiva)
    }
    fun trocabackgroun(quantidade:TextView,desconto:TextView,valor :TextView,containeter:ConstraintLayout){
        quantidade.setTextColor(Color.parseColor("#00325C"))
        desconto.setTextColor(Color.parseColor("#00325C"))
        valor.setTextColor(Color.parseColor("#00325C"))
        containeter.background = ContextCompat.getDrawable(containeter.context, R.drawable.bordas_check_seleciona)

    }

    fun trocabackgrounpadrao(quantidade:TextView,desconto:TextView,valor :TextView,containeter:ConstraintLayout){
        quantidade.setTextColor(Color.parseColor("#585E68"))
        desconto.setTextColor(Color.parseColor("#585E68"))
        valor.setTextColor(Color.parseColor("#585E68"))
        containeter.background = ContextCompat.getDrawable(containeter.context, R.color.corviewicon)

    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.celula_progressiva.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaLista
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaSelecionada
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ProgresivaDAO

class ProgressivaAdpter (list :MutableList<ProgressivaLista>,context: Context, recyclerview:RecyclerView): Adapter<ProgressivaAdpter.ProgressivaViewholder>() {

    var listaProgrssiva = list
    val personaFalse = 0
    val  personaTrue = 1
    val context = context
    var quantidadeAdionada = 0
    var pos = 0
    var positionQuatidade = 0
    var recyclerview = recyclerview
    var  clicou = false




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressivaViewholder {
        var view =  LayoutInflater.from(parent.context).inflate(R.layout.celula_progressiva,parent,false)
        if(viewType == personaFalse){
            view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_progressiva,parent,false)
        }else{
            view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_progressivapersona,parent,false)
        }
        return ProgressivaViewholder(view)
    }

    override fun onBindViewHolder(holder: ProgressivaViewholder, position: Int) {


        val descontoFormat = String.format("%.2f",listaProgrssiva[position].desconto)
        val  valorprogressivaformat = String.format("%.2f",listaProgrssiva[position].valor)
        holder.desconto.text =  descontoFormat +" %"
        holder.quatidade.text = listaProgrssiva[position].quantidade.toString() +" Desc."
        holder.valorProgressiva.text = "R$ "  + valorprogressivaformat



        holder.xProgressiva.setOnClickListener {
            val position1 = holder.adapterPosition
            val ProgressivaDAO =  ProgresivaDAO(context)
            ProgressivaDAO.deleteProgressiva("",listaProgrssiva[position1].desconto,listaProgrssiva[position1].quantidade)
            listaProgrssiva.removeAt(position1)
            notifyItemRemoved(position1)

        }

        Log.d("quantiddae",quantidadeAdionada.toString())

        holder.container.setOnClickListener {
            setSelectedItem(position)
        }
        // logica de quantidade Adiconada
        if(clicou){

            if (position == pos){

                holder.checkbox.isChecked = true
                val descontoProgreesivaSelecionada = listaProgrssiva[position].desconto
                val valorProgressivaSelecionada  = listaProgrssiva[position].valor
                val quantidadeProgressivaSelecionbada  = listaProgrssiva[position].quantidade
                val ProgressivaSelecionada  = ProgressivaSelecionada(quantidadeProgressivaSelecionbada,descontoProgreesivaSelecionada,valorProgressivaSelecionada)
                ActProtudoDetalhe.progressivaSelecionada = ProgressivaSelecionada
                trocabackgroun(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )

            }else{
                holder.checkbox.isChecked = false
                trocabackgrounpadrao(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )

            }
            // Logica de alternar click
        }else{
            pos  = 0
            if(quantidadeAdionada +1 == listaProgrssiva[position].quantidade && listaProgrssiva[position].personalizada){
                positionQuatidade = position

            }
            if ((position == positionQuatidade) && ( quantidadeAdionada +1 ==  listaProgrssiva[position].quantidade )){
                quantidadeAdionada = 0

                val descontoProgreesivaSelecionada = listaProgrssiva[position].desconto
                val valorProgressivaSelecionada  = listaProgrssiva[position].valor
                val quantidadeProgressivaSelecionbada  = listaProgrssiva[position].quantidade
                val ProgressivaSelecionada  = ProgressivaSelecionada(quantidadeProgressivaSelecionbada,descontoProgreesivaSelecionada,valorProgressivaSelecionada)
                ActProtudoDetalhe.progressivaSelecionada = ProgressivaSelecionada

                holder.checkbox.isChecked = true
                trocabackgroun(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )

            }else{
                holder.checkbox.isChecked = false
                trocabackgrounpadrao(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )
            }
        }



    }

    override fun getItemViewType(position: Int): Int {

        if(listaProgrssiva[position].personalizada == true){
            return personaTrue
        }else{
            return personaFalse
        }
    }

    override fun getItemCount(): Int {

        return listaProgrssiva.size
    }

    inner class ProgressivaViewholder(itemView: View) : ViewHolder(itemView){
        val quatidade = itemView.findViewById<TextView>(R.id.qtdprgressiva)
        val desconto = itemView.findViewById<TextView>(R.id.porCento)
        val valorProgressiva  = itemView.findViewById<TextView>(R.id.valorProgressiva)
        val container = itemView.findViewById<ConstraintLayout>(R.id.containerProgressiva)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkBoxprogressiva)
        val xProgressiva = itemView.findViewById<ImageView>(R.id.xProgressiva)
        init {

            checkbox.setOnClickListener {
                if (checkbox.isChecked) {
                    setSelectedItem(adapterPosition)
                }

            }
        }
    }
    fun setSelectedItem(adapterPosition: Int) {
        clicou = true
        if (adapterPosition == RecyclerView.NO_POSITION)  return
        if(positionQuatidade != 0){

            notifyItemChanged(positionQuatidade)
            positionQuatidade= 0

        }else{

            notifyItemChanged(pos)

        }

        pos = adapterPosition
        notifyItemChanged(adapterPosition)

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

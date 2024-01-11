package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaQuantidadeProduto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaValorProduto
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProgressivaLista
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProgressivaSelecionada
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.MascaraCampo
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentProtudos

class ProgressivaAdpter (list :MutableList<ProgressivaLista>, context: Context, recyclerview:RecyclerView,
                         fragmentmeneger:FragmentManager,atualizaValorProduto: AtualizaValorProduto,
                         edtQuatidade :EditText,atualizaQuantidadeProduto: AtualizaQuantidadeProduto ): Adapter<ProgressivaAdpter.ProgressivaViewholder>() {

    var listaProgrssiva = list
    val personaFalse = 0
    val  personaTrue = 1
    val context = context
    var quantidadeAdionada = listaProgrssiva[0].quantidade
    var pos = 0
    var positionQuatidade = 0
    var recyclerview = recyclerview
    var  clicou = false
    var fragmentmeneger = fragmentmeneger
    var soma = true
    var anr= false
    var atualizaQuantidadeProduto = atualizaQuantidadeProduto

    val atualizaValorProduto = atualizaValorProduto
    val edtQuatidade = edtQuatidade



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
        holder.desconto.setText( descontoFormat)
        holder.quatidade.text = listaProgrssiva[position].quantidade.toString() +" Desc."
        holder.valorProgressiva.setText("R$ "  + valorprogressivaformat)



        holder.desconto.setOnFocusChangeListener(object  : OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus){
                    val descontoCap =  holder.desconto.text.toString().replace(",",".").toDouble()
                    if (!anr){
                        if (descontoCap >=  listaProgrssiva [position].DescontoMaximo){
                            val alertas = Alertas()
                            alertas.alerta(fragmentmeneger,"Desconto Fora da politica !","#B89A00",
                                R.drawable.atencao,R.drawable.bordas_amerala_alert)
                            holder.desconto.setText(listaProgrssiva[position].desconto.toString())

                        }else {
                            editaDesc(position,descontoCap, holder )
                        }
                    }else{
                        editaDesc(position,descontoCap, holder )
                    }

                }
            }

        })

        holder.checkbox.setOnClickListener {
            quantidadeAdionada = listaProgrssiva [position].quantidade

            notifyDataSetChanged()
            atualizaQuantidadeProduto.AtuliazaQuantidaProduto(quantidadeAdionada)
        }

        holder.xProgressiva.setOnClickListener {
            val position1 = holder.adapterPosition
            val ProgressivaDAO =  ProgresivaDAO(context)
            ProgressivaDAO.deleteProgressiva("",listaProgrssiva[position1].desconto,listaProgrssiva[position1].quantidade)
            listaProgrssiva.removeAt(position1)
            notifyItemRemoved(position1)

        }

       MascaraCampo.mascaraEdit("NN,NN",holder.desconto)


        Log.d("quantiddae",quantidadeAdionada.toString())

        holder.container.setOnClickListener {
            setSelectedItem(position)
        }
        // logica de quantidade Adiconada

            pos  = 0
            if(quantidadeAdionada >= listaProgrssiva[position].quantidade ){
                positionQuatidade = position

            }

            for (progressiva in listaProgrssiva) {
                if(quantidadeAdionada >= progressiva.quantidade  ){

                    listaProgrssiva.forEach() { progressivaLista ->
                        progressivaLista.ProgressivaSelecionad = false
                    }
                    progressiva.ProgressivaSelecionad = true
                    val descontoProgreesivaSelecionada =progressiva.desconto
                    val valorProgressivaSelecionada  = progressiva.valor
                    val quantidadeProgressivaSelecionbada  =progressiva.quantidade
                    val ProgressivaSelecionada  = ProgressivaSelecionada(quantidadeProgressivaSelecionbada,descontoProgreesivaSelecionada,valorProgressivaSelecionada)
                    ActProtudoDetalhe.progressivaSelecionada = ProgressivaSelecionada
                    if (soma){
                        atualizaValorProduto.AtualizaValorProduto(quantidadeAdionada,ProgressivaSelecionada.valorProgressivaSelecionada,true, true)
                    }else {
                        atualizaValorProduto.AtualizaValorProduto(quantidadeAdionada,ProgressivaSelecionada.valorProgressivaSelecionada,true, false)

                    }
                }
            }




            holder.checkbox.isChecked = listaProgrssiva[position].ProgressivaSelecionad
            if (listaProgrssiva[position].ProgressivaSelecionad){
                trocabackgroun(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )
            }else{
                trocabackgrounpadrao(
                    holder.quatidade,
                    holder.desconto,
                    holder.valorProgressiva,
                    holder.container
                )
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
        val desconto = itemView.findViewById<EditText>(R.id.porCento)
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
    fun editaDesc(position:Int,descontoCap:Double, holder :ProgressivaViewholder ){
        listaProgrssiva[position].desconto = descontoCap
        val valorPf = listaProgrssiva[position].pf
        val valorTotDesc = valorPf  *(descontoCap/ 100)
        val valorFinal =  valorPf - valorTotDesc
        holder.valorProgressiva.setText(String.format("%.2f", valorFinal))
        listaProgrssiva[position].valor = valorFinal
        atualizaValorProduto.AtualizaValorProduto(quantidadeAdionada,valorFinal,true, false)
    }
}

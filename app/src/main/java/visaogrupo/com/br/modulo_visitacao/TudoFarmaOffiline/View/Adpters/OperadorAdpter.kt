package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.OperadorLogistico

class OperadorAdpter  (listaOpl :MutableList<OperadorLogistico>, quantidadeMaximaDePpl:Int, context:Context,opl:MutableList<String>): Adapter<OperadorAdpter.ViewHolderOpl>() {


    val listaOpl = listaOpl
    val quantidademaximaDeOpl = quantidadeMaximaDePpl
    var count  = 0
    val context = context
    var opl = opl
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOpl {
        val   view = LayoutInflater.from(parent.context).inflate(R.layout.celula_opl,parent,false)
        return ViewHolderOpl(view)

    }
    override fun onBindViewHolder(holder: ViewHolderOpl, position: Int) {
         holder.Textopl.setText(listaOpl[position].Nome)
        count = 0

        if (listaOpl[position].posicao != -1 && listaOpl[position].selecionado){
            val  counint = listaOpl[position].posicao +1

            holder.Textopl.setBackgroundResource(R.drawable.fundo_selecionado_opl)
            holder.quantidade.isVisible = true
            holder.quantidade.setText( counint.toString())
        }
         holder.Textopl.setOnClickListener( object :OnClickListener{
             override fun onClick(v: View?) {
                 if (!listaOpl[position].selecionado){

                     count = 0
                     for (i in listaOpl){
                         if (i.selecionado){
                             count ++
                         }
                     }
                     if (count >= quantidademaximaDeOpl){
                         Toast.makeText(context,"Essa loja Aceita até ${quantidademaximaDeOpl} operadores logisco",Toast.LENGTH_SHORT).show()
                         holder.Textopl.setBackgroundResource(R.drawable.fundo_desleciona_opl)
                         holder.quantidade.isVisible = false
                         listaOpl[position].selecionado = false
                     }else {
                         holder.Textopl.setBackgroundResource(R.drawable.fundo_selecionado_opl)
                         listaOpl[position].selecionado = true
                         holder.quantidade.isVisible = true
                         val  counint = count +1
                         listaOpl[position].posicao = counint
                         holder.quantidade.setText( counint.toString())
                         opl.add( listaOpl[position].OperadorLogistico_ID.toString())
                     }
                 }else {
                     count = 0
                     holder.Textopl.setBackgroundResource(R.drawable.fundo_desleciona_opl)
                     listaOpl[position].selecionado = false
                     holder.quantidade.isVisible = false
                     listaOpl[position].posicao = -1
                     for (i in listaOpl){
                         if (i.selecionado){
                             i.posicao =      count ++
                         }
                     }
                     if (opl.contains(listaOpl[position].OperadorLogistico_ID.toString())){
                         opl.remove(listaOpl[position].OperadorLogistico_ID.toString())
                     }
                     notifyDataSetChanged()

                 }
             }

         })


    }
    override fun getItemCount(): Int {
        return  listaOpl.size
    }


    class ViewHolderOpl(itemView: View) : ViewHolder(itemView){
        val Textopl = itemView.findViewById<TextView>(R.id.Textopl)
        val quantidade = itemView.findViewById<TextView>(R.id.quantidade)
    }


}
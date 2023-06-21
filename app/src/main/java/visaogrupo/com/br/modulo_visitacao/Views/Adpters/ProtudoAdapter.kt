package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import java.io.Serializable

class ProtudoAdapter(list:List<ProdutoProgressiva>,context: Context,start :StartaAtividade): Adapter<ProtudoAdapter.ProdutoViewHolder>() {
    var listaProtudos = list
    val  context = context
    val start =  start
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
       val view =  LayoutInflater.from(parent.context).inflate(R.layout.celula_produtos,parent,false)

        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.nomeProtudo.text = listaProtudos[position].nome
        holder.codigoProduto.text = listaProtudos[position].ProdutoCodigo.toString()
        holder.barra.text = listaProtudos[position].barra
        holder.valor.text = "R$ " + listaProtudos[position].valor.toString().replace(".",",")

        if (listaProtudos[position].estaNoCarrinho  == 1){
           holder.quantidade.isVisible = true
           holder.excluiritem.isVisible = true
           holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.verdenutoon)
           holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.corprodto)
           holder.quantidade.text = "x" + listaProtudos[position].quantidadeCarrinho.toString()
        }else{
            holder.quantidade.isVisible = false
            holder.excluiritem.isVisible = false
            holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.corlinhaorigin)
            holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.transparente)
        }
        holder.constrainProtudos.setOnClickListener {
         val intent = Intent(context,ActProtudoDetalhe::class.java)
         val bundle = Bundle()
         bundle.putSerializable("ProtudoSelecionado", listaProtudos[position] as Serializable)
         intent.putExtra("ProtudoSelecionado_bundle", bundle)
         start.atividade(intent)

        }
    }

    override fun getItemCount(): Int {
       return  listaProtudos.size
    }

    class ProdutoViewHolder(itemView: View) : ViewHolder(itemView){
        val constrainProtudos = itemView.findViewById<ConstraintLayout>(R.id.contanerProduto)
        val valor = itemView.findViewById<TextView>(R.id.PrudutoValor)
        val barra = itemView.findViewById<TextView>(R.id.barraProduto)
        val nomeProtudo = itemView.findViewById<TextView>(R.id.NomeProtudo)
        val codigoProduto = itemView.findViewById<TextView>(R.id.codigoProtudo)
        val imgProduto = itemView.findViewById<ImageView>(R.id.imageView3)
        val quantidade = itemView.findViewById<TextView>(R.id.quatidadeadicionada)
        val excluiritem = itemView.findViewById<TextView>(R.id.excluirItem)
        val linhaProtudos = itemView.findViewById<View>(R.id.linhaProtudos)

    }

}
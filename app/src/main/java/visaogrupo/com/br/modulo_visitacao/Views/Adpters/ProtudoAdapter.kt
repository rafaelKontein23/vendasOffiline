package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Produtos

class ProtudoAdapter (list :List<ProdutoProgressiva>): Adapter<ProtudoAdapter.ProdutoViewHolder>() {
    var listaProtudos = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
       val view =  LayoutInflater.from(parent.context).inflate(R.layout.celula_produtos,parent,false)

        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.nomeProtudo.text = listaProtudos[position].nome
        holder.codigoProduto.text = listaProtudos[position].ProdutoCodigo.toString()
        holder.barra.text = listaProtudos[position].barra
        holder.valor.text = "R$ " + listaProtudos[position].valor.toString().replace(".",",")
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

    }

}
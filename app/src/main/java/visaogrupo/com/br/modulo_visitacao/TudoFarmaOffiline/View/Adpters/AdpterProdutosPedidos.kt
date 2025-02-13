package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores

class AdpterProdutosPedidos  (list:MutableList<ProdutosFinalizados>, context : Context) : RecyclerView.Adapter<AdpterProdutosPedidos.ViewHolderPedidoFinalizadoProdutos>() {
    var listaPedidoProdutos = list
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPedidoFinalizadoProdutos {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.celula_produto_pedidos,parent,false)
        return ViewHolderPedidoFinalizadoProdutos(
            view
        )
    }

    override fun onBindViewHolder(holder: ViewHolderPedidoFinalizadoProdutos, position: Int) {
        val valorTot = FormataValores.formatarParaMoeda(listaPedidoProdutos[position].valor)


        holder.nomeProdtud.text = listaPedidoProdutos[position].nomeProduto
        holder.codProduto.text = listaPedidoProdutos[position].produtoCodigo.toString()
        holder.barra.text  = listaPedidoProdutos[position].barra
        holder.valoruni.text  = "R$ " + valorTot
        holder.unidades.text  = listaPedidoProdutos[position].quantidade.toString() + " uni."

    }

    override fun getItemCount(): Int {
        return listaPedidoProdutos.size
    }

    class  ViewHolderPedidoFinalizadoProdutos(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomeProdtud = itemView.findViewById<TextView>(R.id.nomeProdtud)
        val codProduto = itemView.findViewById<TextView>(R.id.codProduto)
        val barra = itemView.findViewById<TextView>(R.id.barra)
        val valoruni = itemView.findViewById<TextView>(R.id.valoruni)
        val unidades = itemView.findViewById<TextView>(R.id.unidade)
        val img = itemView.findViewById<ImageView>(R.id.excluirItem)

    }
}
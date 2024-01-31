package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoKitDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro

class AdapterCarrinhoKit(CarrinhoKit:MutableList<CarrinhoKit>) : Adapter<AdapterCarrinhoKit.ViewHolderCarrinhhoKit>() {
    val  listCarrinhoKit = CarrinhoKit
    var toast: Toast? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarrinhhoKit {
       val view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_carrinhokit_tiulo, parent,false)

       return ViewHolderCarrinhhoKit(view)
    }
    override fun onBindViewHolder(holder: ViewHolderCarrinhhoKit, position: Int) {
        val itemCarrinhoKit = listCarrinhoKit[position]
        val vlaorTotalFormat = String.format("%.2f",itemCarrinhoKit.valortotal)
        holder.pedidoText.text = "Pedido #"+ itemCarrinhoKit.numerPedido.toString()
        holder.nomeKit.text = itemCarrinhoKit.nomeKit
        holder.valorTotalKit.text = "R$" +vlaorTotalFormat
        holder.edtQuantidade.setText(itemCarrinhoKit.quantidade.toString())
        val linearLayoutManager = LinearLayoutManager(holder.continuarCarrinhoKit.context)
        val adpterProdutoKit = AdapterProdutoskit(itemCarrinhoKit.listProdutoKit!!)
        holder.recyclerViewProdutoKItCarrinho.adapter = adpterProdutoKit
        holder.recyclerViewProdutoKItCarrinho.layoutManager = linearLayoutManager
        holder.btnMais.setOnClickListener {
            somar(holder,itemCarrinhoKit)
        }

        holder.btnmenos.setOnClickListener {
            var  quantidade = holder.edtQuantidade.text.toString().toInt()
            val  valorTotalKit = itemCarrinhoKit.por
            val  valorTotalKitDe =itemCarrinhoKit.valorDe
            val data = DataAtual()
            val somaQuantidade = quantidade - 1
            if (somaQuantidade == 0){
                val dialogErro = DialogErro()
                dialogErro.Dialog(holder.btnmenos.context,"Atenção",
                    "Deseja realmente excluir o item do carrinho?",
                    "sim",
                    "cancelar", cancel = true){

                    val carrinhoKitDAO = CarrinhoKitDAO(holder.btnmenos.context)
                    carrinhoKitDAO.excluirItem(itemCarrinhoKit)
                    listCarrinhoKit.removeAt(position)
                    notifyItemRemoved(position)



                }
            }else {
                val  soma = somaQuantidade * valorTotalKit
                val  valorTotalFormat = String.format("%.2f",soma)

                holder.edtQuantidade.setText(somaQuantidade.toString())
                holder.valorTotalKit.text = "R$ "+ valorTotalFormat.replace(".",",")

            }
        }

    }

    override fun getItemCount(): Int {
      return listCarrinhoKit.size
    }

    class ViewHolderCarrinhhoKit(itemView: View) : ViewHolder(itemView){
        val pedidoText = itemView.findViewById<TextView>(R.id.pedidoText)
        val nomeKit = itemView.findViewById<TextView>(R.id.nomeKit)
        val valorTotalKit = itemView.findViewById<TextView>(R.id.valorTotalKit)
        val btnmenos = itemView.findViewById<Button>(R.id.btnmenos)
        val btnMais = itemView.findViewById<Button>(R.id.btnMais)
        val edtQuantidade = itemView.findViewById<EditText>(R.id.edtQuantidade)
        val continuarCarrinhoKit = itemView.findViewById<Button>(R.id.continuarCarrinhoKit)
        val recyclerViewProdutoKItCarrinho = itemView.findViewById<RecyclerView>(R.id.recyclerViewProdutoKItCarrinho)

    }

    class ViewHoldertituloKit(itemView: View) : RecyclerView.ViewHolder(itemView){
        val  nomeKit = itemView.findViewById<TextView>(R.id.nomeKit)
        val  De = itemView.findViewById<TextView>(R.id.De)
        val  por = itemView.findViewById<TextView>(R.id.por)
        val  total = itemView.findViewById<TextView>(R.id.total)
        val  edtQuantidade = itemView.findViewById<EditText>(R.id.edtQuantidade)
        val  btnMais = itemView.findViewById<Button>(R.id.btnMais)
        val  btnmenos = itemView.findViewById<Button>(R.id.btnmenos)
        val  recyProdutosKit = itemView.findViewById<RecyclerView>(R.id.recyProdutosKit)
    }

    fun somar(holder: ViewHolderCarrinhhoKit,item:CarrinhoKit){
        var  quantidade = holder.edtQuantidade.text.toString().toInt()
        val  valorTotalKit = item.por
        val somaQuantidade = quantidade +1
        val  soma = somaQuantidade * valorTotalKit
        val  valorTotalFormat = String.format("%.2f",soma)

        holder.edtQuantidade.setText(somaQuantidade.toString())
        holder.valorTotalKit.text = "R$ "+ valorTotalFormat.replace(".",",")

    }
}
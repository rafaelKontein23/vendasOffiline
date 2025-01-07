package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

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
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoKitDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActCarrinhoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogOperadorLogistico

class AdapterCarrinhoKit(CarrinhoKit:MutableList<CarrinhoKit>, context:ActCarrinhoKit) : Adapter<AdapterCarrinhoKit.ViewHolderCarrinhhoKit>() {
    val  listCarrinhoKit = CarrinhoKit
    var toast: Toast? = null
    val  context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarrinhhoKit {
       val view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_carrinhokit_tiulo, parent,false)

       return ViewHolderCarrinhhoKit(view)
    }
    override fun onBindViewHolder(holder: ViewHolderCarrinhhoKit, position: Int) {

        val itemCarrinhoKit = listCarrinhoKit[position]
        val valorTotalFormat = FormataValores.formatarParaMoeda(itemCarrinhoKit.valortotal)
        holder.pedidoText.text = "Pedido #"+ itemCarrinhoKit.numerPedido.toString()
        holder.nomeKit.text = itemCarrinhoKit.nomeKit
        holder.valorTotalKit.text = valorTotalFormat
        holder.edtQuantidade.setText(itemCarrinhoKit.quantidade.toString())
        val linearLayoutManager = LinearLayoutManager(holder.continuarCarrinhoKit.context)
        val adpterProdutoKit = AdapterProdutoskit(itemCarrinhoKit.listProdutoKit!!)
        holder.recyclerViewProdutoKItCarrinho.adapter = adpterProdutoKit
        holder.recyclerViewProdutoKItCarrinho.layoutManager = linearLayoutManager


        holder.continuarCarrinhoKit.setOnClickListener {
            var  quantidade = holder.edtQuantidade.text.toString().toInt()
            val  valorTotalKit = itemCarrinhoKit.por
            val  soma = valorTotalKit * quantidade

            val dialogOperadorLogistico = DialogOperadorLogistico(context)
            dialogOperadorLogistico.dialogSalvarPedidoKIT(context,itemCarrinhoKit,soma)

        }

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
                    if (listCarrinhoKit.isEmpty()){
                        context.finish()
                    }

                }
            }else {
                val  soma = somaQuantidade * valorTotalKit
                val  valorTotalFormat = FormataValores.formatarParaMoeda(soma)

                holder.edtQuantidade.setText(somaQuantidade.toString())
                holder.valorTotalKit.text =  valorTotalFormat
                val carrinhoKitDAO = CarrinhoKitDAO(holder.btnmenos.context)
                itemCarrinhoKit.quantidade = quantidade
                itemCarrinhoKit.valortotal = soma
                carrinhoKitDAO.atualizaKit(itemCarrinhoKit)

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
        val  De = itemView.findViewById<TextView>(R.id.de)
        val  por = itemView.findViewById<TextView>(R.id.de)
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
        holder.edtQuantidade.setText(somaQuantidade.toString())
        holder.valorTotalKit.text = FormataValores.formatarParaMoeda(soma)
        item.quantidade = quantidade
        item.valortotal = soma
        val carrinhoKitDAO = CarrinhoKitDAO(holder.btnmenos.context)

        carrinhoKitDAO.atualizaKit(item)

    }
}
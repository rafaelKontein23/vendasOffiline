package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
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
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaValorPedidoKitPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.ExcluiPedidoKit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Pedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro

class AdapterTituloKitPedido(
    listaProdutos:MutableList<KitTituloPreco>,context: Context, pedido: PedidoFinalizado,atualizaValorPedidoKitPedido: AtualizaValorPedidoKitPedido,excluiPedidoKit: ExcluiPedidoKit
): Adapter<AdapterTituloKitPedido.viewHolderAdpterPedido>() {

    val listakitTitulo = listaProdutos
    val context =context
    var toast: Toast? = null
    val pedido = pedido
    val  atualizaValorPedidoKitPedido =atualizaValorPedidoKitPedido
    val excluiPedidoKit = excluiPedidoKit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderAdpterPedido {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_titula_kit,parent,false)
        return viewHolderAdpterPedido(view)
    }
    override fun onBindViewHolder(holder: viewHolderAdpterPedido, position: Int) {
        val itemTitulo = listakitTitulo[position]
        val valorDeFormat = String.format("%.2f", itemTitulo.De)
        val valorPorFormat = String.format("%.2f", itemTitulo.Por)

        holder.De.text = "De R$ " + valorDeFormat
        holder.por.text ="Por R$ " + valorPorFormat
        holder.nomeKit.text = itemTitulo.titulo
        holder.total.text = "R$ 00,00"
        holder.edtQuantidade.setText("0")
        val adpterProdutoKit = AdapterProdutoskit(itemTitulo.listaKitProdutos!!)
        val linearLayoutManager = LinearLayoutManager(context)
        holder.recyProdutosKit.layoutManager = linearLayoutManager
        holder.recyProdutosKit.adapter = adpterProdutoKit


        holder.edtQuantidade.setText(itemTitulo.quantidade.toString())
        val  valorTotalFormat = String.format("%.2f",pedido.valorTotal)

        holder.total.text = "R$ "+ valorTotalFormat.replace(".",",")


        holder.btnMais.setOnClickListener {
            somar(holder,itemTitulo)
        }


        holder.btnmenos.setOnClickListener {
            var  quantidade = holder.edtQuantidade.text.toString().toInt()
            val  valorTotalKit = holder.por.text.toString().replace("Por R$ ","").replace(",",".").toDouble()
            val  valorTotalKitDe = holder.De.text.toString().replace("De R$ ","").replace(",",".").toDouble()
            val data = DataAtual()
            val somaQuantidade = quantidade - 1
            if (somaQuantidade == 0){
                val dialogErro = DialogErro()
                dialogErro.Dialog(holder.De.context,"Atenção","essa ação excluira o Pedido, tem certeza que deseja continuar","Sim","Não", cancel = true){
                    val pedidosFinalizadosDAO = PedidosFinalizadosDAO(context)
                    pedidosFinalizadosDAO.excluirItemPedio(pedido.pedidoID.toInt())
                    excluiPedidoKit.excluiPedidoKit()

                }

            }else {
                val  soma = somaQuantidade * valorTotalKit
                val  valorTotalFormat = String.format("%.2f",soma)
                val pedidoFinalizadosDAO = PedidosFinalizadosDAO(context)

                pedidoFinalizadosDAO.atualizaPedidoKit(pedido.pedidoID.toInt(),soma,somaQuantidade)

                holder.edtQuantidade.setText(somaQuantidade.toString())
                holder.total.text = "R$ "+ valorTotalFormat.replace(".",",")

            }

        }
    }
    override fun getItemCount(): Int {
       return listakitTitulo.size
    }

    class viewHolderAdpterPedido(itemView: View) :ViewHolder(itemView){
        val  nomeKit = itemView.findViewById<TextView>(R.id.nomeKit)
        val  De = itemView.findViewById<TextView>(R.id.De)
        val  por = itemView.findViewById<TextView>(R.id.por)
        val  total = itemView.findViewById<TextView>(R.id.total)
        val  edtQuantidade = itemView.findViewById<EditText>(R.id.edtQuantidade)
        val  btnMais = itemView.findViewById<Button>(R.id.btnMais)
        val  btnmenos = itemView.findViewById<Button>(R.id.btnmenos)
        val  recyProdutosKit = itemView.findViewById<RecyclerView>(R.id.recyProdutosKit)
    }
    fun somar(holder: viewHolderAdpterPedido, itemTitulo:KitTituloPreco){
        var  quantidade = holder.edtQuantidade.text.toString().toInt()
        val  valorTotalKit = holder.por.text.toString().replace("Por R$ ","").replace(",",".").toDouble()
        val somaQuantidade = quantidade +1
        val  soma = somaQuantidade * valorTotalKit
        val  valorTotalFormat = String.format("%.2f",soma)

        holder.edtQuantidade.setText(somaQuantidade.toString())
        holder.total.text = "R$ "+ valorTotalFormat.replace(".",",")
        val pedidoFinalizadosDAO = PedidosFinalizadosDAO(context)
        pedidoFinalizadosDAO.atualizaPedidoKit(pedido.pedidoID.toInt(),soma, somaQuantidade)

        atualizaValorPedidoKitPedido.atualizaValorPedidoKitPedido(soma)


    }

}
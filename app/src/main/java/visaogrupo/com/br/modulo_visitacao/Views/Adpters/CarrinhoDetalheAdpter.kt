package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO

class CarrinhoDetalheAdpter (list :MutableList<Carrinho>,view:View,context:Context,atualza:AtualizadetalhesProdutos)  : RecyclerView.Adapter<CarrinhoDetalheAdpter.DetalheCarrrinhoHolder>() {

    val  listaProdutoCarrinho = list
    val view = view
    val context = context
    val  atualza = atualza
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalheCarrrinhoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_carrinhodetalhe,parent,false)
        return  DetalheCarrrinhoHolder(view)
    }

    override fun onBindViewHolder(holder: DetalheCarrrinhoHolder, position: Int) {

        val valorTotalformat = String.format("%.2f", listaProdutoCarrinho[position].valortotal)
        val  valorProduto = String.format("%.2f",listaProdutoCarrinho[position].valor)
        holder.valorTotal.text = "R$ " + valorTotalformat
        holder.valorProgressiva.text = "R$ " + valorProduto
        holder.edtqtd.text= Editable.Factory.getInstance().newEditable(listaProdutoCarrinho[position].quantidade.toString())

        val nomeRemedio = listaProdutoCarrinho[position].nomeProduto
        if (nomeRemedio.length > 20){

            val  nome = nomeRemedio.substring(0,20)+"..."
            holder.nomedoRemedio.text = nome

        }else {

            holder.nomedoRemedio.text = nomeRemedio

        }
        holder.exluir.setOnClickListener {
            val item =listaProdutoCarrinho[position]
            listaProdutoCarrinho.removeAt(position)
            notifyItemRemoved(position)
            val  snackbar = Snackbar.make(view, "Item excluído", Snackbar.LENGTH_LONG).setBackgroundTint(
                Color.WHITE).setTextColor(Color.BLACK)
                .setAction("Desfazer") {
                    listaProdutoCarrinho.add(position, item)
                    notifyItemInserted(position)
                    atualza.detalhes()
                }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event != DISMISS_EVENT_ACTION) {
                            val carrinhoDAO = CarrinhoDAO(context)
                            carrinhoDAO.excluirItem(listaProdutoCarrinho[position].lojaId,listaProdutoCarrinho[position].clienteId,listaProdutoCarrinho[position].produtoCodigo)
                            listaProdutoCarrinho.removeAt(position)
                            atualza.detalhes()
                        }
                    }
                })

            snackbar.show()
        }
        holder.btnMais.setOnClickListener {

            val quatidadeAdicionadaCap =holder.edtqtd.text.toString()
            val quantidade:Int = quatidadeAdicionadaCap.toInt() +1
            var valorProduto =  holder.valorProgressiva.text.toString()
            valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
            val valorPrecoCovertido:Double =     valorProduto.toDouble()
            somaProdutos(quantidade,valorPrecoCovertido,true,holder)
            holder.edtqtd.text = Editable.Factory.getInstance().newEditable(quantidade.toString())

        }

        holder.btnmenos.setOnClickListener {

            val valorTotal = holder.valorTotal.text.toString().replace("R$","").replace(" ","").replace(",",".")
            var valorProduto =  holder.valorProgressiva.text.toString()
            valorProduto = valorProduto.replace("R$","").replace(" ","").replace(",",".")
            val valorTotalDouble = valorTotal.toDouble()
            val valorProdutoDouble = valorProduto.toDouble()
            subtrairPrutudos(valorTotalDouble,valorProdutoDouble,false,0,holder)
            val quantidade:Int = holder.edtqtd.text.toString().toInt() -1
            holder.edtqtd.text = Editable.Factory.getInstance().newEditable(quantidade.toString())

        }

    }

    override fun getItemCount(): Int {
        return listaProdutoCarrinho.size
    }
    class  DetalheCarrrinhoHolder(itemView: View) : ViewHolder(itemView){
        val  exluir = itemView.findViewById<ImageView>(R.id.xcarrinhodetalhes)
        val imagemItem = itemView.findViewById<ImageView>(R.id.imagemItem)
        val nomedoRemedio = itemView.findViewById<TextView>(R.id.nomedoRemedio)
        val valorProgressiva = itemView.findViewById<TextView>(R.id.valorProgressiva)
        val valorTotal = itemView.findViewById<TextView>(R.id.valorTotal)
        val btnmenos = itemView.findViewById<Button>(R.id.btnmenos)
        val btnMais = itemView.findViewById<Button>(R.id.btnMais)
        val edtqtd = itemView.findViewById<EditText>(R.id.edtQuantidade)


    }

    fun somaProdutos(quantidade:Int,valorProtudo:Double,caixapardrao:Boolean,holder:DetalheCarrrinhoHolder){

            val valorAdicionado = quantidade * valorProtudo
            val valorFormatado = String.format("%.2f", valorAdicionado)
            holder.valorTotal.text = "R$ "+ valorFormatado
    }

    fun subtrairPrutudos(valortotal:Double,valorProtudo:Double,caixapadrao:Boolean,quantidade: Int,holder:DetalheCarrrinhoHolder){

            val valorAdicionadosub = valortotal - valorProtudo
            val valorFormatado = String.format("%.2f", valorAdicionadosub)
            holder.valorTotal.text = "R$ "+ valorFormatado

    }

}
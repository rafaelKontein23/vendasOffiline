package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.Models.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.CarrinhoDAO
import java.io.Serializable

class CarrinhoDetalheAdpter (list :MutableList<Carrinho>,view:View,context:Context,atualza:AtualizadetalhesProdutos,startaAtividade: StartaAtividade)  : RecyclerView.Adapter<CarrinhoDetalheAdpter.DetalheCarrrinhoHolder>() {

    var  listaProdutoCarrinho = list
    val view = view
    val context = context
    val  atualza = atualza
    val start = startaAtividade
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalheCarrrinhoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_carrinhodetalhe,parent,false)
        return  DetalheCarrrinhoHolder(view)
    }

    override fun onBindViewHolder(holder: DetalheCarrrinhoHolder, position: Int) {

        val valorTotalformat = String.format("%.2f", listaProdutoCarrinho[position].valortotal)
        val  valorProduto = String.format("%.2f",listaProdutoCarrinho[position].valor)
        holder.valorTotal.text = "R$ " + valorTotalformat
        holder.valorProgressiva.text = "${listaProdutoCarrinho[position].quantidade} Uni.  R$ " + valorProduto
        holder.codigo.text = listaProdutoCarrinho[position].produtoCodigo.toString()

        val nomeRemedio = listaProdutoCarrinho[position].nomeProduto
        holder.nomedoRemedio.text = nomeRemedio


        holder.exluir.setOnClickListener {
            val item =listaProdutoCarrinho[position]
            listaProdutoCarrinho.removeAt(position)
            notifyItemRemoved(position)
            val  snackbar = Snackbar.make(view, "Item excluído", Snackbar.LENGTH_LONG).setBackgroundTint(
                Color.WHITE).setTextColor(Color.BLACK)
                .setAction("Desfazer") {
                    listaProdutoCarrinho.add(position, item)
                    notifyItemInserted(position)
                    atualza.detalhes(listaProdutoCarrinho,true,position,0.0)
                }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event != DISMISS_EVENT_ACTION) {
                            val carrinhoDAO = CarrinhoDAO(context)
                            carrinhoDAO.excluirItem(listaProdutoCarrinho[position].lojaId,listaProdutoCarrinho[position].clienteId,listaProdutoCarrinho[position].produtoCodigo)
                            listaProdutoCarrinho.removeAt(position)
                            atualza.detalhes(listaProdutoCarrinho,true,position,0.0)
                        }
                    }
                })

            snackbar.show()
        }

        holder.containerDetalhe.setOnClickListener {
            val intent = Intent(context, ActProtudoDetalhe::class.java)
            val bundle = Bundle()
            val produto = ProdutoProgressiva(listaProdutoCarrinho[position].nomeProduto,
                "",listaProdutoCarrinho[position].barra,"",
                listaProdutoCarrinho[position].produtoCodigo,
                listaProdutoCarrinho[position].pf.toString(),
                0.0/*lembrar de colocar ovalor aqui*/,listaProdutoCarrinho[position].quantidade,
                24/*lembrar de colocar ovalor aqui*/,1,
                listaProdutoCarrinho[position].valor,
                listaProdutoCarrinho[position].quantidade,
                listaProdutoCarrinho[position].valortotal)
            bundle.putSerializable("ProtudoSelecionado", produto as Serializable)
            intent.putExtra("ProtudoSelecionado_bundle", bundle)
            start.atividade(intent)
        }

    }

    override fun getItemCount(): Int {
        return listaProdutoCarrinho.size
    }
    class  DetalheCarrrinhoHolder(itemView: View) : ViewHolder(itemView){
        val  exluir = itemView.findViewById<ImageView>(R.id.xcarrinhodetalhes)
        val nomedoRemedio = itemView.findViewById<TextView>(R.id.nomedoRemedio)
        val valorProgressiva = itemView.findViewById<TextView>(R.id.valorProgressiva)
        val valorTotal = itemView.findViewById<TextView>(R.id.valorTotal)
        val containerDetalhe = itemView.findViewById<ConstraintLayout>(R.id.containerDetalhe)
        val codigo = itemView.findViewById<TextView>(R.id.codproduto)


    }

}
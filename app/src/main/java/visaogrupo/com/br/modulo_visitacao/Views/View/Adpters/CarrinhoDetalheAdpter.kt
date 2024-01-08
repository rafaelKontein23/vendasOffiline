package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import java.io.Serializable

class CarrinhoDetalheAdpter (list :MutableList<Carrinho>, view:View, context:Context, atualza: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizadetalhesProdutos, startaAtividade: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.StartaAtividade)  : RecyclerView.Adapter<CarrinhoDetalheAdpter.DetalheCarrrinhoHolder>() {

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
                listaProdutoCarrinho[position].pmc,listaProdutoCarrinho[position].quantidade,
                listaProdutoCarrinho[position].caixapadrao,1,
                listaProdutoCarrinho[position].valor,
                listaProdutoCarrinho[position].quantidade,
                listaProdutoCarrinho[position].valortotal,"")




            bundle.putSerializable("ImagemProd", listaProdutoCarrinho[position].base64)
            intent.putExtra("ProtudoSelecionado_bundle", bundle)
            intent.putExtra("ImagemProd_bundle", bundle)
            bundle.putSerializable("ProtudoSelecionado", produto as Serializable)
            intent.putExtra("ProtudoSelecionado_bundle", bundle)
            intent.putExtra("estaNoCarrinho",1)
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
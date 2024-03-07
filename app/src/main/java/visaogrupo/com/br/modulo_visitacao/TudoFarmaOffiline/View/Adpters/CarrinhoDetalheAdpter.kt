package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizadetalhesProdutos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import java.io.Serializable

class CarrinhoDetalheAdpter (list :MutableList<Carrinho>,
                             view:View,
                             context:Context,
                             atualza: AtualizadetalhesProdutos,
                             startaAtividade: StartaAtividade,
                             carrinhodetalhe:Boolean): RecyclerView.Adapter<CarrinhoDetalheAdpter.DetalheCarrrinhoHolder>() {

    var  listaProdutoCarrinho = list
    val view = view
    val context = context
    val  atualza = atualza
    val start = startaAtividade
    val carrinhodetalhe= carrinhodetalhe
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalheCarrrinhoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_carrinhodetalhe,parent,false)
        return  DetalheCarrrinhoHolder(view)
    }

    override fun onBindViewHolder(holder: DetalheCarrrinhoHolder, position: Int) {
        val itemCarrinhoProduto =  listaProdutoCarrinho[position]

        holder.valorTotal.text       = FormataValores.formatarParaMoeda(itemCarrinhoProduto.valortotal)
        holder.valorProgressiva.text = "${itemCarrinhoProduto.quantidade} Uni.  ${FormataValores.formatarParaMoeda(itemCarrinhoProduto.valor)}"
        holder.codigo.text           = itemCarrinhoProduto.produtoCodigo.toString()
        holder.desconto.text         = itemCarrinhoProduto.desconto.toString() + "%"
        holder.nomedoRemedio.text    = itemCarrinhoProduto.nomeProduto

        holder.exluir.isVisible           = itemCarrinhoProduto.LojaTipo !=13
        holder.rever.isVisible            = itemCarrinhoProduto.LojaTipo !=13
        holder.containerDetalhe.isEnabled = itemCarrinhoProduto.LojaTipo !=13

        holder.exluir.setOnClickListener {
            listaProdutoCarrinho.removeAt(position)
            notifyItemRemoved(position)

            val  snackbar = Snackbar.make(view, "Item excluído", Snackbar.LENGTH_LONG).setBackgroundTint(
                Color.WHITE).setTextColor(Color.BLACK)
                .setAction("Desfazer") {
                    listaProdutoCarrinho.add(position, itemCarrinhoProduto)
                    notifyItemInserted(position)
                    atualza.detalhes(listaProdutoCarrinho,true,position,0.0)
                }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event != DISMISS_EVENT_ACTION) {
                            val carrinhoDAO = CarrinhoDAO(context)
                            carrinhoDAO.excluirItem(itemCarrinhoProduto.lojaId,itemCarrinhoProduto.clienteId,itemCarrinhoProduto.produtoCodigo)
                            atualza.detalhes(listaProdutoCarrinho,true,position,0.0)
                        }
                    }
                })

            snackbar.show()
        }


        holder.containerDetalhe.setOnClickListener {
            val intent = Intent(context, ActProtudoDetalhe::class.java)
            val bundle = Bundle()
            val produto = ProdutoProgressiva(
                itemCarrinhoProduto.nomeProduto,
                "",
                itemCarrinhoProduto.barra,
                "",
                itemCarrinhoProduto.produtoCodigo,
                itemCarrinhoProduto.pf.toString(),
                itemCarrinhoProduto.pmc,
                itemCarrinhoProduto.quantidade,
                itemCarrinhoProduto.caixapadrao,
                1,
                itemCarrinhoProduto.valor,
                itemCarrinhoProduto.quantidade,
                itemCarrinhoProduto.valortotal,
                "",
                0,
                itemCarrinhoProduto.centro,
                itemCarrinhoProduto.porecentagem
                ,0.0,
                0.0
            )

            bundle.putSerializable("ImagemProd", listaProdutoCarrinho[position].base64)
            intent.putExtra("ProtudoSelecionado_bundle", bundle)
            intent.putExtra("ImagemProd_bundle", bundle)
            bundle.putSerializable("ProtudoSelecionado", produto as Serializable)
            intent.putExtra("ProtudoSelecionado_bundle", bundle)
            intent.putExtra("estaNoCarrinho",1)
            intent.putExtra("estaNoPedido", false)
            intent.putExtra("pedidoID", 0)
            intent.putExtra("Pedido", false)
            intent.putExtra("CarrinhoDetalhe", carrinhodetalhe)

            start.atividade(intent)
        }

    }

    override fun getItemCount(): Int {
        return listaProdutoCarrinho.size
    }
    class  DetalheCarrrinhoHolder(itemView: View) : ViewHolder(itemView){
        val  exluir =           itemView.findViewById<ImageView>(R.id.xcarrinhodetalhes)
        val nomedoRemedio =     itemView.findViewById<TextView>(R.id.nomedoRemedio)
        val valorProgressiva =  itemView.findViewById<TextView>(R.id.valorProgressiva)
        val valorTotal =        itemView.findViewById<TextView>(R.id.valorTotal)
        val containerDetalhe =  itemView.findViewById<ConstraintLayout>(R.id.containerDetalhe)
        val codigo =            itemView.findViewById<TextView>(R.id.codproduto)
        val desconto =          itemView.findViewById<TextView>(R.id.desconto)
        val rever =             itemView.findViewById<ImageView>(R.id.rever)


    }

}
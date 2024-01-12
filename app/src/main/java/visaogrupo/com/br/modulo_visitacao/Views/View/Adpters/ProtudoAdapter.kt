package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
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
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.ExcluiItemcarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.StartaAtividade
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import java.io.Serializable

class ProtudoAdapter(list:MutableList<ProdutoProgressiva>, context:
Context, start : StartaAtividade, loja_id:Int, cliente_id:Int, excluiItemcarrinho: ExcluiItemcarrinho, fragmentView:View): Adapter<ProtudoAdapter.ProdutoViewHolder>() {
    var listaProtudos = list
    val  context = context
    val start =  start
    val  loja_id = loja_id
    val  cliente_id = cliente_id
    val excluiItemcarrinho = excluiItemcarrinho
    val  fragmentView = fragmentView
    var count = 20
    var carregando = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
       val view =  LayoutInflater.from(parent.context).inflate(R.layout.celula_produtos,parent,false)

        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        if(!carregando){
            holder.efeito.hideShimmer()

            if (listaProtudos != null){
                holder.nomeProtudo.background = ContextCompat.getDrawable(context,R.color.transparente)
                holder.codigoProduto.background = ContextCompat.getDrawable(context,R.color.transparente)
                holder.barra.background = ContextCompat.getDrawable(context,R.color.transparente)
                holder.valor.background = ContextCompat.getDrawable(context,R.color.transparente)
                holder.imgProduto.background = ContextCompat.getDrawable(context,R.color.transparente)
                holder.nomeProtudo.text = listaProtudos[position].nome
                holder.codigoProduto.text = listaProtudos[position].ProdutoCodigo.toString()
                holder.barra.text = listaProtudos[position].barra
                holder.valor.text = "R$ " + listaProtudos[position].valor.toString().replace(".",",")
                holder.nomeProtudo.background = ContextCompat.getDrawable(context,R.color.transparente)

                if (listaProtudos[position].estaNoCarrinho  == 1){
                    val carrinhoDAO = CarrinhoDAO(context)
                    val valores = carrinhoDAO.buscaProgressivavalor(listaProtudos[position].ProdutoCodigo)
                    holder.quantidade.isVisible = true
                    holder.excluiritem.isVisible = true
                    holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.verdenutoon)
                    holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.corprodto)
                    holder.quantidade.text = "x" + listaProtudos[position].quantidadeCarrinho.toString()
                    holder.progressivaSelecionada.text = valores
                }else{
                    holder.quantidade.isVisible = false
                    holder.excluiritem.isVisible = false
                    holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.corlinhaorigin)
                    holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.transparente)
                }
                holder.constrainProtudos.setOnClickListener {
                    val intent = Intent(context, ActProtudoDetalhe::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("ProtudoSelecionado", listaProtudos[position] as Serializable)
                    intent.putExtra("ProtudoSelecionado_bundle", bundle)

                    bundle.putSerializable("ImagemProd", listaProtudos[position].base64)
                    intent.putExtra("ProtudoSelecionado_bundle", bundle)
                    intent.putExtra("ImagemProd_bundle", bundle)
                    intent.putExtra("estaNoCarrinho", listaProtudos[position].estaNoCarrinho)
                    start.atividade(intent)

                }
                CoroutineScope(Dispatchers.IO).launch {
                    if(!listaProtudos[position].base64.isEmpty()){
                        val bitmapimage = exibirImagemBase64(listaProtudos[position].base64)
                        CoroutineScope(Dispatchers.Main).launch {
                            holder.imgProduto.setImageBitmap(bitmapimage)
                        }

                    }else{
                        CoroutineScope(Dispatchers.Main).launch {
                            holder.imgProduto.setImageResource(R.drawable.padrao)
                        }

                    }
                }


                holder.excluiritem.setOnClickListener {

                    holder.quantidade.isVisible = false
                    holder.excluiritem.isVisible = false
                    holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.corlinhaorigin)
                    holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.transparente)

                    val  snackbar =Snackbar.make(fragmentView, "Item excluído", Snackbar.LENGTH_LONG).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK)
                        .setAction("Desfazer") {

                            holder.quantidade.isVisible = true
                            holder.excluiritem.isVisible = true
                            holder.linhaProtudos.background = ContextCompat.getDrawable(context,R.color.verdenutoon)
                            holder.constrainProtudos.background = ContextCompat.getDrawable(context,R.color.corprodto)
                            holder.quantidade.text = "x" + listaProtudos[position].quantidadeCarrinho.toString()
                        }
                        .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                if (event != DISMISS_EVENT_ACTION) {
                                    val carrinhoDAO = CarrinhoDAO(context)
                                    carrinhoDAO.excluirItem(loja_id,cliente_id,listaProtudos[position].ProdutoCodigo)
                                    excluiItemcarrinho.exluiItem()
                                }
                            }
                        })
                    val layoutParams = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(
                        layoutParams.leftMargin,
                        layoutParams.topMargin,
                        layoutParams.rightMargin,
                        200
                    )
                    snackbar.view.layoutParams = layoutParams
                    snackbar.show()
                }
            }


        }




    }

    override fun getItemCount(): Int {
       return  listaProtudos.size
    }
    // nao esta sendo usado
    fun addItems(newItems: List<ProdutoProgressiva>) {
        val startPosition = listaProtudos.size
        listaProtudos.addAll(newItems)
        CoroutineScope(Dispatchers.Main).launch {
            notifyItemRangeInserted(startPosition, newItems.size)
        }

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
        val efeito =itemView.findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)
        val progressivaSelecionada = itemView.findViewById<TextView>(R.id.progressivaSelecionada)

    }
     fun exibirImagemBase64(imagemBase64: String):Bitmap {
        // Decodificar a string Base64 em um array de bytes
        val imageBytes = Base64.decode(imagemBase64, Base64.DEFAULT)

        // Converter o array de bytes em um objeto Bitmap
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return  bitmap

    }
}
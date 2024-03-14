package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitProtudos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores


class AdapterProdutoskit (listaProdutos:List<KitProtudos>): Adapter<AdapterProdutoskit.ViewholderProdutoKit>() {
     val listaProdutos = listaProdutos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderProdutoKit {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_produto_kit,parent,false)
            return  ViewholderProdutoKit(view)
    }
    override fun onBindViewHolder(holder: ViewholderProdutoKit, position: Int) {
          val itemProdtudo = listaProdutos[position]
          holder.quantidade.text = itemProdtudo.quantidade.toString() + " uni."
          holder.NomeProtudo.text = itemProdtudo.produtoNome
          holder.barraProduto.text = itemProdtudo.barra
          val valorProdutoFormat = FormataValores.formatarParaMoeda(itemProdtudo.valorTotal)
          holder.valorDesconto.text = "R$ " + valorProdutoFormat.replace(".",",")
          if ( itemProdtudo.imgBase64 != null){
              if (!itemProdtudo.imgBase64!!.isEmpty() ){
                  val imageBytes = Base64.decode(itemProdtudo.imgBase64, Base64.DEFAULT)
                  val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                  holder.imgproduto.setImageBitmap(bitmap)

              }

          }


    }

    override fun getItemCount(): Int {
        return  listaProdutos.size
    }

    class ViewholderProdutoKit(itemView: View) : ViewHolder(itemView){
         val imgproduto = itemView.findViewById<ImageView>(R.id.imgproduto)
         val NomeProtudo = itemView.findViewById<TextView>(R.id.NomeProtudo)
         val barraProduto = itemView.findViewById<TextView>(R.id.barraProduto)
         val quantidade = itemView.findViewById<TextView>(R.id.quantidade)
         val valorDesconto =  itemView.findViewById<TextView>(R.id.valorDesconto)
         val  codigoProtudo = itemView.findViewById<TextView>(R.id.codigoProtudo)
    }


}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaProgress
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.GrupoLojaAb
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoAB
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoValorAB
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores


class AdpterProdutoGrupoAB (list: MutableList<ProdutoAB>, prioridade:Int, context: Context,
                            valorTotalA:MutableList<ProdutoValorAB>, valorTotalB:
                            MutableList<ProdutoValorAB>, AtualizaProgress: AtualizaProgress, listaItem:  MutableList<GrupoLojaAb>
                            ): Adapter<AdpterProdutoGrupoAB.ViewHolderProdutoGrupoAB>() {
    val listaAb = list
    val prioridade = prioridade
    val context = context
    var valorTotalB = valorTotalB
    var valorTotalA = valorTotalA
    val AtualizaProgress = AtualizaProgress
    var toast: Toast? = null
    var listaItem = listaItem
    var atualizacarrinho = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProdutoGrupoAB {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_grupo_produto_ab,parent,false)
        return ViewHolderProdutoGrupoAB(view)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolderProdutoGrupoAB, position: Int) {
        val  itemListaAbProdutos = listaAb[position]
        holder.barraProduto.text = itemListaAbProdutos.Barra
        holder.NomeProtudo.text = itemListaAbProdutos.Nome
        holder.codigoProtudo.text = itemListaAbProdutos.Produto_codigo.toString()
        holder.quantidade.text = itemListaAbProdutos.Quantidade.toString() +"uni."
        holder.desconto.text = itemListaAbProdutos.Desconto.toString() +"% "

        val valorComDesconto = (itemListaAbProdutos.PF *itemListaAbProdutos.Desconto ) /100
        val valorTotalComDesconto = itemListaAbProdutos.PF - valorComDesconto
        val valorFormatComDesconto = FormataValores.formatarParaMoeda(valorTotalComDesconto)



        holder.valorProgressiva.text = valorFormatComDesconto
        holder.pf.text ="${FormataValores.formatarParaMoeda(valorTotalComDesconto)}"

        if (itemListaAbProdutos.estaNoCarrinho == 1){
            holder.edtQuantidade.setText(itemListaAbProdutos.quantidadeCarrinho.toString())
            somaItem(holder,valorTotalComDesconto, false,itemListaAbProdutos)

        }else {
            holder.edtQuantidade.setText("0")
            holder.valorToral.text = "R$ " +valorFormatComDesconto


        }

        if (!itemListaAbProdutos.Imagem.isEmpty()){
            val imageBytes = Base64.decode(itemListaAbProdutos.Imagem, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.imagemProdtudo.setImageBitmap(bitmap)

        }

        if (prioridade == 1){
              holder.btnMais.backgroundTintList =  context.getColorStateList(R.color.azullojaA)
              holder.btnmenos.backgroundTintList =  context.getColorStateList(R.color.azullojaA)
              holder.edtQuantidade.setBackgroundResource(R.drawable.bordas_azul_produto_a)

        }else
        {
            holder.btnMais.backgroundTintList =  context.getColorStateList(R.color.laranjalojaB)
            holder.btnmenos.backgroundTintList =  context.getColorStateList(R.color.laranjalojaB)
            holder.edtQuantidade.setBackgroundResource(R.drawable.bordas_azul_produto_b)

        }

        holder.btnMais.setOnClickListener {
            somaItem(holder,valorTotalComDesconto,true, itemListaAbProdutos)
        }

        holder.btnmenos.setOnClickListener {
            val quantidade = holder.edtQuantidade.text.toString().toInt()
            if (quantidade == 0){

                toast?.cancel()
                toast =   Toast.makeText(context,"Quantidade não pode ser menor que zero",Toast.LENGTH_SHORT)
                toast?.show()
            }else {
                somaItem(holder,valorTotalComDesconto,false,itemListaAbProdutos)

            }
        }
    }



    override fun getItemCount(): Int {
       return listaAb.size
    }

    class ViewHolderProdutoGrupoAB (itemView: View):ViewHolder(itemView){
          val codigoProtudo = itemView.findViewById<TextView>(R.id.codigoProtudo)
          val NomeProtudo = itemView.findViewById<TextView>(R.id.NomeProtudo)
          val barraProduto = itemView.findViewById<TextView>(R.id.barraProduto)
          val quantidade = itemView.findViewById<TextView>(R.id.quantidade)
          val desconto = itemView.findViewById<TextView>(R.id.desconto)
          val pf = itemView.findViewById<TextView>(R.id.pf)
          val valorToral = itemView.findViewById<TextView>(R.id.valorToltal)
          val btnmenos = itemView.findViewById<Button>(R.id.btnmenos)
          val btnMais = itemView.findViewById<Button>(R.id.btnMais)
          val edtQuantidade = itemView.findViewById<EditText>(R.id.edtQuantidade)
          val imagemProdtudo = itemView.findViewById<ImageView>(R.id.imgproduto)
          val valorProgressiva = itemView.findViewById<TextView>(R.id.valorProgressiva)

    }
    fun somaItem(holder:ViewHolderProdutoGrupoAB, valorComDesconto:Double,mais:Boolean,itemProdutoAB:ProdutoAB){
        var getQuantidade =0

        getQuantidade = holder.edtQuantidade.text.toString().toInt()


        var valorQuantidade = 0
        if (mais){
            valorQuantidade = getQuantidade +1
        }else{
            valorQuantidade = getQuantidade -1
        }

        var valorTotal = valorComDesconto * valorQuantidade
        holder.valorToral.text = FormataValores.formatarParaMoeda(valorTotal)
        holder.edtQuantidade.setText(valorQuantidade.toString())
        val produtoValorAB = ProdutoValorAB(holder.codigoProtudo.text.toString(),valorTotal)
        var temNaLista = false
        if (prioridade == 1){
            if (valorTotalA.isEmpty()){
                valorTotalA.add(produtoValorAB)

            }else{

                for ( (i,valor) in valorTotalA.withIndex()){
                    if (valor.codigoProduto == produtoValorAB.codigoProduto ){
                        valorTotalA[i].valortotalProduto = valorTotal
                        temNaLista = true
                        break
                    }else{
                        temNaLista = false

                    }
                }
                if (!temNaLista){
                    valorTotalA.add(produtoValorAB)
                }
            }
        }else{

            if (valorTotalB.isEmpty()){
                valorTotalB.add(produtoValorAB)

            }else{
                for ( (i,valor) in valorTotalB.withIndex()){
                    if (valor.codigoProduto == produtoValorAB.codigoProduto ){
                        valorTotalB[i].valortotalProduto = valorTotal
                        temNaLista = true
                        break
                    }else{
                        temNaLista = false

                    }
                }
                if (!temNaLista){
                    valorTotalB.add(produtoValorAB)
                }
            }
        }

        if (prioridade == 1){
            AtualizaProgress.atualizaprogress(valorTotalA,valorTotalB,true,mais, itemProdutoAB, valorQuantidade, temNaLista, valorTotal)

        }else{
            AtualizaProgress.atualizaprogress(valorTotalA,valorTotalB,false,mais, itemProdutoAB, valorQuantidade, temNaLista, valorTotal)
        }
    }
}
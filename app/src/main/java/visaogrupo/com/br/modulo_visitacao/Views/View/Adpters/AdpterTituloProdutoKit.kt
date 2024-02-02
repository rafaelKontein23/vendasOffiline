package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaValorTotalKitCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProgressivaSelecionada
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual

class AdpterTituloProdutoKit(listaProdutos:MutableList<KitTituloPreco>,
                             context: Context,
                             atualizaValorTotalKitCarrinho: AtualizaValorTotalKitCarrinho,
                             lojaSelecionada: Lojas,
                             clienteSelecionado:Clientes,
                             login:Int) : Adapter<AdpterTituloProdutoKit.ViewHoldertituloKit>() {

    val listakitTitulo = listaProdutos
    val  context = context
    val atualizaValorTotalKitCarrinho = atualizaValorTotalKitCarrinho
    val  lojaSelecionada = lojaSelecionada
    val  clienteSelecionado = clienteSelecionado
    val  login =login

    var toast: Toast? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoldertituloKit {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_titula_kit,parent,false)
        return ViewHoldertituloKit(view)
    }


    override fun onBindViewHolder(holder: ViewHoldertituloKit, position: Int) {
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
        if (itemTitulo.quantidade >0){
            holder.edtQuantidade.setText(itemTitulo.quantidade.toString())
            somar(holder,itemTitulo)
        }




        holder.btnMais.setOnClickListener {
            somar(holder,itemTitulo)
        }



        holder.btnmenos.setOnClickListener {
            var  quantidade = holder.edtQuantidade.text.toString().toInt()
            val  valorTotalKit = holder.por.text.toString().replace("Por R$ ","").replace(",",".").toDouble()
            val  valorTotalKitDe = holder.De.text.toString().replace("De R$ ","").replace(",",".").toDouble()
            val data = DataAtual()
            val somaQuantidade = quantidade - 1
            if (somaQuantidade < 0){
                toast?.cancel()
                toast =   Toast.makeText(context,"Quantidade não pode ser menor que zero",Toast.LENGTH_SHORT)
                toast?.show()

            }else {
                val  soma = somaQuantidade * valorTotalKit
                val  valorTotalFormat = String.format("%.2f",soma)

                holder.edtQuantidade.setText(somaQuantidade.toString())
                holder.total.text = "R$ "+ valorTotalFormat.replace(".",",")
                val carrinhoKit = CarrinhoKit(lojaSelecionada.loja_id,
                    clienteSelecionado.Empresa_id,
                    itemTitulo.kitId,
                    "",
                    login,clienteSelecionado.UF,
                    0.0,
                    0.0,
                    0,somaQuantidade,
                    valorTotalKit,
                    valorTotalKitDe,
                    0,
                    0.0,
                    clienteSelecionado.CODLISTAPRECOSYNC,
                    soma,
                    itemTitulo.titulo,
                    lojaSelecionada.nome,
                    clienteSelecionado.RazaoSocial,
                    clienteSelecionado.CNPJ,
                    data.recuperaData(),
                    lojaSelecionada.MinimoValor,
                    clienteSelecionado.FormaPagamentoExclusiva,
                    lojaSelecionada.Qtd_Minima_Operador,
                    lojaSelecionada.Qtd_Maxima_Operador,
                    lojaSelecionada.RegraPrazoMedio,
                    lojaSelecionada.LojaTipo,
                    0.0,
                    itemTitulo.listaKitProdutos!!,
                    0
                )

                atualizaValorTotalKitCarrinho.atualizaValor(carrinhoKit)
            }

        }

    }

    override fun getItemCount(): Int {
        return listakitTitulo.size
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

    fun somar(holder: ViewHoldertituloKit, itemTitulo:KitTituloPreco){
        var  quantidade = holder.edtQuantidade.text.toString().toInt()
        val  valorTotalKit = holder.por.text.toString().replace("Por R$ ","").replace(",",".").toDouble()
        val  valorTotalKitDe = holder.De.text.toString().replace("De R$ ","").replace(",",".").toDouble()
        val somaQuantidade = quantidade +1
        val  soma = somaQuantidade * valorTotalKit

        val  valorTotalFormat = String.format("%.2f",soma)
        val data = DataAtual()

        holder.edtQuantidade.setText(somaQuantidade.toString())
        holder.total.text = "R$ "+ valorTotalFormat.replace(".",",")
        val carrinhoKit = CarrinhoKit(lojaSelecionada.loja_id,
            clienteSelecionado.Empresa_id,
            itemTitulo.kitId,
            "",
            login,clienteSelecionado.UF,
            0.0,
            0.0,
            0,somaQuantidade,
            valorTotalKit,
            valorTotalKitDe,
            0,
            0.0,
            clienteSelecionado.CODLISTAPRECOSYNC,
            soma,
            itemTitulo.titulo,
            lojaSelecionada.nome,
            clienteSelecionado.RazaoSocial,
            clienteSelecionado.CNPJ,
            data.recuperaData(),
            lojaSelecionada.MinimoValor,
            clienteSelecionado.FormaPagamentoExclusiva,
            lojaSelecionada.Qtd_Minima_Operador,
            lojaSelecionada.Qtd_Maxima_Operador,
            lojaSelecionada.RegraPrazoMedio,
            lojaSelecionada.LojaTipo,
            0.0,
            itemTitulo.listaKitProdutos!!,0

        )
        atualizaValorTotalKitCarrinho.atualizaValor(carrinhoKit)
    }


}
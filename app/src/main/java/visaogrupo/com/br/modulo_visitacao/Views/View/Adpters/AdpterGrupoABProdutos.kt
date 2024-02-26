package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaProgress
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.GrupoLojaAb
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoAB
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoValorAB

class AdpterGrupoABProdutos (list:  MutableList<GrupoLojaAb>,
                             context: Context, valorTotalA:MutableList<ProdutoValorAB>,
                             valorTotalB: MutableList<ProdutoValorAB>,
                             AtualizaProgress: AtualizaProgress,

): RecyclerView.Adapter<AdpterGrupoABProdutos.ViewHolderGrupoABProdutos>() {
    val listalojaAb = list
    var fechado = true
    val context = context
    var valorTotalA = valorTotalA
    var valorTotalB = valorTotalB
    val AtualizaProgress = AtualizaProgress
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGrupoABProdutos {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.celula_loja_grupo_ab, parent,false)
        return ViewHolderGrupoABProdutos(view)
    }


    override fun onBindViewHolder(holder: ViewHolderGrupoABProdutos, position: Int) {
         val itemlohaGrupoAB = listalojaAb[position]
         holder.nomeGrupo.text = itemlohaGrupoAB.NomeGrupo
         val linearLayout = LinearLayoutManager(context)
         val adapter = AdpterProdutoGrupoAB(itemlohaGrupoAB.listaProduto!!,
             itemlohaGrupoAB.Prioridade, context, valorTotalA,valorTotalB,AtualizaProgress)
         holder.recyProduto.layoutManager = linearLayout
         holder.recyProduto.adapter =adapter
         holder.nomeGrupo.text = itemlohaGrupoAB.Grupo
        if (itemlohaGrupoAB.Prioridade == 1){
            holder.constraintLayout5.setBackgroundColor(Color.parseColor("#52BFD3"))
        }else {
            holder.constraintLayout5.setBackgroundColor(Color.parseColor("#FF6F01"))
        }

        holder.constraintLayout5.setOnClickListener {
            if (fechado){
                holder.recyProduto.isVisible = false
                fechado = false

            }else{
                holder.recyProduto.isVisible = true
                fechado = true

            }
        }


    }

    override fun getItemCount(): Int {
        return listalojaAb.size
    }

    class ViewHolderGrupoABProdutos (itemVew: View): ViewHolder(itemVew){
        val nomeGrupo = itemVew.findViewById<TextView>(R.id.nomeGrupo)
        val recyProduto = itemVew.findViewById<RecyclerView>(R.id.recyProduto)
        val setaFechar = itemVew.findViewById<ImageView>(R.id.setaFechar)
        val constraintLayout5 = itemVew.findViewById<ConstraintLayout>(R.id.constraintLayout5)
    }


}
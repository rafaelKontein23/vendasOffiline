package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Act_Pricipal
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Enuns_Cadastro.TrocaItemSelecionado
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.MudarFragment
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

class LojasAdapter (list :List<Lojas>,trocarcorItem: TrocarcorItem,frameid:Int,supportFragmentManager:FragmentManager,carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho) : Adapter<LojasAdapter.LojasViewHolder>() {
    var listaLojas = list
    val trocarcorItem = trocarcorItem
    val  frameid = frameid
    val supportFragmentManager = supportFragmentManager
    val carrinhoVisible = carrinhoVisible
    val  atualizaCarrinho = atualizaCarrinho

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LojasViewHolder {
     val  view = LayoutInflater.from(parent.context).inflate(R.layout.celula_loja,parent,false)
     return LojasViewHolder(view)
    }

    override fun onBindViewHolder(holder: LojasViewHolder, position: Int) {
        holder.nomeloja.text = listaLojas[position].nome.toString()
        holder.valorMinimo.text = listaLojas[position].MinimoValor.toString()

        holder.containerLoja.setOnClickListener {
            val sharedPreferences = holder.valorMinimo.context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val objetoSerializado = gson.toJson(listaLojas[position])
            val editor = sharedPreferences.edit()
            editor.putString("LojaSelecionada", objetoSerializado)
            editor.apply()

            //inicia produtos
            Act_Pricipal.troca = TrocaItemSelecionado.Prodtudos
            trocarcorItem.trocacor()
            val mudarFragment = MudarFragment()
            mudarFragment.openFragmentProtudos(supportFragmentManager,frameid,carrinhoVisible, atualizaCarrinho)
        }
    }

    override fun getItemCount(): Int {
       return listaLojas.size
    }

    class LojasViewHolder(itemView: View) : ViewHolder(itemView){
        val containerLoja = itemView.findViewById<ConstraintLayout>(R.id.containerloja)
        val nomeloja = itemView.findViewById<TextView>(R.id.nomeloja)
        val valorMinimo = itemView.findViewById<TextView>(R.id.valorminimo)
    }


}
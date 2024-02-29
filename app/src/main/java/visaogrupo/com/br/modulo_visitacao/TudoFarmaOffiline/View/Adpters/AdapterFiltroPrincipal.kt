package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaListaFiltro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FiltrosPricipaisFilt

class AdapterFiltroPrincipal(listaFiltroFilt :MutableList<FiltrosPricipaisFilt>,context: Context, atualizaListaFiltro:AtualizaListaFiltro): Adapter<AdapterFiltroPrincipal.ViewHolderFiltroPrincipal>() {
    val listaFiltroFilt = listaFiltroFilt
    val context = context
    val  atualizaListaFiltro = atualizaListaFiltro



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFiltroPrincipal {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_filtro_principal,parent, false)

        return ViewHolderFiltroPrincipal(view)
    }

    override fun onBindViewHolder(holder: ViewHolderFiltroPrincipal, position: Int) {
        holder.titulofiltroPrincipal.text = listaFiltroFilt[position].Descricao
        val adapterFiltroSub = AdapterFiltroSub(listaFiltroFilt[position].listaFiltros,atualizaListaFiltro)
        val linearLayoutManager = LinearLayoutManager(context)
        holder.recyclerFiltros.layoutManager = linearLayoutManager
        holder.recyclerFiltros.adapter = adapterFiltroSub


    }
    override fun getItemCount(): Int {
        return listaFiltroFilt.size
    }
    class ViewHolderFiltroPrincipal (itemView: View) :ViewHolder(itemView){
        val titulofiltroPrincipal = itemView.findViewById<TextView>(R.id.titulofiltroPrincipal)
        val recyclerFiltros = itemView.findViewById<RecyclerView>(R.id.recyclerFiltros)
    }
}
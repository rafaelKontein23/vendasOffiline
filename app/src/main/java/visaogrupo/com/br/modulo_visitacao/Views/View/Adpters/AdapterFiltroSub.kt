package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaListaFiltro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Filtros

class AdapterFiltroSub(listasFiltro:MutableList<Filtros>, atualizaListaFiltro: AtualizaListaFiltro) :Adapter<AdapterFiltroSub.ViewHolderFiltro>() {
    val listasFiltro = listasFiltro
    val atualizaListaFiltro = atualizaListaFiltro
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFiltro {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_filtro_sub,parent,false)
        return ViewHolderFiltro(view)
    }


    override fun onBindViewHolder(holder: ViewHolderFiltro, position: Int) {
       holder.checkBoxFiltro.text = listasFiltro[position].Descricao

        holder.checkBoxFiltro.setOnCheckedChangeListener { buttonView, isChecked ->
            // Lógica a ser executada quando o estado do CheckBox muda
            if (isChecked) {
              atualizaListaFiltro.atualizaFiltro(listasFiltro[position].Filtro_id,false)
            }else{
                atualizaListaFiltro.atualizaFiltro(listasFiltro[position].Filtro_id,true)

            }
        }
    }
    override fun getItemCount(): Int {
      return  listasFiltro.size
    }

    class ViewHolderFiltro(itemView : View):ViewHolder(itemView){
        val checkBoxFiltro = itemView.findViewById<CheckBox>(R.id.checkBoxFiltro)
    }


}



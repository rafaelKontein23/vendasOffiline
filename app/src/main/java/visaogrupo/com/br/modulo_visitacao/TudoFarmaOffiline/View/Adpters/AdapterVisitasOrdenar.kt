package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas

class AdapterVisitasOrdenar(listaVisitas:MutableList<Visitas>) : Adapter<AdapterVisitasOrdenar.ViewHolderCalendarioRotiro>() {

    var listaVisitas = listaVisitas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCalendarioRotiro {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_visitas_marcadas, parent,false)
        return ViewHolderCalendarioRotiro(view)
    }
    override fun onBindViewHolder(holder: ViewHolderCalendarioRotiro, position: Int) {
         val itemVisita  = listaVisitas[position]


    }

    override fun getItemCount(): Int {
        return listaVisitas.size
    }

    class ViewHolderCalendarioRotiro(itemView: View) :ViewHolder(itemView){

    }


}
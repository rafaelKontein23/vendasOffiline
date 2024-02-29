package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaLetraFiltro

class AdapterFiltroAZ(listaAz: List<Char>, letraFiltro: AtualizaLetraFiltro): RecyclerView.Adapter<AdapterFiltroAZ.ViewHolderFiltroAZ>() {
     val filtroListaAz = listaAz
    val  letraFiltro = letraFiltro

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFiltroAZ {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_a_z,parent,false)
        return  ViewHolderFiltroAZ(view)
    }

    override fun getItemCount(): Int {
       return filtroListaAz.size
    }

    override fun onBindViewHolder(holder: ViewHolderFiltroAZ, position: Int) {

            holder.aZ.text = filtroListaAz[position].toString()
            holder.containerFiltro.setOnClickListener {
                  letraFiltro.letraFiltro(filtroListaAz[position].toString())
            }
     }

    class  ViewHolderFiltroAZ(itemView: View) : RecyclerView.ViewHolder(itemView){
          val aZ = itemView.findViewById<TextView>(R.id.aZ)
          val containerFiltro = itemView.findViewById<ConstraintLayout>(R.id.containerFiltro)

    }
}
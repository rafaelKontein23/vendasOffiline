package visaogrupo.com.br.modulo_visitacao.Views.Adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.modulo_visitacao.R

class ProgressivaAdpter: Adapter<ProgressivaAdpter.ProgressivaViewholder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressivaViewholder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.celula_progressiva,parent,false)

        return ProgressivaViewholder(view)
    }

    override fun onBindViewHolder(holder: ProgressivaViewholder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ProgressivaViewholder(itemView: View) : ViewHolder(itemView){

    }
}
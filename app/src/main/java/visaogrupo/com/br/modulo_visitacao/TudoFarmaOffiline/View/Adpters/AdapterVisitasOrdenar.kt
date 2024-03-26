package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataTexto

class AdapterVisitasOrdenar(listaVisitas:MutableList<Visitas>) : Adapter<AdapterVisitasOrdenar.ViewHolderCalendarioRotiro>() {

    var listaVisitas = listaVisitas
    var selecionar = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCalendarioRotiro {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_visitas_marcadas, parent,false)
        return ViewHolderCalendarioRotiro(view)
    }
    override fun onBindViewHolder(holder: ViewHolderCalendarioRotiro, position: Int) {
         val itemVisita  = listaVisitas[position]
         holder.celulaClienteCNPJ.text = FormataTexto.formataCnpj(itemVisita.cnpj!!)
         holder.enderecoCliente.text = itemVisita.endereco
         holder.textRazaosocialcliente.text = itemVisita.razaoSocial
         if (itemVisita.status == 0){
             holder.imgVista.setImageResource(R.drawable.circulo)
         }else if (itemVisita.status == 1){
             holder.imgVista.setImageResource(R.drawable.check_sucess)

         }else if (itemVisita.status == 2){
             holder.imgVista.setImageResource(R.drawable.erro_x)

         }else if (itemVisita.status == 3){
             holder.imgVista.setImageResource(R.drawable.relogio)

         }
        if (selecionar){
            holder.imgSeleciona.setImageResource(R.drawable.marcar_visita)
            val shakeAnimation = ObjectAnimator.ofFloat(holder.containerVisita, "translationX", -10f, 10f)
            shakeAnimation.duration = 100
            shakeAnimation.repeatCount = 5
            shakeAnimation.repeatMode = ObjectAnimator.REVERSE
            shakeAnimation.start()
        }else{

            holder.imgSeleciona.setImageResource(R.drawable.drag)
        }


    }

    override fun getItemCount(): Int {
        return listaVisitas.size
    }

    class ViewHolderCalendarioRotiro(itemView: View) :ViewHolder(itemView){
        val celulaClienteCNPJ = itemView.findViewById<TextView>(R.id.celulaClienteCNPJ)
        val textRazaosocialcliente = itemView.findViewById<TextView>(R.id.textRazaosocialcliente)
        val enderecoCliente = itemView.findViewById<TextView>(R.id.enderecoCliente)
        val imgVista = itemView.findViewById<ImageView>(R.id.imgVista)
        val imgSeleciona = itemView.findViewById<ImageView>(R.id.imageView8)
        val containerVisita = itemView.findViewById<ConstraintLayout>(R.id.containerVisita)

    }


}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.IniciaAticidadePrincipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Notificacoes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.Trocar_cor_de_icon
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.VerificaTempoDoPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.NotificacaoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActPricipal
import java.time.temporal.ChronoUnit

class AdapterNotificacao(list: MutableList<Notificacoes>, context: Context, iniciaAticidadePrincipal: IniciaAticidadePrincipal): Adapter<AdapterNotificacao.ViewHolderNotificacao>() {
    var listaNotificacao = list
    val context = context
    val iniciaAticidadePrincipal = iniciaAticidadePrincipal
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotificacao {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_notificacao,parent,false)
        return ViewHolderNotificacao(view)
    }
    override fun onBindViewHolder(holder: ViewHolderNotificacao, position: Int) {
        val itemNotificacao =  listaNotificacao [position]
        holder.tituloNotificao.text = itemNotificacao.titulo
        holder.mensagem.text= itemNotificacao.mensagem
        val tempo = separaDatas(itemNotificacao.diaChegada)
        holder.tempo.text = tempo
        holder.visualiza.isVisible = itemNotificacao.visualizado == 1
        if (itemNotificacao.visualizado == 0){
            TrocaCorVisualizado(holder,"#FF004682")

        }else{
            TrocaCorVisualizado(holder,"#9FA3A8")
        }

        holder.visualiza.isVisible = itemNotificacao.visualizado == 0
        holder.containerNotification.setOnClickListener {
             val notificacaoDAO = NotificacaoDAO(context)
            notificacaoDAO.atualizar(itemNotificacao.notificacaoID)
            iniciaAticidadePrincipal.iniciaAtividade()

        }
    }
    override fun getItemCount(): Int {
        return listaNotificacao.size
    }

    class ViewHolderNotificacao(itemView: View) : ViewHolder(itemView){
          val tituloNotificao = itemView.findViewById<TextView>(R.id.tituloNotificao)
          val mensagem =       itemView.findViewById<TextView>(R.id.mensagem)
          val tempo =          itemView.findViewById<TextView>(R.id.tempo)
          val visualiza = itemView.findViewById<View>(R.id.visualiza)
          val containerNotification = itemView.findViewById<ConstraintLayout>(R.id.containerNotification)
          val iconeNotification =  itemView.findViewById<ImageView>(R.id.iconeNotification)

    }

    fun TrocaCorVisualizado(holder:ViewHolderNotificacao,cor:String){
        holder.tituloNotificao.setTextColor(Color.parseColor(cor))
        holder.mensagem.setTextColor(Color.parseColor(cor))
        holder.tempo.setTextColor(Color.parseColor(cor))
        val trocaCorIcon = Trocar_cor_de_icon()
        var icone = holder.iconeNotification.drawable
        trocaCorIcon.trocar_cor_iten(holder.iconeNotification, icone,cor)

    }

    fun separaDatas(dataComponente:String):String{
        val dataHoraString = dataComponente

        val partes = dataHoraString.split(" ")
        val data = partes[0]

        val hora = partes[1]
        val partesData = data.split("/")

        val dia = partesData[0]
        val mes = partesData[1]
        val ano = partesData[2]

        val partesHora = hora.split(":")

        val horaFormatada = partesHora[0]
        val minutos = partesHora[1]
        val verificaTempoDoPedido = VerificaTempoDoPedido()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tempo = verificaTempoDoPedido.verifcaTempoPedido(ano.toInt(),mes.toInt(),dia.toInt(),horaFormatada.toInt(),minutos.toInt())
            return tempo
        }else{
            return ""
        }
    }
}
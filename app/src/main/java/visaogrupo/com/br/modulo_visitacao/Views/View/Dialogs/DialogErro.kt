package visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Funcao_erro
import visaogrupo.com.br.recursos.R

class DialogErro {

    fun Dialog(
            context: Context?,
            titulo: String?,
            mensagem: String?,
            aceitar: String?,
            cancelarr: String?,
            funcao: Funcao_erro
    ) {
            val dialog = android.app.Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog)
            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialoAnimation
            dialog.window!!.setGravity(Gravity.CENTER)
            val Titulo_dialog: TextView
            val mensagem_dialog: TextView
            val confirma: TextView
            val cancelar: TextView
            Titulo_dialog = dialog.findViewById(R.id.Titulo_dialog)
            mensagem_dialog = dialog.findViewById(R.id.mensagem_dialog)
            confirma = dialog.findViewById(R.id.dialog_aceitar)
            cancelar = dialog.findViewById(R.id.Cancelar_diallog)
            Titulo_dialog.text = titulo
            mensagem_dialog.text = mensagem
            confirma.text = aceitar
            cancelar.text = cancelarr
            confirma.setOnClickListener { funcao.ondismisdialog(dialog)
            }
        }

}
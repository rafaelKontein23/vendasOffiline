package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import visaogrupo.com.br.recursos.R

class DialogErro {

    fun Dialog(
            context: Context?,
            titulo: String?,
            mensagem: String?,
            aceitar: String?,
            cancelarr: String?,
            funcaonao: (() -> Unit?)? = null,
            cancel:Boolean = true,
            funcao:() -> Unit,

    ) {
            val dialog = android.app.Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(cancel)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialoAnimation
            dialog.window!!.setGravity(Gravity.CENTER)
            dialog.show()


            val Titulo_dialog: TextView
            val mensagem_dialog: TextView
            val confirma: TextView
            val cancelar: TextView
            Titulo_dialog = dialog.findViewById(R.id.Titulo_dialog)
            mensagem_dialog = dialog.findViewById(R.id.mensagem_dialog)
            confirma = dialog.findViewById(R.id.dialog_aceitar)
            cancelar = dialog.findViewById(R.id.Cancelar_diallog)

            cancelar.setOnClickListener {
                    dialog.dismiss()
                    if (funcaonao != null){
                            funcaonao()
                    }
            }

            Titulo_dialog.text = titulo
            mensagem_dialog.text = mensagem
            confirma.text = aceitar
            cancelar.text = cancelarr
            confirma.setOnClickListener {
                    dialog.dismiss()
                    funcao()
            }
        }

}
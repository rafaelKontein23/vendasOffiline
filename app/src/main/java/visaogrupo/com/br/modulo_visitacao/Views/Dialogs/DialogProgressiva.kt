package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import visaogrupo.com.br.modulo_visitacao.R

class DialogProgressiva {

    fun dialog(context:Context,valorpfatual:Double,nomeremedio:String){
        val  dialog =  Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progressiva);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);

        val editDesconto = dialog.findViewById<EditText>(R.id.editdescontoItem)
        val editquantidade = dialog.findViewById<EditText>(R.id.editquantidadeItem)
        val nomeRemedio = dialog.findViewById<TextView>(R.id.nomedoRemedio)
        val valorpf = dialog.findViewById<TextView>(R.id.valorPf)
        val  descontoeValor = dialog.findViewById<TextView>(R.id.decontEValor)

        valorpf.text = "PF: R$ ${valorpfatual}"
        nomeRemedio.text = nomeremedio




        editDesconto.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()){
                    val porcentagem =  p0.toString().toInt()
                    val  descontonovo = (valorpfatual * porcentagem) / 100
                    val valornovo = valorpfatual - descontonovo
                    descontoeValor.text = porcentagem.toString()+"% - R$ " +String.format("%.2f",valornovo)
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



    }
}
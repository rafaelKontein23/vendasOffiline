package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProgresivaDAO

class DialogProgressiva {

    fun dialog(context:Context, valorpfatual:Double,
               nomeremedio:String, protudoProgressiva: ProdutoProgressiva, AtualizaProgressiva: visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaProgressiva
    ){
        var valornovo = 0.0

        var porcentagem = 0.0
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
        val buttonSalvar = dialog.findViewById<Button>(R.id.buttonSalvar)

        valorpf.text = "PF: R$ ${valorpfatual}"
        nomeRemedio.text = nomeremedio
        buttonSalvar.setOnClickListener {
            val quatidade = editquantidade.text.toString()
            val desconto = editDesconto.text.toString()
            if(quatidade.isEmpty() || quatidade.toInt() ==0){
                editquantidade.requestFocus()
                Toast.makeText(context,"A Quantidade não Pode está vazia",Toast.LENGTH_SHORT).show()
            }else if(desconto.isEmpty() || desconto.toInt() == 0){
                editDesconto.requestFocus()
                Toast.makeText(context,"O Desconto não Pode está vazia",Toast.LENGTH_SHORT).show()
            }else{
                val ProgresivaDAO = ProgresivaDAO(context)
                ProgresivaDAO.insertProgressiva(protudoProgressiva,valornovo, quatidade.toInt(),porcentagem)

                dialog.onBackPressed()
                AtualizaProgressiva.ProgressivaAtualiza(protudoProgressiva.ProdutoCodigo,protudoProgressiva,quatidade.toInt(),valornovo,porcentagem)
            }
        }


        editDesconto.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()){
                    porcentagem =  p0.toString().toDouble()
                    val descontonovo = (valorpfatual * porcentagem) / 100
                    valornovo = valorpfatual - descontonovo
                    descontoeValor.text = porcentagem.toString()+"% - R$ " +String.format("%.2f",valornovo)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })



    }
}
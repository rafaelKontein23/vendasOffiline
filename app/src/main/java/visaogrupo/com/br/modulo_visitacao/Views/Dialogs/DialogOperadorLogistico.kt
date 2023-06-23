package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaSelecionada
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.OperadorLogisticaDAO

class DialogOperadorLogistico {


    fun dialog(context:Context,lojaSelecionada: Lojas,clientes: Clientes){
        val  dialog =  Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_operador_logistico);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);

        val  operadorspiner = dialog.findViewById<Spinner>(R.id.oplspiner)
        val formspner = dialog.findViewById<Spinner>(R.id.formaDepagamentospiner)

        CoroutineScope(Dispatchers.IO).launch {
            val  listaFormPag:MutableList<String> = mutableListOf()
            val listaOplspner :MutableList<String> = mutableListOf()
            val job1 = launch {
                val  operadorLogisticaDAO = OperadorLogisticaDAO(context)
                val listaOpl = operadorLogisticaDAO.listar(clientes.UF,lojaSelecionada.loja_id)

                listaOplspner.add("Selecione")
                for (i in 0 until listaOpl.size){
                    listaOplspner.add(listaOpl[i].Nome)
                }

            }
            val job2 = launch {
                val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
                val listaFormaDePagemento = formaDePagamentoDAO.listar(lojaSelecionada.loja_id)
                listaFormPag.add("Selecione")
                for (i in 0 until listaFormaDePagemento.size){
                    listaFormPag.add(listaFormaDePagemento[i].FormaPgto)
                }

            }
            job1.join()
            job2.join()

            MainScope().launch {
                val adapterPag = ArrayAdapter(context,android.R.layout.simple_spinner_item,listaFormPag)
                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, listaOplspner)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapterPag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                formspner.adapter = adapterPag
                operadorspiner.adapter = adapter
            }
        }
    }
}
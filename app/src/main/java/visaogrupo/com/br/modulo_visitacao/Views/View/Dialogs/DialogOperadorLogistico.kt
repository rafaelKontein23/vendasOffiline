package visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.taskEnviaPedido
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.OperadorLogisticaDAO

class DialogOperadorLogistico {


    @RequiresApi(Build.VERSION_CODES.O)
    fun dialog(context:Context, list: MutableList<Carrinho>){
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
        val  buton = dialog.findViewById<Button>(R.id.Enviar_Pedido)
        val  observacao = dialog.findViewById<EditText>(R.id.observacao)

        buton.setOnClickListener {
            val oplLogisticocap = operadorspiner.selectedItem.toString()
            val formadepagamentocap = formspner.selectedItem.toString()

            val  verificaInternet = Verifica_Internet()
            val isInternet = verificaInternet.isOnline(context)
            if(isInternet){
                if (oplLogisticocap.toLowerCase() != "selecione" && formadepagamentocap.toLowerCase() != "selecione"){
                    val observacaocap = observacao.text.toString()
                    val taskEnviaPedido = taskEnviaPedido()
                    CoroutineScope(Dispatchers.IO).launch {
                        taskEnviaPedido.eviarPedido(list,context,oplLogisticocap,observacaocap,formadepagamentocap)
                    }
                }else{
                    Toast.makeText(context,"Verifique os campos " , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"Você precisa ter acesso a internet para prosseguir com essa ação" , Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val  listaFormPag:MutableList<String> = mutableListOf()
            val listaOplspner :MutableList<String> = mutableListOf()
            // busca opl
            val job1 = launch {
                val  operadorLogisticaDAO = OperadorLogisticaDAO(context)
                val listaOpl = operadorLogisticaDAO.listar(ActPricipal.clienteUF,ActPricipal.loja_id)

                listaOplspner.add("Selecione")
                for (i in 0 until listaOpl.size){
                    listaOplspner.add(listaOpl[i].Nome)
                }

            }
            // busca forma de pagamento
            val job2 = launch {
                val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
                val listaFormaDePagemento = formaDePagamentoDAO.listar(ActPricipal.loja_id)
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
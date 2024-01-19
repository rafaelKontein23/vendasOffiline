package visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaOperadorLogistico
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.PedidoSucesso
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CustomSpinerFormDePag
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.OperadorLogistico
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.ExcluirPrefuser
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.HoraAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.PushNativo
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.RecuperaDataAtual
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.FormaDePagamentoDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.OperadorLogisticaDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.taskEnviaPedido
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdapterFormDePagSpiner
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.OperadorAdpter

class DialogOperadorLogistico (context:Context){
     var oplId = mutableListOf<String>()
    val  dialog =  Dialog(context);

    @RequiresApi(Build.VERSION_CODES.O)
    fun dialog( context:Context,list: MutableList<Carrinho>, valorTotal:Double){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_operador_logistico);
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
        dialog.show();


        val  recyclerOpl = dialog.findViewById<RecyclerView>(R.id.recyclerOpl)
        val formspner = dialog.findViewById<Spinner>(R.id.formaDepagamentospiner)
        val fecharOpl = dialog.findViewById<ImageView>(R.id.fecharOpl)


        val  buton = dialog.findViewById<Button>(R.id.Enviar_Pedido)
        val  observacao = dialog.findViewById<EditText>(R.id.observacao)
        val  tituloAr = dialog.findViewById<TextView>(R.id.tituloAr)
        val  Description = dialog.findViewById<TextView>(R.id.Description)
        val  NunerodoPedido = dialog.findViewById<EditText>(R.id.NunerodoPedido)


        observacao.isVisible = list[0].Anr == 1
        tituloAr.isVisible = list[0].Anr == 1


        Description.text = "Selecione até ${list[0].QtdMaxima_Operador} para atender seu pedido"

        buton.setOnClickListener {
            val formadepagamentocap = formspner.selectedItem.toString()

            buton.isEnabled = false
            val numeroPedio = NunerodoPedido.text.toString()

            if (!oplId.isEmpty() && !formadepagamentocap.contains("Selecionar")){
                val observacaocap = observacao.text.toString()

                val formaDePagmento = infoFormaDePagamento(context,list[0].valortotal,list[0].RegraPrazo,list[0].cnpj,formadepagamentocap)
                val pedidoFinalizado = PedidosFinalizadosDAO(context)
                val data = RecuperaDataAtual.dataAtual()
                val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val gsonuserid = Gson()
                val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
                val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
                var chave = login.Usuario_id + list[0]?.clienteId +data
                chave = chave.replace(":","").replace("/","").replace(" ","")
                PushNativo.showNotificationPedido(context,"TESTE1","Pedido Salvo ","Seu pedido foi salvo com sucesso\n" +
                        "Data do Pedido: ${data} | ${HoraAtual.horaAtual()}")
                if (formaDePagmento != null) {
                    pedidoFinalizado.insert(
                        list[0],
                        data,
                        HoraAtual.horaAtual(),
                        chave,
                        list,
                        numeroPedio,
                        oplId.toString(),
                        observacaocap,
                        formaDePagmento,
                        list[0].RegraPrazo,
                        list[0].QtdMaxima_Operador
                    )
                }
                val carrinhoDao = CarrinhoDAO(context)
                carrinhoDao.excluirItemCarrinho(list[0].clienteId,list[0].lojaId)
                ExcluirPrefuser.excluirItemPref(context,"LojaSelecionada")
                ExcluirPrefuser.excluirItemPref(context,"ClienteSelecionado")

                dialog.dismiss()
                val  intent = Intent(context,ActPricipal::class.java)
                intent.putExtra("Tela","pedido")
                context.startActivity(intent)





            }else{
                if (oplId.isEmpty() && formadepagamentocap.contains("Selecionar") ){
                    Toast.makeText(context,"Selecione o operador logístico, Selecione uma forma de pagamento " , Toast.LENGTH_SHORT).show()
                }else if (oplId.isEmpty()){
                    Toast.makeText(context,"Selecione o operador logístico" , Toast.LENGTH_SHORT).show()

                }else if (formadepagamentocap.contains("Selecionar")){
                    Toast.makeText(context,"Selecione uma forma de pagamento" , Toast.LENGTH_SHORT).show()

                }

            }

            buton.isEnabled = true
        }

        fecharOpl.setOnClickListener{
            dialog.dismiss()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val  listaFormPag = mutableListOf<CustomSpinerFormDePag>()
            val listaOplspner :ArrayList<OperadorLogistico> = ArrayList()
            // busca opl
            val buscaOpl = launch {
                val  operadorLogisticaDAO = OperadorLogisticaDAO(context)
                val listaOpl = operadorLogisticaDAO.listar(ActPricipal.clienteUF,ActPricipal.loja_id)

                for (i in 0 until listaOpl.size){
                    listaOplspner.add(listaOpl[i])
                }

            }
            // busca forma de pagamento
            val buscaFormaDePag = launch {
                val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
                val listaFormaDePagemento = formaDePagamentoDAO.listar(ActPricipal.loja_id, valorTotal, list[0].RegraPrazo, list[0].cnpj)
                listaFormPag.add(0,CustomSpinerFormDePag("Selecionar",0))
                for (i in 0 until listaFormaDePagemento.size){
                    listaFormPag.add(CustomSpinerFormDePag(listaFormaDePagemento[i].FormaPgto,listaFormaDePagemento[i].exlusiva))
                }


            }
            buscaOpl.join()
            buscaFormaDePag.join()

            MainScope().launch {
                val adapterForm = AdapterFormDePagSpiner(context,1, listaFormPag)

                val oplAdapter  = OperadorAdpter(listaOplspner,list[0].QtdMaxima_Operador,context, oplId  )
                val layoutMeneger = GridLayoutManager(context,2)
                recyclerOpl.layoutManager = layoutMeneger
                recyclerOpl.adapter= oplAdapter

                formspner.adapter = adapterForm
            }
        }
    }


    }

 fun infoFormaDePagamento(context:Context, valorToatalPedido: Double, RegraPrazo:Int, cnpj:String, formaDepagaemntoSelecionada:String ): FormaDePagaemnto? {
        var formaDePagaemnto: FormaDePagaemnto? = null

        val  formaDePagamentoDAO = FormaDePagamentoDAO(context)
        val listaFormaDePagemento = formaDePagamentoDAO.listar(ActPricipal.loja_id, valorToatalPedido, RegraPrazo, cnpj)

        for (i in listaFormaDePagemento){
              if (formaDepagaemntoSelecionada.contains(i.FormaPgto)){
                  formaDePagaemnto = i
                  break
              }
        }

    return formaDePagaemnto
}

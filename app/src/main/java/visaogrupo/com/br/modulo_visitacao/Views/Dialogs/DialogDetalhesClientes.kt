package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Act_Pricipal
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Enuns_Cadastro.TrocaItemSelecionado
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.MudarFragment
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes

class DialogDetalhesClientes {


       fun dialogDetalhe(context:Context,cnpjformat:String, cliente:Clientes,frameid:Int,supportFragmentManager:FragmentManager,trocarcorItem: TrocarcorItem){
           val  dialog =  Dialog(context);

           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           dialog.setContentView(R.layout.dialogdetalhecliente);

           dialog.show();
           dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
           dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
           dialog.getWindow()?.setGravity(Gravity.BOTTOM);
           val cnpj =  dialog.findViewById<TextView>(R.id.cnpjdetalhe)
           val razaoSocial =  dialog.findViewById<TextView>(R.id.razaodetalhe)
           val endereco =  dialog.findViewById<TextView>(R.id.enderecodetalhe)
           val Telefone =  dialog.findViewById<TextView>(R.id.telefonecelular)
           val vender = dialog.findViewById<TextView>(R.id.vender)
           vender.setOnClickListener {
               dialog.onBackPressed()

               Act_Pricipal.troca = TrocaItemSelecionado.lojas
               trocarcorItem.trocacor()
               val mudarFragment = MudarFragment()
               mudarFragment.openFragmentWithFragmentManager(supportFragmentManager,frameid)

           }
           cnpj.text = cnpjformat
           razaoSocial.text = cliente.RazaoSocial
           endereco.text = "${cliente.Endereco}, ${cliente.Numero} ${cliente.Cidade},${cliente.Bairro}, ${cliente.UF}"
           Telefone.text = cliente.Telefone



       }
}
package visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Enuns.TrocaItemSelecionado

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.MudarFragment
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActPricipal


class DialogDetalhesClientes {


       fun dialogDetalhe(context:Context, cnpjformat:String,
                         cliente: Clientes, frameid:Int,
                         supportFragmentManager:FragmentManager,
                         trocarcorItem: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TrocarcorItem,
                         carrinhoVisible: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible, atualizaCarrinho: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
       ){
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
           val  xdetalhe = dialog.findViewById<ImageView>(R.id.xdetalhe)
           val email = dialog.findViewById<TextView>(R.id.email)
           val maps= dialog.findViewById<ImageView>(R.id.maps)

           email.setOnClickListener {
               val subject = "Comprar remedios"
               val body = "Olá, como posso tirar uma duvida?"
               val intent = Intent(Intent.ACTION_SENDTO)
               intent.data = Uri.parse("mailto:")

               intent.putExtra(
                   Intent.EXTRA_EMAIL,
                   arrayOf<String>(email.text.toString())
               )
               intent.putExtra(Intent.EXTRA_SUBJECT, subject)
               intent.putExtra(Intent.EXTRA_TEXT, body)
               context.startActivity(Intent.createChooser(intent, "Enviar email"))
           }

           Telefone.setOnClickListener {
               val intent = Intent(Intent.ACTION_DIAL)
               intent.data = Uri.parse("tel:" + Telefone.text.toString())

               context.startActivity(intent)

           }

           maps.setOnClickListener {
               val address = endereco.text.toString()
               val uri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
               val mapIntent = Intent(Intent.ACTION_VIEW, uri)
               mapIntent.setPackage("com.google.android.apps.maps")
               context.startActivity(mapIntent)

           }

           xdetalhe.setOnClickListener {
               dialog.onBackPressed()
           }
           vender.setOnClickListener {
               dialog.onBackPressed()

               ActPricipal.troca = TrocaItemSelecionado.lojas
               trocarcorItem.trocacor()
               val mudarFragment = MudarFragment()
               mudarFragment.openFragmentLojas(supportFragmentManager,frameid,trocarcorItem, carrinhoVisible, atualizaCarrinho )

           }
           cnpj.text = cnpjformat
           razaoSocial.text = cliente.RazaoSocial
           endereco.text = "${cliente.Endereco}, ${cliente.Numero} ${cliente.Cidade},${cliente.Bairro}, ${cliente.UF}"
           Telefone.text = cliente.Email
           email.text = cliente.Telefone
       }
}
package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Retrofit_Request.URLs

class DialogMudarAmbienteSenha {

    fun dialogSenha(context:Context){
        val  dialog =  Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_senha);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);

        val xsenha = dialog.findViewById<ImageView>(R.id.xsenha)
        val butoonsenha = dialog.findViewById<Button>(R.id.buttonSenha)
        val edisenha = dialog.findViewById<EditText>(R.id.editsenha)

        butoonsenha.setOnClickListener {
            val capSenha= edisenha.text.toString()

            if(capSenha.equals("catarinense1234")){
                Trocar_Ambiente(context,dialog)
            }else{
                Toast.makeText(context,"Senha incorreta",Toast.LENGTH_SHORT).show()
            }
        }
        xsenha.setOnClickListener {
            dialog.onBackPressed()
        }
    }


    fun Trocar_Ambiente(context:Context, dialogsenha:Dialog){
        val  dialog =  Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ambiente);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);

        val  butone = dialog.findViewById<Button>(R.id.buttonE)
        val  butonqa = dialog.findViewById<Button>(R.id.buttonqa)
        val  butonar = dialog.findViewById<Button>(R.id.buttonar)

        butone.setOnClickListener {
            URLs.urlCarga = "https://wwwe.catarinenseonline.com.br/"
            URLs.url = "https://wwwe.visaogrupo.com.br/ws/"
            URLs.urlEnviaPedido = "https://wwwe.visaogrupo.com.br/ws/catarinense/envio/pedido"
            URLs.urlEstoque ="https://catarinenseonline.com.br/Docs/Tablet/Carga/estoque/estoque_"
            URLs.urlimagens= "https://catarinenseonline.com.br/ImagensEan/"
            URLs.urlProgressiva = "https://wwwi.catarinenseonline.com.br/Progressivas/Progressiva_"
            URLs.urlportal = "https://wwwi.catarinenseonline.com.br/autenticacao/AcessoTablet=?l="
            dialog.onBackPressed()
            dialogsenha.onBackPressed()
            Toast.makeText(context,"Estamos no WWWE",Toast.LENGTH_SHORT).show()
        }
        butonqa.setOnClickListener {
            URLs.urlCarga = "https://wwwe.catarinenseonline.com.br/"
            URLs.url = "https://wwwe.visaogrupo.com.br/ws/"
            URLs.urlEnviaPedido = "https://qa.visaogrupo.com.br/ws/catarinense/envio/pedido"
            URLs.urlEstoque ="https://catarinenseonline.com.br/Docs/Tablet/Carga/estoque/estoque_"
            URLs.urlimagens= "https://catarinenseonline.com.br/ImagensEan/"
            URLs.urlProgressiva = "https://qa.catarinenseonline.com.br/Progressivas/Progressiva_"
            URLs.urlportal = "https://qa.catarinenseonline.com.br/autenticacao/AcessoTablet=?l="
            dialog.onBackPressed()
            dialogsenha.onBackPressed()
            Toast.makeText(context,"Estamos no qa",Toast.LENGTH_SHORT).show()
        }
        butonar.setOnClickListener {
            URLs.urlCarga = "https://www.catarinenseonline.com.br/"
            URLs.url = "https://www.visaogrupo.com.br/ws/"
            URLs.urlEnviaPedido = "https://www.visaogrupo.com.br/ws/catarinense/envio/pedido"
            URLs.urlEstoque ="https://catarinenseonline.com.br/Docs/Tablet/Carga/estoque/estoque_"
            URLs.urlimagens= "https://catarinenseonline.com.br/ImagensEan/"
            URLs.urlProgressiva = "https://www.catarinenseonline.com.br/Progressivas/Progressiva_"
            URLs.urlportal = "https://www.catarinenseonline.com.br/autenticacao/AcessoTablet=?l="
            dialog.onBackPressed()
            dialogsenha.onBackPressed()
            Toast.makeText(context,"Estamos no AR",Toast.LENGTH_SHORT).show()
        }
    }
}
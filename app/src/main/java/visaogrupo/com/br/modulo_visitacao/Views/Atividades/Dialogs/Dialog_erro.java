package visaogrupo.com.br.modulo_visitacao.Views.Atividades.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import visaogrupo.com.br.modulo_visitacao.R;

public class Dialog_erro {

    public void  Dialog(Context context,String titulo,String mensagem,String aceitar,String cancelarr){
        final Dialog  dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView Titulo_dialog,mensagem_dialog,confirma,cancelar;

        Titulo_dialog = dialog.findViewById(R.id.Titulo_dialog);
        mensagem_dialog = dialog.findViewById(R.id.mensagem_dialog);
        confirma = dialog.findViewById(R.id.dialog_aceitar);
        cancelar = dialog.findViewById(R.id.Cancelar_diallog);

        Titulo_dialog.setText(titulo);
        mensagem_dialog.setText(mensagem);
        confirma.setText(aceitar);
        cancelar.setText(cancelarr);

        confirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.onBackPressed();
            }
        });




    }
}

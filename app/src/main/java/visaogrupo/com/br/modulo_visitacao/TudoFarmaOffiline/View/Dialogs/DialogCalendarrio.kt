package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.DataPikerData
import java.util.Calendar

class DialogCalendarrio: BottomSheetDialogFragment() {

    fun dialogCalendario(context:Context,dataPikerData: DataPikerData){
        val  dialog =  Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendario);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.setGravity(Gravity.CENTER);

        val fecharCalender = dialog.findViewById<ImageView>(R.id.fecharCalender)
        val datePicker     = dialog.findViewById<DatePicker>(R.id.datePicker)
        val selecionar     = dialog.findViewById<TextView>(R.id.selecionar)

        fecharCalender.setOnClickListener {
            dialog.dismiss()
        }

        val calendar = Calendar.getInstance()
        datePicker.minDate = calendar.timeInMillis

        selecionar.setOnClickListener {
            var data = "${datePicker.dayOfMonth}/${datePicker.month+1}/${datePicker.year}"
            dialog.dismiss()
            dataPikerData.dataPiker(data)
        }

    }
}


package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.widget.EditText
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher


class MascaraCampo {
    companion object {
        fun mascaraEdit(mascara:String,edtText:EditText){
            val simpleMaskFormatter=  SimpleMaskFormatter(mascara);
            val mwt =  MaskTextWatcher(edtText,simpleMaskFormatter);
            edtText.addTextChangedListener(mwt);

        }


    }

}
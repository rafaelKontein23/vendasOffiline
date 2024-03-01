package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.content.Context

class FezCargaPreferencias {

    companion object{
        fun atualizaInfoDeCarga(context:Context, fezCarga:Boolean){
            val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean("cargafeita", fezCarga)
            editor?.apply()
        }
    }
}
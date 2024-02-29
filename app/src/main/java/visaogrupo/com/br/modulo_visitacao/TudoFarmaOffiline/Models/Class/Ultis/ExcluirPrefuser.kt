package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.content.Context

class ExcluirPrefuser {
     companion object {
         fun excluirItemPref(context: Context, chave: String) {
             val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
             val editor = prefs.edit()
             editor.remove(chave) // Remove o item associado à chave fornecida
             editor.apply() // Aplica as mudanças
         }
     }

}
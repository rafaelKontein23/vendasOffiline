package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas

interface ILojas {

    fun insert (jsonlojas: JSONArray):Boolean

    fun atualizar (lojas: Lojas):Boolean

    fun remover (lojas: Lojas):Boolean

    fun listarlojas (context: Context,empresaID:Int,query:String):List<Lojas>
}
package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes

interface IClientes {

    fun insert (jsoncCientes: JSONArray):Boolean

    fun atualizar (Clientes: Clientes):Boolean

    fun remover (Clientes: Clientes):Boolean

    fun listar (context: Context,query:String):MutableList<Clientes>
    fun countar(context: Context):Int

}
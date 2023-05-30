package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

interface IClientes {

    fun insert (jsoncCientes: JSONArray):Boolean

    fun atualizar (Clientes: Lojas):Boolean

    fun remover (Clientes: Lojas):Boolean
}
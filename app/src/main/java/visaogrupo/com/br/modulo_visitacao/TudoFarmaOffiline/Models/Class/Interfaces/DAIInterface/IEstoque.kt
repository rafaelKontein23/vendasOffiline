package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Estoque

interface IEstoque {

    fun insert (jsonEstoque: JSONArray, lojaID:Int)

    fun atualizar (FormaDePagaemnto: Estoque):Boolean

    fun remover (FormaDePagaemnto: Estoque):Boolean
}
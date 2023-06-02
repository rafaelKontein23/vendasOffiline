package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Estoque
import visaogrupo.com.br.modulo_visitacao.Views.Models.FormaDePagaemnto

interface IEstoque {

    fun insert (jsonEstoque: JSONArray, lojaID:Int)

    fun atualizar (FormaDePagaemnto: Estoque):Boolean

    fun remover (FormaDePagaemnto: Estoque):Boolean
}
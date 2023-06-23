package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

interface IFormaDePagamento {

    fun insert (jsonFormaDePagamento: JSONArray,lojaID:Int)

    fun atualizar (FormaDePagaemnto: FormaDePagaemnto):Boolean

    fun listar (loja_id:Int):MutableList<FormaDePagaemnto>
}
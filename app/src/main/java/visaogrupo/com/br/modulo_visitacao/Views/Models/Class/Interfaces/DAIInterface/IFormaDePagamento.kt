package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto

interface IFormaDePagamento {

    fun insert (jsonFormaDePagamento: JSONArray,lojaID:Int)

    fun atualizar (FormaDePagaemnto: FormaDePagaemnto):Boolean

    fun listar (loja_id:Int):MutableList<FormaDePagaemnto>
}
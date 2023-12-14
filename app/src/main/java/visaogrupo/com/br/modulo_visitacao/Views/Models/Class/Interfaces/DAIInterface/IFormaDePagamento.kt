package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas

interface IFormaDePagamento {

    fun insert (jsonFormaDePagamento: JSONArray,lojaID:Int)

    fun atualizar (FormaDePagaemnto: FormaDePagaemnto):Boolean

    fun listar (loja_id:Int, valorToatalPedido:Double, RegraSelecionada: Int,cnpj:String
    ):MutableList<FormaDePagaemnto>
}
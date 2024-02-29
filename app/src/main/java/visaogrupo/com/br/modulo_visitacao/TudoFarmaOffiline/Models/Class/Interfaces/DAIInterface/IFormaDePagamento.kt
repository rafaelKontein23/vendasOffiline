package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FormaDePagaemnto

interface IFormaDePagamento {

    fun insert (jsonFormaDePagamento: JSONArray,lojaID:Int)

    fun atualizar (FormaDePagaemnto: FormaDePagaemnto):Boolean

    fun listar (loja_id:Int, valorToatalPedido:Double, RegraSelecionada: Int,cnpj:String
    ):MutableList<FormaDePagaemnto>
}
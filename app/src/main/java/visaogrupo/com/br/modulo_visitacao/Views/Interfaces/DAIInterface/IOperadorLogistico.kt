package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.FormaDePagaemnto
import visaogrupo.com.br.modulo_visitacao.Views.Models.OperadorLogistico

interface IOperadorLogistico {

    fun insert (jsonFormaDePagamento: JSONArray, lojaID:Int, uf: String)

    fun atualizar (OperadorLogistico: OperadorLogistico):Boolean

    fun remover (OperadorLogistico: OperadorLogistico):Boolean
}
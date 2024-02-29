package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.OperadorLogistico

interface IOperadorLogistico {

    fun insert (jsonFormaDePagamento: JSONArray, lojaID:Int, uf: String)

    fun atualizar (OperadorLogistico: OperadorLogistico):Boolean

    fun remover (OperadorLogistico: OperadorLogistico):Boolean
    fun listar (uf: String,loja_id:Int):MutableList<OperadorLogistico>
}
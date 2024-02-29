package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Produtos

interface IProtudos {

    fun insert (jsonProtudos: JSONArray):Boolean

    fun atualizar (produtos: Produtos):Boolean

    fun remover (produtos: Produtos):Boolean

    fun  litar(query: String):MutableList<ProdutoProgressiva>
}
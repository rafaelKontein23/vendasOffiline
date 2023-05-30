package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Produtos

interface IProtudos {

    fun insert (jsonProtudos: JSONArray):Boolean

    fun atualizar (produtos: Produtos):Boolean

    fun remover (produtos:Produtos):Boolean
}
package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IEstoque
import visaogrupo.com.br.modulo_visitacao.Views.Models.Estoque

class EstoqueDAO:IEstoque {
    override fun insert(jsonEstoque: JSONArray, lojaID: Int) {

    }

    override fun atualizar(FormaDePagaemnto: Estoque): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(FormaDePagaemnto: Estoque): Boolean {
        TODO("Not yet implemented")
    }
}
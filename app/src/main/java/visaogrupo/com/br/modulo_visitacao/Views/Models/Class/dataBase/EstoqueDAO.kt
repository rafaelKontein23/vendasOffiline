package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Estoque

class EstoqueDAO:
    visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.IEstoque {
    override fun insert(jsonEstoque: JSONArray, lojaID: Int) {

    }

    override fun atualizar(FormaDePagaemnto: Estoque): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(FormaDePagaemnto: Estoque): Boolean {
        TODO("Not yet implemented")
    }
}
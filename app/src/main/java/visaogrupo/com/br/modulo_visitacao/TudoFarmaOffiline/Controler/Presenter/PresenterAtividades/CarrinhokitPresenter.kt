package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.Context
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoKitDAO

class CarrinhokitPresenter {

    fun buscaItensCarrinhoKit(context: Context):MutableList<CarrinhoKit>{
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
        val queryBuscaItensCarrinhoKit = "SELECT * FROM TB_CarrinhoKit WHERE loja_id =${lojaSelecionada.loja_id} AND cliente_id =${clienteSelecionado.Empresa_id} "
        val carrinhoKitDAO = CarrinhoKitDAO(context)
        val listaCarrinhoKit = carrinhoKitDAO.buscaCarrinhoKit(queryBuscaItensCarrinhoKit)

        for((index,itemProtudo) in listaCarrinhoKit.withIndex()){
            val  queryItemCarrinhoKit = "SELECT ProdutoKit.*, imagens.imagembase64\n" +
                    "FROM TB_carrinhoProdutoKit ProdutoKit\n" +
                    "LEFT JOIN TB_Imagens imagens ON ProdutoKit.barra = imagens.barra\n" +
                    "WHERE ProdutoKit.loja_id = ${lojaSelecionada.loja_id} AND ProdutoKit.cliente_id = ${clienteSelecionado.Empresa_id} AND ProdutoKit.kitcodigo = ${itemProtudo.kitCodigo};\n"
            val listaKitProdutos = carrinhoKitDAO.buscaItensProdutoCarrinhoKit(queryItemCarrinhoKit,itemProtudo.kitCodigo)
            itemProtudo.listProdutoKit = listaKitProdutos
        }
        return listaCarrinhoKit
    }
}
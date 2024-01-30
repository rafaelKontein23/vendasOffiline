package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos

data  class KitTituloPreco (
    val titulo:String,
    val kitId:Int,
    var De:Double,
    var Por:Double,
    var listaKitProdutos:MutableList<KitProtudos>? = null,
    var estaNoCarrinho:Int,
    var quantidade:Int
)
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos

data class ProdutoAB(
    val Apresentacao: String,
    val Barra: String,
    val CaixaPadrao: Int,
    val Imagem: String,
    val ListaICMS: String,
    val Nome: String,
    val PrincipioAtivo: String,
    val Produto_codigo: Int,
    val Referencia: String,
    val CODLISTAPRECOSYNC: Int,
    val Desconto: Double,
    val Grupo_Codigo: Int,
    val Loja_id: Int,
    val PF: Double,
    val PMC: Double,
    val Porc: Int,
    val Prod_cod: Int,
    val QtdMax: Int,
    val QtdMin: Int,
    var Quantidade: Int,
    val UF: String,
    val grupoCodigo:Int,
    var valorTotal:Double = 0.0,
    var estaNoCarrinho:Int,
    var quantidadeCarrinho:Int
)

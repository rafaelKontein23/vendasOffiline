package visaogrupo.com.br.modulo_visitacao.Views.Models

data class Carrinho(
    val lojaId: Int,
    val clienteId: Int,
    val produtoCodigo: Int,
    val operadorLogistigo: String,
    val usuarioId: Int,
    val uf: String,
    val comissao: Double,
    val comissaoPorcentagem: Double,
    val marcasXComissoesId: Int,
    val barra: String,
    val quantidade: Int,
    val pf: Double,
    val valor: Double,
    val valorOriginal: Double,
    val grupoCodigo: Int,
    val desconto: Double,
    val descontoOriginal: Double,
    val st: Double,
    val formalizacao: String,
    val codListaPrecoSync: Int,
    val apontadorCodigo: String,
    val valortotal:Double,
    val nomeProduto:String
)

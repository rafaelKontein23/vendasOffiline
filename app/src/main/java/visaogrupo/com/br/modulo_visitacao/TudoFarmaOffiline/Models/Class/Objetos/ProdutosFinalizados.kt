package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class ProdutosFinalizados (
    val barra: String,
    val produtoCodigo: Int?,
    val desconto: Double,
    val descontoOriginal: Double,
    val formalizacao: String?,
    val pf: Double,
    val quantidade: Int,
    val valorRepasse: Double,
    val st: String?,
    val valor: Double,
    val  nomeProduto:String
)
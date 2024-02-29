package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class KitProtudos(
    val kitID:Int,
    val produtoCodigo: String,
    val produtoNome: String,
    val fabricante: String,
    val desconto: Double,
    val quantidade: Int,
    val imagem: String,
    val valorTotal:Double,
    val barra:String,
    val imgBase64:String?
)

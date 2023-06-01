package visaogrupo.com.br.modulo_visitacao.Views.Models

data class Progressiva(
    val Data_Vencimento: String,
    val Desconto: Double,
    val DescontoMaximo: Double,
    val Desconto_Min: Double,
    val Loja_id: Int,
    val PF: Double,
    val PMC: Double,
    val Prioridade: Boolean,
    val Prod_cod: Int,
    val Promocao: Boolean,
    val Quantidade: Int,
    val QuantidadeMaxima: Int,
    val ST: Double,
    val Seq_Cond_Coml: Int,
    val Seq_Kit: Int,
    val UF: String,
    val Valor: Double,
    val formalizacao: String
)
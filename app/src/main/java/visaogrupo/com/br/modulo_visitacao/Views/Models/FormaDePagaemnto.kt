package visaogrupo.com.br.modulo_visitacao.Views.Models

data class FormaDePagaemnto(
    val Alternativa: Int,
    val Cod_FormaPgto: String,
    val FormaPgto: String,
    val ValorMinimo: Double,
    val loja: Int
)
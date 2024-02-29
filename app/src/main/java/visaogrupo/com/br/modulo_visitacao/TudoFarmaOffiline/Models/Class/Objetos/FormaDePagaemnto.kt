package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class FormaDePagaemnto(
    val Alternativa: Int,
    val Cod_FormaPgto: String,
    val FormaPgto: String,
    val ValorMinimo: Double,
    val loja: Int,
    val  PrazoMedio:Int,
    val exlusiva:Int
)
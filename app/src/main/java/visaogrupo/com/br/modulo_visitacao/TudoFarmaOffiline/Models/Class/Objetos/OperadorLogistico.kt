package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class OperadorLogistico(
    val Estado: String,
    val Grupo: String,
    val Loja_id: Int,
    val MinimoValor: Double,
    val Nome: String,
    val OperadorLogistico_Grupo_id: Int,
    val OperadorLogistico_ID: Int,
    var selecionado: Boolean = false,
    var posicao :Int  = -1
)
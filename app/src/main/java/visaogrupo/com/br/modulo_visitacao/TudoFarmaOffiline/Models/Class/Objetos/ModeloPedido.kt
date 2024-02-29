package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class ModeloPedido(  val codigoUsuario: Int,
                           val nomeModeloPedido: String,
                           val clienteId: Int,
                           val codigoLoja: Int,
                           val codigoProduto: Int,
                           val quantidade: Int,
                           val codigoProgressiva: Int,
                           val desconto: Double)

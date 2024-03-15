package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class Notificacoes(  val notificacaoID:Int,
                         val titulo: String,
                         val mensagem: String,
                         val tipo: Int,
                         val json: String,
                         val pedidoID: Long,
                         val visualizado:Int,
                         val diaChegada:String)

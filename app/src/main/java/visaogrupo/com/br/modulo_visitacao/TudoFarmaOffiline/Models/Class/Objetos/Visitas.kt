package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class Visitas( val visitaID: Int? = null,
                    val cnpj: String? = null,
                    val razaoSocial: String? = null,
                    val endereco: String? = null,
                    val telefone: String? = null,
                    val email: String? = null,
                    val dataVisita: String? = null,
                    val ordem: Int? = null,
                    val status :Int = 0,
                    var selcionado:Boolean = false)

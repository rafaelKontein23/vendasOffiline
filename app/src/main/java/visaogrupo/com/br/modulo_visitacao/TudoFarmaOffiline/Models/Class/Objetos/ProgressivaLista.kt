package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class ProgressivaLista(val produtoCodigo: Int,
                            val caixaPadrao: Int,
                            val pmc: Double,
                            val pf: Double,
                            var valor: Double,
                            val quantidade: Int,
                            var desconto: Double,
                            var personalizada:Boolean,
                            var isSelected: Boolean = false,
                            val DescontoMaximo:Double,
                            var ProgressivaSelecionad:Boolean = false
                        )

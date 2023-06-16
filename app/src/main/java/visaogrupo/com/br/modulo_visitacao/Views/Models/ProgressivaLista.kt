package visaogrupo.com.br.modulo_visitacao.Views.Models

data class ProgressivaLista(val produtoCodigo: Int,
                            val caixaPadrao: Int,
                            val pmc: Double,
                            val pf: Double,
                            val valor: Double,
                            val quantidade: Int,
                            val desconto: Double,
                            var personalizada:Boolean,
                            var isSelected: Boolean = false)

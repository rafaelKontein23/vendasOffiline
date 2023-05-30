package visaogrupo.com.br.modulo_visitacao.Views.Models

data class Lojas(
    val ANR: Boolean,
    val DataFim: String,
    val DataInicio: String,
    val DescontoMaximo: Double,
    val Distribuidora: Int,
    val LiberaCotacao: Boolean,
    val LiberaFidelidade: Boolean,
    val LogoHome: String,
    val LojaTablet: Boolean,
    val LojaTipo: Int,
    val Loja_id: Int,
    val MinimoUnidades: Int,
    val MinimoValor: Double,
    val Nome: String,
    val Ordem: Int,
    val Portfolio: String,
    val QtdProdutosLoja: Int,
    val TipoImposto: String,
    val TipoImposto_ID: Int,
    val Unificada: Int,
    val ValidaEstoque: Boolean,
    val ValorUnificada: Double,
    val VendaTipo_id: Int,
    val tipo: String
)
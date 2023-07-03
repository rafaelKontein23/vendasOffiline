package visaogrupo.com.br.modulo_visitacao.Views.Models

import java.util.Base64

data class Carrinho(
    val lojaId: Int,
    val clienteId: Int,
    val produtoCodigo: Int,
    val operadorLogistigo: String,
    val usuarioId: Int,
    val uf: String,
    val comissao: Double,
    val comissaoPorcentagem: Double,
    val marcasXComissoesId: Int,
    val barra: String,
    val quantidade: Int,
    val pf: Double,
    val valor: Double,
    val valorOriginal: Double,
    val grupoCodigo: Int,
    val desconto: Double,
    val descontoOriginal: Double,
    val st: Double,
    val formalizacao: String,
    val codListaPrecoSync: Int,
    val apontadorCodigo: String,
    var valortotal:Double,
    val nomeProduto:String,
    val nomeLoja:String,
    val razaoSolcial:String,
    val cnpj:String,
    val data:String,
    val valorMinimoLoja:Double,
    val base64: String,
    val pmc: Double,
    val caixapadrao:Int
)

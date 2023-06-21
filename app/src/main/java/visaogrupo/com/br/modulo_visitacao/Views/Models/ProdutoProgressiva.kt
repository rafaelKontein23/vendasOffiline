package visaogrupo.com.br.modulo_visitacao.Views.Models

data class ProdutoProgressiva(
    val nome:String,
    val aprensentacao:String,
    val barra:String,
    val imagem:String,
    val ProdutoCodigo:Int,
    val valor:String,
    val PMC:Double,
    val quatidade:Int,
    val caixapadrao:Int,
    val  estaNoCarrinho:Int,
    val  valorcarrinho:Double,
    val  quantidadeCarrinho:Int,
    val  valorTotal:Double):java.io.Serializable

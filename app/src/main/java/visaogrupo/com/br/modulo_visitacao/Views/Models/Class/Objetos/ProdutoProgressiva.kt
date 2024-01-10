package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos

import java.util.Base64

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
    val  valorTotal:Double,
    val base64 :String,
    val quantidadeEstoque:Int
):java.io.Serializable

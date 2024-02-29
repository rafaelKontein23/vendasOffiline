package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos

data class Pedido(
     val cliente_id:Int,
     val loja_id:Int,
     val cnpj:String,
     val razaosocial:String,
     val valortotal:Double,
     val qtd_Total:Int,
     val data :String,
     val ufCliente:String,
     val lojavalorMinomo:Double
)

package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.content.Context
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitProtudos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActLogin
import java.text.SimpleDateFormat
import java.util.Calendar

class ConstroiJsonPedido {


    fun envairPedidoJson(listaProtudos:MutableList<ProdutosFinalizados>, context:Context, OPL:List<String>, pedidoFinalizado: PedidoFinalizado) :Pair<String, String>{
        var  idMobile = ""
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gsonuserid = Gson()
        val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
        val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
        var  jsonListaProdutos=  ""
        var idsOpl = ""
        val calendar = Calendar.getInstance()

        val dataAtual = calendar.time

        val formatoData = SimpleDateFormat("dd/MM/yyyy")
        var dataFormatada = formatoData.format(dataAtual)
        val horaAtual = calendar.get(Calendar.HOUR_OF_DAY)
        dataFormatada  = dataFormatada.replace("\\","")
        dataFormatada  = dataFormatada.replace("/","")

        idMobile = "${login.Usuario_id}  ${dataFormatada}   ${horaAtual}"

        for (i  in OPL.indices){
            idsOpl +=  "\"OperadorLogistico${i+1}\": \"${OPL[i]}\",\n"
        }
        var finalJosn = ""
        if (pedidoFinalizado.kit == 1){
            var kitTituloLista = mutableListOf<KitProtudos>()


                val pedidosFinalizadosDAO = PedidosFinalizadosDAO(context)
                kitTituloLista = pedidosFinalizadosDAO.listaProdutosPedidokit(pedidoFinalizado.pedidoID.toInt(), pedidoFinalizado.kitCodigo)
                for (i in 0 until  kitTituloLista.size){
                    if (i +1 == kitTituloLista.size){
                        finalJosn = "}"
                    }else{
                        finalJosn = "},"
                    }
                    jsonListaProdutos += "{\n"+
                            "\"Barra\": \"${kitTituloLista[i].barra}\",\n" +
                            "\"Produto_codigo\": ${kitTituloLista[i].produtoCodigo},\n" +
                            "\"Desconto\": ${kitTituloLista[i].desconto},\n" +
                            "\"DescontoOriginal\": 0,\n" +
                            "\"formalizacao\": \"s/n\",\n" +
                            "\"PF\": ${kitTituloLista[i].valorTotal},\n" +
                            "\"Quantidade\": ${kitTituloLista[i].quantidade},\n" +
                            "\"ValorRepasse\": 0.0,\n" +
                            "\"ST\": ,\n" +
                            "\"Valor\": ${kitTituloLista[i].valorTotal}\n" +
                            "${finalJosn}"
                }
        }else{
            for (i in 0 until  listaProtudos.size){
                if (i +1 == listaProtudos.size){
                    finalJosn = "}"
                }else{
                    finalJosn = "},"
                }
                jsonListaProdutos += "{\n"+
                        "\"Barra\": \"${listaProtudos[i].barra}\",\n" +
                        "\"Produto_codigo\": ${listaProtudos[i].produtoCodigo},\n" +
                        "\"Desconto\": ${listaProtudos[i].desconto},\n" +
                        "\"DescontoOriginal\": ${listaProtudos[i].descontoOriginal},\n" +
                        "\"formalizacao\": \"${listaProtudos[i].formalizacao}\",\n" +
                        "\"PF\": ${listaProtudos[i].pf},\n" +
                        "\"Quantidade\": ${listaProtudos[i].quantidade},\n" +
                        "\"ValorRepasse\": ${listaProtudos[i].valorRepasse},\n" +
                        "\"ST\": ${listaProtudos[i].st},\n" +
                        "\"Valor\": ${listaProtudos[i].valor}\n" +
                        "${finalJosn}"
            }
        }

        val data = RecuperaDataAtual.dataAtual()
        val chavekey=  login.Usuario_id + pedidoFinalizado?.clienteId +data
        var chave = login.Usuario_id + pedidoFinalizado?.clienteId +data
        chave = chave.replace(":","").replace("/","").replace(" ","")


        var quantidadeKit =0
        var pedidoCodigoKit = 0
        if (pedidoFinalizado.kit == 1){
             quantidadeKit = pedidoFinalizado.quantidade!!
             pedidoCodigoKit = pedidoFinalizado.kitCodigo

        }
        val json = "{\n" +
                "\"Pedidos\": {"+
                    "\"TipoCadastro\": ${login.TipoCadastro_id},\n"+
                    "\"Usuario_id\": ${login.Usuario_id},\n"+
                    "\"CodigoKit\": ${pedidoCodigoKit},\n"+
                    "\"QuantidadeKit\": ${quantidadeKit},\n"+
                    "\"EmpresaId\": ${pedidoFinalizado?.clienteId},\n" +
                    "\"LojaId\": ${pedidoFinalizado?.lojaId},\n" +
                    "\"CotacaoId\": 0,\n"+
                    "\"Teste\": ${1},"+
                    "\"NrPedidoOrigemBonificacao\": 0,\n"+
                    "\"Supervisor_Id\": 0,\n"+
                    "\"IdMobile\": ${idMobile},\n"+
                    "\"OrderAgrupado\": 0,\n"+
                    "\"ChavePedido\": \"${chave}\",\n" +
                    "\"UF\": \"${pedidoFinalizado?.uf}\",\n"+
                    "\"IP\": \"${CapturaIp.getDeviceIPAddress()}\",\n" +
                    "\"Observacao\": \"\",\n" +
                    "\"PedidoCliente\": \"${pedidoFinalizado.numeroPedido}\",\n" +
                    "\"FormaPagamento\": \"${pedidoFinalizado.formaDePagamento}\",\n" +
                    "\"HashEmail\": \"\",\n" +
                    "\"OrigemTracking\": \"app\",\n" +
                    "\"JustificativaANR\": \"${pedidoFinalizado.justificativaANR}\",\n" +
                    "\"NumeroBonificacao\": \"\",\n" +
                    "\"OrigemTudoFarma\": \"true\",\n" +
                    "\"PrimeiraCompra\": \"false\",\n" +
                    "\"OrigemBonificacao\": \"false\",\n" +
                    "\"Bonificacao\": \"false\",\n" +
                    "\"AprovacaoPrazo\": \"${pedidoFinalizado.RegraPrazo}\",\n" +
                    "\"ANR\": \"${pedidoFinalizado.anr}\",\n" +
                   "\"TipoLoja\": ${pedidoFinalizado?.TipoLoja},\n"+
                    idsOpl+
                    "\"Produtos\":[" +
                           "${jsonListaProdutos}" +
                    "]," +
                  "\"Versaoapp\": \"${ActLogin.versao}\"\n" +
                    "}" +
              "}"


        return Pair(json, chavekey)
    }

}

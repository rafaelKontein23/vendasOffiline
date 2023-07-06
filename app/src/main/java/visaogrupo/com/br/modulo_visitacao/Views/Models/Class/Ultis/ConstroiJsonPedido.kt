package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import android.content.Context
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.Views.View.Atividades.ActLogin

class ConstroiJsonPedido {


    fun envairPedidoJson(listaProtudos:MutableList<Carrinho>, context:Context, OPL:String, Observacao :String, formPag:String) :Pair<String, String>{

        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        val  lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        val  clientesSelecionado =  gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
        val gsonuserid = Gson()
        val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
        val login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
        var  jsonListaProdutos=  ""
        var finalJosn = ""
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
                "\"ValorRepasse\": ${listaProtudos[i].valor},\n" +
                "\"ST\": ${listaProtudos[i].st},\n" +
                "\"Valor\": ${listaProtudos[i].valortotal}\n" +
                "${finalJosn}"
            }
        val data = RecuperaDataAtual.dataAtual()
        val chavekey=  login.Usuario_id + clientesSelecionado.Empresa_id +data
        var chave = login.Usuario_id + clientesSelecionado.Empresa_id +data
        chave = chave.replace(":","").replace("/","").replace(" ","")
        val json = "{\n" +
                "\"DataLimiteFaturamento\": \"\",\n" +
                "\"ChavePedido\": \"${chave}\",\n" +
                "\"Distribuidora_id\": 0,\n" +
                "\"EmpresaId\": ${clientesSelecionado.Empresa_id},\n" +
                "\"FormaPagamento\": \"${formPag}\",\n" +
                "\"IdMobile\": \"${"202306260648261"}\",\n" +
                "\"IP\": \"${"192.168.0.62"}\",\n" +
                "\"LojaId\": ${lojaSelecionada.loja_id},\n" +
                "\"Observacao\": \"${Observacao}\",\n" +
                "\"OperadorLogistico1\": \"${OPL}\",\n" +
                "\"PedidoCliente\": \"\",\n" +
                "\"Produtos\":[" +
                "${jsonListaProdutos}" +
                "]," +
                "\"Setor\": \"${"X1234Y"}\",\n"+
                "\"Teste\": ${1},"+
                "\"TipoCadastro\": ${login.TipoCadastro_id},\n"+
                "\"TipoLoja\": ${lojaSelecionada.loja_id},\n"+
                "\"UF\": \"${clientesSelecionado.UF}\",\n"+
                "\"Usuario_id\": ${login.Usuario_id},\n"+
                "\"Versao\": \"${ActLogin.versao}\"\n"+
                "}"

        return Pair(json, chavekey)
    }

}

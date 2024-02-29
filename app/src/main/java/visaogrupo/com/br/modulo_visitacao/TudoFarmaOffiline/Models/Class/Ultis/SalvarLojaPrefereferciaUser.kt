package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import android.content.Context
import com.google.gson.Gson
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.LojasDAO

class SalvarLojaeClientePrefereferciaUser {

    companion object {
        fun salvarLoja(clienteId:Int,lojaId:Int,context: Context){
            val querylojasClientes = " SELECT DISTINCT LojCli.empresa_id, Lojas.*,imagembase64 " +
                    "FROM TB_lojas Lojas " +
                    "INNER JOIN TB_lojaporcliente LojCli ON Lojas.Loja_id = LojCli.loja_id " +
                    "INNER JOIN TB_clientes CLIENTES ON Clientes.Empresa_id = LojCli.empresa_id " +
                    "INNER JOIN TB_OperadorLogistico Operador ON Operador.loja_id = Lojas.loja_id AND Operador.estado = Clientes.uf " +
                    "LEFT join TB_ImagensLojas ImgLj ON ImgLj.loja_id = Lojas.loja_id \n"+
                    "WHERE LojCli.empresa_id = ${clienteId} And Lojas.loja_id = ${lojaId} "
            val lojaDao = LojasDAO(context)
            val lojaCarrinho =  lojaDao.pegaLojaCarrinnho(querylojasClientes)
            val gson = Gson()
            val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val objetoSerializado = gson.toJson(lojaCarrinho)
            val editor = sharedPreferences.edit()
            editor.putString("LojaSelecionadaCarrinho", objetoSerializado)
            editor.apply()
        }

        fun salvaCliente(context: Context, clienteId: Int){
            val queryClientes = "SELECT * FROM TB_clientes WHERE empresa_id = ${clienteId} "
            val clienteDao = ClientesDAO(context)
            val clienteCarrinho =  clienteDao.pegaClienteCarrinho(queryClientes,context)
            val gson = Gson()
            val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val objetoSerializado = gson.toJson(clienteCarrinho)
            val editor = sharedPreferences.edit()
            editor.putString("ClienteSelecionadaCarrinho", objetoSerializado)
            editor.apply()
        }
    }
}
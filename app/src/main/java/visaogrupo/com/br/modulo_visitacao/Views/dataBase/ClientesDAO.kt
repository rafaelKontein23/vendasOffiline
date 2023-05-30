package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IClientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

class ClientesDAO (context: Context): IClientes{
    val DBClientes = DataBaseHelber(context).writableDatabase
    override fun insert(jsoncCientes: JSONArray): Boolean {
        try {
            val valoresClientes =   ContentValues()

            for (i in 0 until jsoncCientes.length()){
                try {
                    val jsonClientesRetorno = jsoncCientes.optJSONObject(i)

                    valoresClientes.put("Empresa_id",jsonClientesRetorno.optInt("Empresa_id"))
                    valoresClientes.put("cnpj",jsonClientesRetorno.optString("CNPJ"))
                    valoresClientes.put("RazaoSocial",jsonClientesRetorno.optString("RazaoSocial"))
                    valoresClientes.put("Fantasia",jsonClientesRetorno.optString("Fantasia"))
                    valoresClientes.put("Endereco",jsonClientesRetorno.optString("Endereco"))
                    valoresClientes.put("Numero",jsonClientesRetorno.optString("Numero"))
                    valoresClientes.put("Complemento",jsonClientesRetorno.optString("Complemento"))
                    valoresClientes.put("Bairro",jsonClientesRetorno.optString("Bairro"))
                    valoresClientes.put("Cidade",jsonClientesRetorno.optString("Cidade"))
                    valoresClientes.put("UF",jsonClientesRetorno.optString("UF"))
                    valoresClientes.put("CompraControlado",jsonClientesRetorno.optBoolean("CompraControlado"))
                    valoresClientes.put("LimiteCredito",jsonClientesRetorno.optInt("LimiteCredito"))
                    valoresClientes.put("UltimoPedido",jsonClientesRetorno.optString("UltimoPedido"))
                    valoresClientes.put("VendaDireta",jsonClientesRetorno.optString("VendaDireta"))
                    valoresClientes.put("Associativismo",jsonClientesRetorno.optString("Associativismo"))
                    valoresClientes.put("Telefone",jsonClientesRetorno.optString("Telefone"))
                    valoresClientes.put("Email",jsonClientesRetorno.optString("Email"))
                    valoresClientes.put("Formalizado",jsonClientesRetorno.optString("Formalizado"))
                    valoresClientes.put("Investimento",jsonClientesRetorno.getBoolean("Investimento"))
                    valoresClientes.put("DuplicataVencida",jsonClientesRetorno.optInt("DuplicataVencida"))
                    valoresClientes.put("SanitarioData",jsonClientesRetorno.optString("SanitarioData"))

                    val jsonClienteporloja = JSONArray(jsonClientesRetorno.optString("LOJA"))


                    for (j in 0 until jsonClienteporloja.length()) {
                        val jsonClientesPorLojasRetorno = jsonClienteporloja.optJSONObject(j)
                        val lojaId = jsonClientesPorLojasRetorno.optInt("Loja_id")

                        val valoresClientesPorLojas = ContentValues()
                        valoresClientesPorLojas.put("empresa_id", jsonClientesRetorno.optInt("Empresa_id"))
                        valoresClientesPorLojas.put("loja_id", lojaId)

                        DBClientes.insertOrThrow("TB_lojaporcliente", null, valoresClientesPorLojas)
                    }


                    DBClientes.insert("TB_clientes",null,valoresClientes)



                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
            return true
        }catch (e:Exception){
            e.printStackTrace()
            return  false
        }
    }

    override fun atualizar(Clientes: Lojas): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(Clientes: Lojas): Boolean {
        TODO("Not yet implemented")
    }
}
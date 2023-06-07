package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IClientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.LojaXCliente
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
                    valoresClientes.put("Cep",jsonClientesRetorno.optString("Cep"))
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

    override fun atualizar(Clientes: Clientes): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(Clientes: Clientes): Boolean {
        TODO("Not yet implemented")
    }

    override fun listar(context: Context,query:String): MutableList<Clientes> {
        val lojaxcliente :List<LojaXCliente> = mutableListOf<LojaXCliente>()
        val clientesList = mutableListOf<Clientes>()
        val db = DataBaseHelber(context)
        val listaclientes = query
        val cursor: Cursor = db.readableDatabase.rawQuery(listaclientes,null)

        while (cursor.moveToNext()){

            val Empresa_id:Int = cursor.getInt(0)
            val cnpj =  cursor.getString(1)
            val RazaoSocial =   cursor.getString(2)
            val Fantasia =  cursor.getString(3)
            val Endereco =   cursor.getString(4)
            val Numero =  cursor.getString(5)
            val Complemento =    cursor.getString(6)
            val Bairro =    cursor.getString(7)
            val Cidade =   cursor.getString(8)
            val UF =    cursor.getString(9)
            val Cep =  cursor.getString(10)
            val UltimoPedido =  cursor.getString(13)
            val VendaDireta =  cursor.getString(14)
            val Associativismo =   cursor.getString(15)
            val Telefone = cursor.getString(16)
            val Email =  cursor.getString(17)
            val DuplicataVencida =  cursor.getInt(20)


            val clientes = Clientes(
                "",Associativismo,Bairro,"",Cep,cnpj,
                "",0,
                0,Cidade,0,"",Complemento,
                0, false,
                "","","",
                DuplicataVencida,Email,Empresa_id,
                Endereco,false,
                Fantasia,0,
                0,"",""
                ,1,lojaxcliente,"",
                "","","",Numero,RazaoSocial,Telefone,"","","",UF,UltimoPedido,VendaDireta)
            clientesList.add(clientes)
        }
        return clientesList
    }

    override fun countar(context:Context): Int {
        val dbHelper = DataBaseHelber(context)
        val db = dbHelper.readableDatabase

        val query = "SELECT COUNT(*) FROM TB_clientes"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val count = cursor.getInt(0)

        cursor.close()
        db.close()

        return count
    }
}
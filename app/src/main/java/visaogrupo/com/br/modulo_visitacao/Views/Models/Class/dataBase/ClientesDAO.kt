package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.IClientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.LojaXCliente

class ClientesDAO (context: Context): IClientes {
    val DBClientes = DataBaseHelber(context).writableDatabase
    override fun insert(jsoncCientes: JSONArray): Boolean {
        try {
            val valoresClientes =   ContentValues()

            for (i in 0 until jsoncCientes.length()){
                try {
                    val jsonClientesRetorno = jsoncCientes.optJSONObject(i)
                    val FormaPagamentoExclusiva = jsonClientesRetorno.getBoolean("FormaPagamentoExclusiva")

                    valoresClientes.put("Empresa_id",jsonClientesRetorno.optInt("Empresa_id"))
                    valoresClientes.put("cnpj",jsonClientesRetorno.optString("CNPJ"))
                    valoresClientes.put("RazaoSocial",jsonClientesRetorno.optString("RazaoSocial"))
                    valoresClientes.put("Cep",jsonClientesRetorno.optString("Cep"))
                    valoresClientes.put("Compra",jsonClientesRetorno.optInt("Compra"))
                    valoresClientes.put("Fantasia",jsonClientesRetorno.optString("Fantasia"))
                    valoresClientes.put("Endereco",jsonClientesRetorno.optString("Endereco"))
                    valoresClientes.put("Numero",jsonClientesRetorno.optString("Numero"))
                    valoresClientes.put("Complemento",jsonClientesRetorno.optString("Complemento"))
                    valoresClientes.put("Bairro",jsonClientesRetorno.optString("Bairro"))
                    valoresClientes.put("Cidade",jsonClientesRetorno.optString("Cidade"))
                    valoresClientes.put("UF",jsonClientesRetorno.optString("UF"))
                    valoresClientes.put("CompraControlado",jsonClientesRetorno.optInt("CompraControlado"))
                    valoresClientes.put("LimiteCredito",jsonClientesRetorno.optInt("LimiteCredito"))
                    valoresClientes.put("UltimoPedido",jsonClientesRetorno.optString("UltimoPedido"))
                    valoresClientes.put("VendaDireta",jsonClientesRetorno.optString("VendaDireta"))
                    valoresClientes.put("Associativismo",jsonClientesRetorno.optString("Associativismo"))
                    valoresClientes.put("Telefone",jsonClientesRetorno.optString("Telefone"))
                    valoresClientes.put("Email",jsonClientesRetorno.optString("Email"))
                    valoresClientes.put("Formalizado",jsonClientesRetorno.optString("Formalizado"))
                    valoresClientes.put("DuplicataVencida",jsonClientesRetorno.optInt("DuplicataVencida"))
                    valoresClientes.put("SanitarioData",jsonClientesRetorno.optString("SanitarioData"))
                    valoresClientes.put("Codigo",jsonClientesRetorno.optInt("CODLISTAPRECOSYNC"))
                    valoresClientes.put("CodEstoque",jsonClientesRetorno.optInt("CODEMPRESASYNC"))
                    valoresClientes.put("ExibeAlerta",jsonClientesRetorno.optString("ExibeAlerta"))
                    valoresClientes.put("LimiteCredito",jsonClientesRetorno.optString("LimiteCredito"))
                    valoresClientes.put("LimiteDisponivel",jsonClientesRetorno.optString("LimiteDisponivel"))
                    valoresClientes.put("UltimoPedido",jsonClientesRetorno.optString("UltimoPedido"))
                    valoresClientes.put("FormaPagamentoExclusiva",FormaPagamentoExclusiva)

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
            val Compra  = cursor.getInt(23)
            val Exbibelerta = cursor.getString(24)
            val  FormaPagamentoExclusiva = cursor.getInt(26)

            val clientes = Clientes(
                "",Associativismo,Bairro,"",Cep,cnpj,
                "",0,
                0,Cidade,0,"",Complemento,
                Compra, false,
                "","","",
                DuplicataVencida,Email,Empresa_id,
                Endereco,Exbibelerta,
                Fantasia,0,
                0,"",""
                ,1,lojaxcliente,"",
                "","","",Numero,RazaoSocial,Telefone,"","","",
                UF,UltimoPedido,
                VendaDireta, FormaPagamentoExclusiva)
            clientesList.add(clientes)
        }
        return clientesList
    }

    fun pegaClienteCarrinho(query:String,context: Context):Clientes?{
        val lojaxcliente :List<LojaXCliente> = mutableListOf<LojaXCliente>()
        var clientes:Clientes? = null
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
            val Compra  = cursor.getInt(23)
            val Exbibelerta = cursor.getString(24)
            val  FormaPagamentoExclusiva = cursor.getInt(26)

            clientes = Clientes(
                "",Associativismo,Bairro,"",Cep,cnpj,
                "",0,
                0,Cidade,0,"",Complemento,
                Compra, false,
                "","","",
                DuplicataVencida,Email,Empresa_id,
                Endereco,Exbibelerta,
                Fantasia,0,
                0,"",""
                ,1,lojaxcliente,"",
                "","","",Numero,RazaoSocial,Telefone,"","","",
                UF,UltimoPedido,
                VendaDireta, FormaPagamentoExclusiva)
            break

        }
        return clientes
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

    fun buscarClientes(busca:String,context: Context):MutableList<Clientes>{
        val clientesList = mutableListOf<Clientes>()
        try {
            val dbbusca = DataBaseHelber(context)
            val query = "SELECT * FROM TB_clientes WHERE cnpj LIKE '%${busca}%' OR RazaoSocial LIKE '%${busca}%' OR Endereco LIKE '%${busca}%'"
            val cursor = dbbusca.readableDatabase.rawQuery(query,null)
            val  lojasxclienets = mutableListOf<LojaXCliente>()

            while (cursor.moveToNext()){
                if(cursor == null){

                }else{
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
                    val Compra = cursor.getInt(23)
                    val FormaPagamentoExclusiva = cursor.getInt(26)



                    val clientes = Clientes(
                        "",Associativismo,Bairro,"",Cep,cnpj,
                        "",0,
                        0,Cidade,0,"",Complemento,
                        Compra, false,
                        "","","",
                        DuplicataVencida,Email,Empresa_id,
                        Endereco,"",
                        Fantasia,0,
                        0,"",""
                        ,1,lojasxclienets,"",
                        "","","",Numero,RazaoSocial,Telefone,"","","",UF,
                        UltimoPedido,VendaDireta,FormaPagamentoExclusiva)
                    clientesList.add(clientes)

                }
            }
            return  clientesList
        }catch (e:Exception){
            e.printStackTrace()
        }

      return clientesList
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface.ILojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Lojas

class LojasDAO (context:Context):
    ILojas {

    var DBLojas = DataBaseHelber(context).writableDatabase

    override   fun insert(lojas: JSONArray): Boolean {
         try {

             val valoresLojas =   ContentValues()


             for (i in 0 until lojas.length()) {
                 try {
                     val  jsonLojasRetorno = lojas.optJSONObject(i)
                     val RegraPrazoMedio = jsonLojasRetorno.getBoolean("RegraPrazoMedio")

                     valoresLojas.put("nome", jsonLojasRetorno.optString("Nome"));
                     valoresLojas.put("loja_id", jsonLojasRetorno.optInt("Loja_id"));
                     valoresLojas.put("tipo", jsonLojasRetorno.optString("tipo"));
                     valoresLojas.put("MinimoUnidades", jsonLojasRetorno.optInt("MinimoUnidades"));
                     valoresLojas.put("MinimoValor", jsonLojasRetorno.optDouble("MinimoValor"));
                     valoresLojas.put("LojaTipo", jsonLojasRetorno.optInt("LojaTipo"));
                     valoresLojas.put("LogoHome", jsonLojasRetorno.optString("LogoHome"));
                     valoresLojas.put("Distribuidora", jsonLojasRetorno.optInt("Distribuidora"));
                     valoresLojas.put("Loja_Desconto", jsonLojasRetorno.optBoolean("LojaDesconto"));
                     valoresLojas.put("DescontoMaximo", jsonLojasRetorno.optDouble("DescontoMaximo"));
                     valoresLojas.put("Tipo_Imposto_ID", jsonLojasRetorno.optInt("TipoImposto_ID"));
                     valoresLojas.put("Tipo_Imposto", jsonLojasRetorno.optString("TipoImposto"));
                     valoresLojas.put("Libera_Fidelidade", jsonLojasRetorno.optBoolean("LiberaFidelidade"));
                     valoresLojas.put("Qtd_ProdutosLoja", jsonLojasRetorno.optInt("QtdProdutosLoja"));
                     valoresLojas.put("Loja_Tablet", jsonLojasRetorno.optBoolean("LojaTablet"));
                     valoresLojas.put("Venda_Tipo_id", jsonLojasRetorno.optInt("VendaTipo_id"));
                     valoresLojas.put("Data_Inicio", jsonLojasRetorno.optString("DataInicio"));
                     valoresLojas.put("Data_Fim", jsonLojasRetorno.optString("DataFim"));
                     valoresLojas.put("Portal_Tablet", jsonLojasRetorno.optInt("PortalTablet"));
                     valoresLojas.put("Qtd_Minima_Operador", jsonLojasRetorno.optInt("QtdMinima_Operador"));
                     valoresLojas.put("Qtd_Maxima_Operador", jsonLojasRetorno.optInt("QtdMaxima_Operador"));
                     valoresLojas.put("Loja_Online", jsonLojasRetorno.optBoolean("LojaOnline"));
                     valoresLojas.put("Minimo_Aprovacao", jsonLojasRetorno.optDouble("MinimoAprovacao"));
                     valoresLojas.put("Valida_Estoque", jsonLojasRetorno.optBoolean("ValidaEstoque"));
                     valoresLojas.put("Loja_Preco", jsonLojasRetorno.optBoolean("LojaPreco"));
                     valoresLojas.put("RegraPrazoMedio", RegraPrazoMedio);
                     valoresLojas.put("Exibe_Estoque", jsonLojasRetorno.optBoolean("ExibeEstoque"));
                     valoresLojas.put("ANR", jsonLojasRetorno.optBoolean("ANR"));
                     DBLojas.insert("TB_lojas",null,valoresLojas)
                 }catch (e:Exception){
                     e.printStackTrace()
                 }

             }
             Log.d("Terminou","")
             return  true
         }catch (e:Exception){
             e.printStackTrace()
             return  false
         }
    }

    override fun atualizar(produto: Lojas): Boolean {
        TODO("Not yet implemented")
    }

    override fun remover(produto: Lojas): Boolean {
        TODO("Not yet implemented")
    }

    override fun listarlojas(context: Context, empresaID: Int,query:String): List<Lojas> {
        val listaLojasobject = mutableListOf<Lojas>()
        val dblistalojas = DataBaseHelber(context)


        val  cursor = dblistalojas.readableDatabase.rawQuery(query,null)
        while (cursor.moveToNext()){
            val loja_id = cursor.getInt(1)
            val nome =  cursor.getString(2)
            val MinimoUnidades =   cursor.getInt(4)
            val MinimoValor =  cursor.getDouble(5)
            val LojaTipo =   cursor.getInt(6)
            val tipo =  cursor.getString(7)
            val LogoHome =    cursor.getString(8)
            val Distribuidora =    cursor.getInt(9)
            val Loja_Desconto =   cursor.getString(10)
            val DescontoMaximo =    cursor.getDouble(11)
            val Tipo_Imposto_ID =  cursor.getInt(12)
            val Tipo_Imposto =  cursor.getInt(13)
            val Libera_Fidelidade =  cursor.getString(14)
            val Qtd_ProdutosLoja =   cursor.getInt(15)
            val Loja_Tablet = cursor.getString(16)
            val Venda_Tipo_id =  cursor.getInt(17)
            val Data_Inicio =  cursor.getString(19)
            val Data_Fim = cursor.getString(20)
            val Portal_Tablet =  cursor.getInt(21)
            val Qtd_Minima_Operador =   cursor.getInt(22)
            val Qtd_Maxima_Operador =  cursor.getInt(23)
            val Loja_Online =   cursor.getString(24)
            val Minimo_Aprovacao =  cursor.getDouble(25)
            val Valida_Estoque =    cursor.getString(26)
            val Loja_Preco =    cursor.getString(27)
            val ANR        =      cursor.getInt(28)
            val exibe_Estoque = cursor.getInt(29)
            val RegraPrazoMedio = cursor.getInt(30)
            val loja = Lojas(loja_id, nome,
                MinimoUnidades, MinimoValor,
                LojaTipo, tipo, LogoHome,
                Distribuidora, Loja_Desconto,
                DescontoMaximo, Tipo_Imposto_ID,
                Tipo_Imposto, Libera_Fidelidade,
                Qtd_ProdutosLoja, Loja_Tablet,
                Venda_Tipo_id, Data_Inicio, Data_Fim,
                Portal_Tablet, Qtd_Minima_Operador,
                Qtd_Maxima_Operador, Loja_Online,
                Minimo_Aprovacao, Valida_Estoque,
                Loja_Preco, exibe_Estoque,ANR, RegraPrazoMedio)

            listaLojasobject.add(loja)

        }

        return  listaLojasobject
    }
}
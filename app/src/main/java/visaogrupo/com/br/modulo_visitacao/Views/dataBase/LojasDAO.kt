package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.ILojas
import visaogrupo.com.br.modulo_visitacao.Views.Models.Lojas

class LojasDAO (context:Context):ILojas{

    var DBLojas = DataBaseHelber(context).writableDatabase
    val listaLojas = mutableListOf<Lojas>()
    override   fun insert(lojas: JSONArray): Boolean {
         try {

             val valoresLojas =   ContentValues()


             for (i in 0 until lojas.length()) {
                 try {
                     val  jsonLojasRetorno = lojas.optJSONObject(i)

                     valoresLojas.put("nome", jsonLojasRetorno.optString("Nome"));
                     valoresLojas.put("loja_id", jsonLojasRetorno.optInt("Loja_id"));
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
                     valoresLojas.put("Portal_Tablet", jsonLojasRetorno.optInt("PortalTablet"));
                     valoresLojas.put("Qtd_Minima_Operador", jsonLojasRetorno.optInt("QtdMinima_Operador"));
                     valoresLojas.put("Qtd_Maxima_Operador", jsonLojasRetorno.optInt("QtdMaxima_Operador"));
                     valoresLojas.put("Loja_Online", jsonLojasRetorno.optBoolean("LojaOnline"));
                     valoresLojas.put("Minimo_Aprovacao", jsonLojasRetorno.optDouble("MinimoAprovacao"));
                     valoresLojas.put("Valida_Estoque", jsonLojasRetorno.optBoolean("ValidaEstoque"));
                     valoresLojas.put("Loja_Preco", jsonLojasRetorno.optBoolean("LojaPreco"));
                     valoresLojas.put("Exibe_Estoque", jsonLojasRetorno.optBoolean("ExibeEstoque"));
                     val lojas = Lojas(
                         jsonLojasRetorno.optBoolean("ANR"),
                         jsonLojasRetorno.optString("DataFim"),
                         jsonLojasRetorno.optString("DataInicio"),
                         jsonLojasRetorno.optDouble("DescontoMaximo"),
                         jsonLojasRetorno.optInt("Distribuidora"),
                         jsonLojasRetorno.optBoolean("LiberaCotacao"),
                         jsonLojasRetorno.optBoolean("LiberaFidelidade"),
                         jsonLojasRetorno.optString("LogoHome"),
                         jsonLojasRetorno.optBoolean("LojaTablet"),
                         jsonLojasRetorno.optInt("LojaTipo"),
                         jsonLojasRetorno.optInt("Loja_id"),
                         jsonLojasRetorno.optInt("MinimoUnidades"),
                         jsonLojasRetorno.optDouble("MinimoValor"),
                         jsonLojasRetorno.optString("Nome"),
                         jsonLojasRetorno.optInt("Ordem"),
                         jsonLojasRetorno.optString("Portfolio"),
                         jsonLojasRetorno.optInt("QtdProdutosLoja"),
                         jsonLojasRetorno.optString("TipoImposto"),
                         jsonLojasRetorno.optInt("TipoImposto_ID"),
                         jsonLojasRetorno.optInt("Unificada"),
                         jsonLojasRetorno.optBoolean("ValidaEstoque"),
                         jsonLojasRetorno.optDouble("ValorUnificada"),
                         jsonLojasRetorno.optInt("VendaTipo_id"),
                         jsonLojasRetorno.optString("tipo")
                     )
                     listaLojas.add(lojas)

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
}
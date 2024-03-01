package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase


import android.content.ContentValues
import android.content.Context
import com.google.gson.Gson
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.GrupoLojaAb
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.GrupoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoAB

class GrupoLojaAbDAO(context: Context) {
    val dbGrupoProduto = DataBaseHelber(context)
    fun insertGrupoEProduto(jsonArrayGr: JSONArray){

        for(i in 0 until  jsonArrayGr.length()){
            try {
                val jsonAtual = jsonArrayGr.getJSONObject(i)
                val gson = Gson()

                val gsonGrupo = gson.fromJson(jsonAtual.toString(), GrupoLojaAb::class.java)

                val insert = ContentValues()
                insert.put("Grupo_codigo", gsonGrupo.Grupo_Codigo)
                insert.put("Nome_grupo",gsonGrupo.NomeGrupo)
                insert.put("Prioridade",gsonGrupo.Prioridade)
                insert.put("Loja_id",gsonGrupo.Loja_id)
                insert.put("Grupo",gsonGrupo.Grupo)
                insert.put("Porc",gsonGrupo.Porc)
                insert.put("Codigo_PRECO_SYNC",gsonGrupo.CODLISTAPRECOSYNC)
                dbGrupoProduto.writableDatabase.insertOrThrow("Tb_GrupoAB", null, insert)

                for (i in gsonGrupo.GrupoFilho!!){
                    val insertProdutoGrupo = ContentValues()
                    insertProdutoGrupo.put("Grupo_codigo", gsonGrupo.Grupo_Codigo)
                    insertProdutoGrupo.put("Produto_codigo",i.Produto_codigo)
                    insertProdutoGrupo.put("Nome",i.Nome)
                    insertProdutoGrupo.put("Referencia",i.Referencia)
                    insertProdutoGrupo.put("APRESENTACAO",i.Apresentacao)
                    insertProdutoGrupo.put("PrincipioAtivo",i.PrincipioAtivo)
                    insertProdutoGrupo.put("ListaICMS",i.ListaICMS)
                    insertProdutoGrupo.put("CaixaPadrao",i.CaixaPadrao)
                    insertProdutoGrupo.put("Barra",i.Barra)
                    dbGrupoProduto.writableDatabase.insertOrThrow("TB_grupoAB_Produtos",null,insertProdutoGrupo)

                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
    fun inserirGrupoProgressiva(jsonArray: JSONArray){
        for(i in 0 until jsonArray.length()){
            try {
                val json = jsonArray.getJSONObject(i)
                val gson = Gson()
                val gruppoProgressiva = gson.fromJson(json.toString(),GrupoProgressiva::class.java)
                val  valuesonsert = ContentValues()

                valuesonsert.put("Loja_id", gruppoProgressiva.Loja_id)
                valuesonsert.put("CODLISTAPRECOSYNC", gruppoProgressiva.CODLISTAPRECOSYNC)
                valuesonsert.put("Grupo_Codigo", gruppoProgressiva.Grupo_Codigo)
                valuesonsert.put("Prod_cod", gruppoProgressiva.Prod_cod)
                valuesonsert.put("Quantidade", gruppoProgressiva.Quantidade)
                valuesonsert.put("Desconto", gruppoProgressiva.Desconto)
                valuesonsert.put("PF", gruppoProgressiva.PF)
                valuesonsert.put("PMC", gruppoProgressiva.PMC)
                valuesonsert.put("UF", gruppoProgressiva.UF)
                valuesonsert.put("QtdMin", gruppoProgressiva.QtdMin)
                valuesonsert.put("QtdMax", gruppoProgressiva.QtdMax)
                valuesonsert.put("PORC", gruppoProgressiva.Porc)
                valuesonsert.put("formalizacao", gruppoProgressiva.formalizacao)

                dbGrupoProduto.writableDatabase.insertOrThrow("TB_Grupo_Progressiva",null,valuesonsert)
            }catch (e:Exception){
                e.printStackTrace()
            }



        }
    }

    fun listarLoja(lojaId :Int, codiPreco:Int) : MutableList<GrupoLojaAb> {
         val query  =  "SELECT * FROM Tb_GrupoAB WHERE  loja_id = ${lojaId} GROUP by 1"
         val cursor = dbGrupoProduto.writableDatabase.rawQuery(query,null)

        val listaGrupoAB = mutableListOf<GrupoLojaAb>()
         while (cursor.moveToNext()){
             val grupoCodigo = cursor.getInt(0)
             val nomeGrupo= cursor.getString(1)
             val prioridade = cursor.getInt(2)
             val lojaID = cursor.getInt(3)
             val grupo = cursor.getString(4)
             val porc = cursor.getDouble(5)
             val codPrecoSync = cursor.getInt(6)

             val grupoLojaAb = GrupoLojaAb(codPrecoSync, grupo,
                 Grupo_Codigo = grupoCodigo, Loja_id = lojaID, NomeGrupo = nomeGrupo, Porc = porc, Prioridade = prioridade)
             listaGrupoAB.add(grupoLojaAb)

         }
        for (i in listaGrupoAB){
            val queryProdutosLojaAb = "SELECT DISTINCT grupoAB_Produtos.*, Grupo_Progressiva.*, img.imagembase64,CASE WHEN IFNULL(carrinho.Produto_codigo,0 )>0 THEN 1 ELSE 0 END estaNoCaarinho,carrinho.Quantidade FROM TB_grupoAB_Produtos as grupoAB_Produtos\n" +
                    "INNER JOIN TB_Grupo_Progressiva Grupo_Progressiva  ON grupoAB_Produtos.Grupo_codigo = Grupo_Progressiva.Grupo_Codigo and Grupo_Progressiva.Prod_cod = grupoAB_Produtos.Produto_codigo\n" +
                    "LEFT JOIN TB_Imagens img ON img.barra = grupoAB_Produtos.Barra\n" +
                    "LEFT JOIN TB_Carrinho carrinho  ON carrinho.loja_id = ${i.Loja_id} AND carrinho.Produto_codigo = grupoAB_Produtos.Produto_codigo\n"+
                    "WHERE grupoAB_Produtos.Grupo_codigo =${i.Grupo_Codigo} AND  Grupo_Progressiva.Loja_id = ${i.Loja_id} AND Grupo_Progressiva.CODLISTAPRECOSYNC =12"

            val cursorProduto = dbGrupoProduto.writableDatabase.rawQuery(queryProdutosLojaAb,null)
            val listaprodutoAB = mutableListOf<ProdutoAB>()
            while (cursorProduto.moveToNext()){
                 val grupoCodigo = cursorProduto.getInt(0)
                 val produtoCodigo = cursorProduto.getInt(1)
                 val nome = cursorProduto.getString(2)
                 val refrencia = cursorProduto.getString(3)
                 val apresentacao = cursorProduto.getString(4)
                 val principioAtivo = cursorProduto.getString(5)
                 val listIcms = cursorProduto.getString(6)
                 val caixapadrao = cursorProduto.getInt(7)
                 val barra =      cursorProduto.getString(8)
                 val lojaId =   cursorProduto.getInt(9)
                 val codSync = cursorProduto.getInt(10)
                 val GrupoCodigo = cursorProduto.getInt(11)
                 val produtoCod = cursorProduto.getInt(12)
                 val quantidade = cursorProduto.getInt(13)
                 val desconto = cursorProduto.getDouble(14)
                 val pf       = cursorProduto.getDouble(15)
                 val pmc      = cursorProduto.getDouble(16)
                 val uf =       cursorProduto.getString(17)
                 val QtdMin = cursorProduto.getInt(18)
                 val QtdMax = cursorProduto.getInt(19)
                 val porc   = cursorProduto.getInt(20)
                 val form =   cursorProduto.getInt(21)
                 var imagem = ""
                if (!cursorProduto.getString(22).isNullOrEmpty()){
                    imagem =cursorProduto.getString(22)
                }
                var estaNoCarrinho = cursorProduto.getInt(23)
                var quantidadeProduto = 0
                 if (cursorProduto.getInt(24) != null){
                     quantidadeProduto = cursorProduto.getInt(24)
                 }

                 val  produtoAB = ProdutoAB(apresentacao,barra,caixapadrao,imagem,listIcms,nome,principioAtivo,
                    produtoCodigo,refrencia,codSync,desconto,grupoCodigo,lojaId,pf,pmc,porc,
                    produtoCod,QtdMax,QtdMin,quantidade,uf,GrupoCodigo,estaNoCarrinho = estaNoCarrinho, quantidadeCarrinho = quantidadeProduto)
                listaprodutoAB.add(produtoAB)



            }
            i.listaProduto = listaprodutoAB

        }
        return listaGrupoAB
    }
    fun confereSeExisteItens():Boolean{


        val query = "SELECT  * FROM  Tb_GrupoAB"
        val cursor = dbGrupoProduto.readableDatabase.rawQuery(query, null)
        var existemItem =   cursor.moveToFirst()
        cursor.close()
        dbGrupoProduto.close()

        return existemItem
    }
}
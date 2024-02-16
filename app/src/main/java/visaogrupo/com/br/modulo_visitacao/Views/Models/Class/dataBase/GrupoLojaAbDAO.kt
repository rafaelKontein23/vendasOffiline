package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase


import android.content.ContentValues
import android.content.Context
import com.google.gson.Gson
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.GrupoLojaAb
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.GrupoProgressiva

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

                for (i in gsonGrupo.GrupoFilho){
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
}
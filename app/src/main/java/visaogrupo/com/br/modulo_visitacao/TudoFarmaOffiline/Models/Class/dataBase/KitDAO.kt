package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Kit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitProtudos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitTituloPreco

class KitDAO (context: Context){
    val dbKit = DataBaseHelber(context).writableDatabase
    fun insertKit(kit: Kit){
        try {
            val valueskits = ContentValues()
            valueskits.put("Kit_id",kit.kitId)
            valueskits.put("Kit_codigo",kit.kitId)
            valueskits.put("kit_Nome",kit.kitNome)

            dbKit.insertOrThrow("TB_Kits",null,valueskits)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun insertKitProduto(kitProtudos: KitProtudos){
        try {
            val valaresProtudo = ContentValues()
            valaresProtudo.put("Kit_id",kitProtudos.kitID)
            valaresProtudo.put("Produto_Codigo",kitProtudos.produtoCodigo)
            valaresProtudo.put("Produto_Nome",kitProtudos.produtoNome)
            valaresProtudo.put("Fabricante",kitProtudos.fabricante)
            valaresProtudo.put("Desconto",kitProtudos.desconto)
            valaresProtudo.put("Quantidade",kitProtudos.quantidade)
            valaresProtudo.put("Imagem",kitProtudos.imagem)

            dbKit.insertOrThrow("Tb_Kit_Produtos",null,valaresProtudo)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun listaProdutos(query:String,kitTituloPreco:KitTituloPreco):KitTituloPreco{
        val curso  = dbKit.rawQuery(query,null)
        val listaProtudos= mutableListOf<KitProtudos>()

        while (curso.moveToNext()){
            val kitId = curso.getInt(0)
            val produtoCod = curso.getString(1)
            val produtoNome = curso.getString(2)
            val fabricante = curso.getString(3)
            val desconto = curso.getDouble(4)
            val quantidade = curso.getInt(5)
            val imagem = curso.getString(6)
            val valorPf = curso.getDouble(7)
            val valorBarra = curso.getString(8)

            var base64Imagens=  ""

            if (curso.getString(9) != null && !curso.getString(9).equals("null")){
                base64Imagens = curso.getString(9)
            }

            val kitProduto = KitProtudos(kitId,produtoCod,produtoNome,fabricante,desconto,
                quantidade,imagem,valorPf,valorBarra, base64Imagens)
            listaProtudos.add(kitProduto)

        }
        var valorTotal = 0.0
        for (i in  listaProtudos){
            valorTotal += i.valorTotal * i.quantidade
        }
        var  valorTotDesc = 0.0
        for (i in listaProtudos){
            val valorDesc = i.valorTotal - (i.valorTotal * (i.desconto/100))

            valorTotDesc +=  valorDesc * i.quantidade
        }

        kitTituloPreco.listaKitProdutos = listaProtudos
        kitTituloPreco.De  = valorTotal
        kitTituloPreco.Por = valorTotDesc


        return kitTituloPreco
    }


    fun listarItennkitTitulo(query:String):MutableList<KitTituloPreco>{
        val curso  = dbKit.rawQuery(query,null)
        val listaKitPreco = mutableListOf<KitTituloPreco>()
        while (curso.moveToNext()){
            val kitNomeTitulo  = curso.getString(0)
            val kitCodigo = curso.getInt(1)
            val estaNoCarrinhoKit = curso.getInt(2)
            var quantidade = 0
            if (curso.getInt(3) != null){
                quantidade = curso.getInt(3)
            }
            val kitTituloPreco = KitTituloPreco(kitNomeTitulo,kitCodigo,0.0,0.0, estaNoCarrinho =  estaNoCarrinhoKit, quantidade = quantidade)
            listaKitPreco.add(kitTituloPreco)

        }
        return listaKitPreco
    }
}
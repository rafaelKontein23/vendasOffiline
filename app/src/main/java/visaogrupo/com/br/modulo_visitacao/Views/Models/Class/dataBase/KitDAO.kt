package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Kit
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.KitProtudos

class KitDAO (context: Context){
    val dbKit = DataBaseHelber(context).writableDatabase
    fun insertKit(kit: Kit){
        try {
            val valueskits = ContentValues()
            valueskits.put("Kit_id",kit.kitId)
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
}
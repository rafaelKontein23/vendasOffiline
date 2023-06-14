package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.Context
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface.IProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Progressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaLista

class ProgresivaDAO(context: Context):IProgressiva {
    val  dbProgressiva = DataBaseHelber(context)
    override fun listarProgressiba(query: String): MutableList<ProgressivaLista> {
        val listaProgresiva = mutableListOf<ProgressivaLista>()
        val cursor = dbProgressiva.readableDatabase.rawQuery(query,null)

        while (cursor.moveToNext()){
         val Produto_codigo = cursor.getInt(0)
         val CaixaPadrao	= cursor.getInt(1)
         val PMC = cursor.getDouble(2)
         val PF	= cursor.getDouble(3)
         val Valor = cursor.getDouble(4)
         val Quantidade	 = cursor.getInt(5)
         val Desconto  = cursor.getDouble(6)
         val ProgressivaLista = ProgressivaLista(Produto_codigo,CaixaPadrao,PMC,PF,Valor,Quantidade,Desconto)
         listaProgresiva.add(ProgressivaLista)
        }
        return listaProgresiva

    }

    override fun insertProgressiva(): Boolean {
        TODO("Not yet implemented")
    }
}
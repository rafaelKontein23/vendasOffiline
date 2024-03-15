package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase

import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface.IClientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Notificacoes

class NotificacaoDAO(context: Context) {
    val dbNotificacoes = DataBaseHelber(context).writableDatabase
     fun insert(notificacoes: Notificacoes): Boolean {
         try {
             val valoresNoticaocoes = ContentValues()
             valoresNoticaocoes.put("titulo",notificacoes.titulo)
             valoresNoticaocoes.put("mensagem",notificacoes.mensagem)
             valoresNoticaocoes.put("Tipo",notificacoes.tipo)
             valoresNoticaocoes.put("json",notificacoes.json)
             valoresNoticaocoes.put("pedidoID",notificacoes.pedidoID)
             valoresNoticaocoes.put("visualizado",notificacoes.visualizado)
             valoresNoticaocoes.put("dataChegada",notificacoes.diaChegada)
             dbNotificacoes.insertOrThrow("TB_Notificacao",null,valoresNoticaocoes)

             return true
         }catch (e:Exception){
             e.printStackTrace()
             return false
         }
    }

    fun atualizar(): Boolean {
        TODO("Not yet implemented")
    }

    fun remover(Clientes: Clientes): Boolean {
        TODO("Not yet implemented")
    }

    fun listar(): MutableList<Notificacoes> {
        val listaNoticacoes = mutableListOf<Notificacoes>()
        val query = "Select * FROM TB_Notificacao"

        val cursor = dbNotificacoes.rawQuery(query,null)
        while (cursor.moveToNext()){
             val notificaoID = cursor.getInt(0)
             val titulo     = cursor.getString(1)
             val mensagem   = cursor.getString(2)
             val tipo       = cursor.getInt(3)
             val json       = cursor.getString(4)
             val pedidoID   = cursor.getLong(5)
             val visualizado = cursor.getInt(6)
             val dataHora    = cursor.getString(7)
             val notificacoes = Notificacoes(notificaoID,titulo,mensagem,tipo,json,pedidoID,visualizado,dataHora)
             listaNoticacoes.add(notificacoes)
        }
        return listaNoticacoes
    }

    fun countar(context: Context): Int {
        TODO("Not yet implemented")
    }
}
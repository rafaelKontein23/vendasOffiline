package visaogrupo.com.br.modulo_visitacao.Views.Interfaces.DAIInterface

import visaogrupo.com.br.modulo_visitacao.Views.Models.ProgressivaLista

interface IProgressiva {

    fun listarProgressiba(query:String):MutableList<ProgressivaLista>

    fun insertProgressiva():Boolean
}
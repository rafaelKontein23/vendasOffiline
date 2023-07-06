package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.DAIInterface

import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProgressivaLista

interface IProgressiva {

    fun listarProgressiba(query:String,Persona:Boolean):MutableList<ProgressivaLista>

    fun insertProgressiva(ProtudoProgressiva: ProdutoProgressiva, valorno:Double, quatidade:Int, desconto:Double):Boolean

    fun deleteProgressiva(query:String,desconto: Double,quantida:Int)
}
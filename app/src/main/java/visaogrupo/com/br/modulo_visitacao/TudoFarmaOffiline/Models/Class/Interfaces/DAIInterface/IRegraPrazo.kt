package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.DAIInterface

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PrazoMedioXValor


interface IRegraPrazo {

    fun insertRegraProgressiva(list : MutableList<PrazoMedioXValor>)

}
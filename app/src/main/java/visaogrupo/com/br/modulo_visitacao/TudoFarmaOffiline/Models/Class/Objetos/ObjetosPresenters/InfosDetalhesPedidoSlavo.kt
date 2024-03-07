package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ObjetosPresenters

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CustomSpinerFormDePag
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.OperadorLogistico

data class InfosDetalhesPedidoSalvo(
    val listaFormPag: MutableList<CustomSpinerFormDePag>,
    val listaOplspner:MutableList<OperadorLogistico>
)

package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos

data class GrupoLojaAb(
    val CODLISTAPRECOSYNC: Int,
    val Grupo: String,
    val GrupoFilho: List<GrupoFilho>,
    val Grupo_Codigo: Int,
    val Loja_id: Int,
    val NomeGrupo: String,
    val Porc: Double,
    val Prioridade: Int
)
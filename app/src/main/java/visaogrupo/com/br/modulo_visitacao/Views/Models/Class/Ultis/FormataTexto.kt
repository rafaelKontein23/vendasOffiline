package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

class FormataTexto {

    companion object{
        fun formataCnpj(cnpjSelecionado:String):String{
            try {
                val cnpj = cnpjSelecionado.substring(0,2)+"."+cnpjSelecionado.substring(2,5)+
                        "."+cnpjSelecionado.substring(5,8)+"/"+cnpjSelecionado.substring(8,12) +"-"+
                        cnpjSelecionado.substring(12,14);

                return cnpj
            }catch (e:Exception){
                e.printStackTrace()
                return cnpjSelecionado
            }


        }
    }
}
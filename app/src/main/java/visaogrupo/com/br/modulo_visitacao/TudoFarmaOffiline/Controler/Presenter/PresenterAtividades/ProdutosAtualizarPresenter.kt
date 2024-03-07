package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.Context
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProdutosDAO

class ProdutosAtualizarPresenter {

    fun filtrarListaProtudos(listaFiltroID: MutableList<Int>,filtro:String, pedido: PedidoFinalizado,context: Context):Triple<MutableList<ProdutoProgressiva>,List<Char>,String>{
        var listaProtudosFiltro = mutableListOf<ProdutoProgressiva>()

        var idfiltros = ""
        for (i in 0 until  listaFiltroID.size){
            if (i+1 != listaFiltroID.size){
                idfiltros +=listaFiltroID[i].toString() +","
            }else{
                idfiltros +=listaFiltroID[i].toString()
            }

        }

        var filtroIdNull = ""
        if (listaFiltroID != null && !listaFiltroID.isEmpty()){
            filtroIdNull= "and prod_cod In (" +
                    "SELECT produto_codigo FROM TB_Filtros Filtro \n" +
                    "inner join TB_FiltroProdutos FiltroProdutos on Filtro.filtroid = FiltroProdutos.filtroid \n" +
                    "where Filtro.filtroid in (${idfiltros})\n" +
                    ")"
        }
       val queryFiltro = "SELECT \n" +
                "Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Estoque.Quantidade,\n" +
                "Progressiva.pf,imagens.imagembase64, Estoque.centro, Estoque.quantidade as qtdEstoque, PedProd.valor, \n" +
                "(CASE WHEN PedProd.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoPedido, PedProd.quantidade as QtdPedido,Repasse.*, Progressiva.DescontoMaximo,Progressiva.Desconto_Min\n" +
                "FROM TB_produtos Produtos \n" +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                "LEFT JOIN TBPedidosFinalizados AS Ped on Ped.cliente_id = CLI.empresa_id and Ped.loja_id = Progressiva.loja_id AND PEd.pedidoid = ${pedido!!.pedidoID}\n" +
                "LEFT JOIN TB_Produtos_Pedidos_Finalizado PedProd on PedProd.PedidoID = Ped.pedidoid \n" +
                "                                                  and PedProd.produto_codigo = Progressiva.prod_cod \n" +
                "LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+

                "where Progressiva.loja_id = ${pedido!!.lojaId} and Progressiva.uf = '${pedido!!.uf}' AND ClI.Empresa_id = ${pedido!!.clienteId}\n" +
                filtroIdNull+
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo order by ${filtro}"


        val  produtos = ProdutosDAO(context)
        listaProtudosFiltro = produtos.litarPedidosProdutos(queryFiltro)


        val letrasIniciais = listaProtudosFiltro.map { it.nome.first().toUpperCase() }
        val letrasUnicas = letrasIniciais.distinct()

        return Triple(listaProtudosFiltro,letrasUnicas, queryFiltro)
    }


    fun protudosIniciais(pedido: PedidoFinalizado,context: Context,listaProtudos:MutableList<ProdutoProgressiva>):Triple<MutableList<ProdutoProgressiva>,List<Char>,String>{
       var listaProtudosfun = listaProtudos
       var query = "SELECT \n" +
                "Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo,Produtos.caixapadrao,Progressiva.pmc,Estoque.Quantidade,\n" +
                "Progressiva.pf,imagens.imagembase64, Estoque.centro, Estoque.quantidade as qtdEstoque, PedProd.valor, \n" +
                "(CASE WHEN PedProd.Quantidade > 0 THEN 1 ELSE 0 END) AS EstaNoPedido, PedProd.quantidade as QtdPedido,Repasse.*, Progressiva.DescontoMaximo,Progressiva.Desconto_Min\n" +
                "FROM TB_produtos Produtos \n" +
                "inner join TB_Progressiva Progressiva on Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                "INNER JOIN TB_clientes CLI ON CLI.uf = Progressiva.uf AND CLI.codigo = PROGRESSIVA.codigo\n" +
                "LEFT join TB_Imagens imagens on Produtos.barra = imagens.barra \n" +
                "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra AND Estoque.centro = CLI.codestoque \n" +
                "LEFT JOIN TBPedidosFinalizados AS Ped on Ped.cliente_id = CLI.empresa_id and Ped.loja_id = Progressiva.loja_id AND PEd.pedidoid = ${pedido!!.pedidoID}\n" +
                "LEFT JOIN TB_Produtos_Pedidos_Finalizado PedProd on PedProd.PedidoID = Ped.pedidoid \n" +
                " and PedProd.produto_codigo = Progressiva.prod_cod \n" +
                "LEFT join TB_Repasse  Repasse ON PROGRESSIVA.prod_cod = Repasse.material AND Repasse.UF = ClI.uf AND Repasse.centro = CLI.codestoque "+
                "where Progressiva.loja_id = ${pedido!!.lojaId} and Progressiva.uf = '${pedido!!.uf}' AND ClI.Empresa_id = ${pedido!!.clienteId}\n" +
                "group by Produtos.nome, Produtos.Apresentacao, Produtos.barra,Produtos.Imagem,Produtos.Produto_codigo \n" +
                "order by 1"



        val produtos = ProdutosDAO(context)
        listaProtudosfun = produtos.litarPedidosProdutos(query)
        val letrasIniciais = listaProtudosfun.map { it.nome.first().toUpperCase() }
        val letrasUnicas = letrasIniciais.distinct()

        return Triple(listaProtudosfun,letrasUnicas,query)

    }

    fun atualizaValoresProgrress(listaProtudos:MutableList<ProdutoProgressiva>):Double{
        var valorSomado =0.0
        val listasomacarrinho = listaProtudos.filter { it.valorcarrinho != null }
        for ( i  in  0 until listasomacarrinho.size){
            val soma = listasomacarrinho[i].valorTotal
            valorSomado = valorSomado + soma
        }

        return valorSomado
    }
}
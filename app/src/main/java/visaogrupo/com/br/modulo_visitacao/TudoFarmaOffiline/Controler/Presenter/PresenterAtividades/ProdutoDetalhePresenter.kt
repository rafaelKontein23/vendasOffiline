package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_act_protudo_detalhe.edtQuantidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Carrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutoProgressiva
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProgressivaLista
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProgresivaDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.ProgressivaAdpter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActProtudoDetalhe
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ProdutoDetalhePresenter {

    suspend fun buscaProgressiva(baseContext:Context,
                         listaProgressiva:MutableList<ProgressivaLista>,
                         listaProgressivaOriginal:MutableList<ProgressivaLista>,
                         lojaSelecionada: Lojas,
                         clienteSelecionado: Clientes,
                         protudoSelecionado: ProdutoProgressiva,
                         estaNoCarrinho:Int,

    ):Triple<MutableList<ProgressivaLista>,MutableList<ProgressivaLista>, String>{
        var quantidae = ""
        var listaProgressivafun = listaProgressiva
        var listaProgressivaOriginalFun = listaProgressivaOriginal

        val corro = CoroutineScope(Dispatchers.IO).launch {
            val ProgresivaDAO = ProgresivaDAO(baseContext)
            var  lista_progressivapresona = mutableListOf<ProgressivaLista>()
            val job1 = launch {
                val query = "SELECT DISTINCT Produtos.Produto_codigo, Produtos.caixapadrao, Progressiva.pmc, Progressiva.pf, Progressiva.valor, Progressiva.Quantidade, Progressiva.desconto,Progressiva.DescontoMaximo\n" +
                        "FROM TB_produtos Produtos\n" +
                        "INNER JOIN TB_Progressiva Progressiva ON Produtos.Produto_codigo = Progressiva.Prod_cod\n" +
                        "LEFT JOIN TB_Estoque Estoque ON Estoque.EAN = Produtos.barra\n" +
                        "WHERE Progressiva.loja_id = ${lojaSelecionada.loja_id}\n" +
                        "  AND Progressiva.uf = '${clienteSelecionado.UF}'\n" +
                        "  AND Produtos.Produto_codigo = ${protudoSelecionado.ProdutoCodigo}\n" +
                        "ORDER BY 6"
                listaProgressivafun = ProgresivaDAO.listarProgressiba(query,false)
                listaProgressivaOriginalFun = ProgresivaDAO.listarProgressiba(query,false)
            }

            val  job2 = launch {
                val queryPersona = "SELECT * FROM Tb_Progressiva_Personalizada WHERE Tb_Progressiva_Personalizada.column_produto_codigo = ${protudoSelecionado.ProdutoCodigo} "
                lista_progressivapresona = ProgresivaDAO.listarProgressiba(queryPersona,true)
            }
            job1.join()
            job2.join()
            if (estaNoCarrinho == 1){
                quantidae = protudoSelecionado.quantidadeCarrinho.toString()
            }else{
                quantidae =  listaProgressivafun[0].quantidade.toString()

            }
            listaProgressivafun.addAll(lista_progressivapresona)
        }
        corro.join()
        return Triple(listaProgressivafun,listaProgressivaOriginalFun,quantidae)

    }



    fun subtrairPrutudos(valortotal:Double,
                         valorProtudo:Double,
                         caixapadrao:Boolean,
                         quantidade: Int,
                         repasse:Double):String{

        var valorProdutoItem = valorProtudo
        if(repasse > 0.0){
            var discount = valorProdutoItem - (valorProdutoItem * (repasse) / 100);
            valorProdutoItem = discount;


            val symbols = DecimalFormatSymbols()
            symbols.decimalSeparator = ','
            val formato = DecimalFormat("#,##0.00", symbols)

            val numeroFormatado = formato.format(valorProdutoItem)

            valorProdutoItem = numeroFormatado.replace(",",".").toDouble()

        }else{
            valorProdutoItem = valorProtudo
        }
        if(caixapadrao){
            val valortotalCal  =  quantidade *valorProdutoItem
            return FormataValores.formatarParaMoeda(valortotalCal)

        }else{
            val valorAdicionadosub = valortotal - valorProdutoItem
             return FormataValores.formatarParaMoeda(valorAdicionadosub)

        }
    }



    fun calculaCaixaparaoMenos (quantidadecaixapradrao:Int,
                                quatidadeAdicionadaCap:Int,
                                listaProgressiva:MutableList<ProgressivaLista>):Int{
        var  quatidadeAdicionadaCapfun = quatidadeAdicionadaCap

        val resto = quatidadeAdicionadaCapfun % quantidadecaixapradrao

        if ( resto == 0) {
            quatidadeAdicionadaCapfun -= quantidadecaixapradrao
            return quatidadeAdicionadaCapfun

        } else {
            quatidadeAdicionadaCapfun -= resto
            return quatidadeAdicionadaCapfun

        }
        if ( quatidadeAdicionadaCapfun == 0){
            quatidadeAdicionadaCapfun = listaProgressiva[0].quantidade
            return quatidadeAdicionadaCapfun
        }
    }



    fun somaProdutos(quantidade:Int,valorProtudo:Double,repasse:Double):String{
        var valorProdutoItem = valorProtudo
        if(repasse > 0.0){
            var discount = valorProdutoItem - (valorProdutoItem * (repasse) / 100);
            valorProdutoItem = discount;

            val formato = DecimalFormat("#.##")
            val numeroFormatado = formato.format(valorProdutoItem)
            valorProdutoItem = numeroFormatado.replace(",",".").toDouble()

        }else{
            valorProdutoItem = valorProtudo
        }
        val valorAdicionado = quantidade * valorProdutoItem
        return FormataValores.formatarParaMoeda(valorAdicionado)
    }

    fun calculaCaixaparaoMais(quatidadeAdicionadaCap:Int,quantidadecaixapradrao:Int):Int{
        var quatidadeAdicionadaCapFun =quatidadeAdicionadaCap
        quatidadeAdicionadaCapFun += quantidadecaixapradrao - ((quatidadeAdicionadaCapFun %   quantidadecaixapradrao.toInt() ))
        return quatidadeAdicionadaCapFun
    }




    fun adiconarAoCarrinho(baseContext:Context,
                           listaProgressivaOriginal:MutableList<ProgressivaLista>,
                           lojaSelecionada: Lojas,
                           clienteSelecionado: Clientes,
                           protudoSelecionado: ProdutoProgressiva,
                           estaNoCarrinho:Int,
                           pedidodID:Int,
                           chekANR:Boolean,
                           valortotal:Double,
                           pedido:Boolean,
                           estaNoPedidoSalvo:Boolean,
                           imagemBase64:String,
                           quantidade: Int,
                           userId:Int){

        val  daataformat = DataAtual()
        val  data = daataformat.recuperaData()
        var anr =  0
        if (chekANR){
            anr = 1
        }else{
            anr = 0
        }
        var descontoOriginal = 0.0
        for (i in listaProgressivaOriginal){
            if (ActProtudoDetalhe.progressivaSelecionada.quantidadeSelecionada == i.quantidade){
                descontoOriginal = i.desconto
                break
            }
        }

        val  carrinho = Carrinho(
            lojaSelecionada.loja_id,
            clienteSelecionado.Empresa_id,
            protudoSelecionado.ProdutoCodigo,
            "",
             userId,
             clienteSelecionado.UF,
            0.0,
            0.0,
            0,
            protudoSelecionado.barra,
            quantidade,
            protudoSelecionado.valor.toDouble(),
            ActProtudoDetalhe.progressivaSelecionada.valorProgressivaSelecionada,
            protudoSelecionado.valor.toString().toDouble(),
            0,
            ActProtudoDetalhe.progressivaSelecionada.porcentagemProgressivaSelecionda,
            descontoOriginal,
            0.0,
            "",
            1,"",
            valortotal,
            protudoSelecionado.nome,
            lojaSelecionada.nome,
            clienteSelecionado.RazaoSocial,
            clienteSelecionado.CNPJ,data,
            lojaSelecionada.MinimoValor,
            imagemBase64,
            protudoSelecionado.PMC,
            clienteSelecionado.FormaPagamentoExclusiva,
            protudoSelecionado.caixapadrao,
            lojaSelecionada.Qtd_Minima_Operador,
            lojaSelecionada.Qtd_Maxima_Operador,
            anr,
            lojaSelecionada.RegraPrazoMedio,
            lojaSelecionada.LojaTipo,
            protudoSelecionado.centro,
            protudoSelecionado.porcentagem)


        val carrinhoDAO = CarrinhoDAO(baseContext)
        try {
            if (pedido){
                if (estaNoPedidoSalvo){
                    val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(baseContext)
                    pedidosFinalizadosDAO.atualizaProdutoPedidoValores(pedidodID,carrinho.produtoCodigo,carrinho)
                    Toast.makeText(baseContext,"Item atualizado com sucesso nos pedidos", Toast.LENGTH_SHORT).show()

                }else{
                    val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(baseContext)
                    pedidosFinalizadosDAO.adicionaItemNoPedido(pedidodID,carrinho)
                    Toast.makeText(baseContext,"Item adicionado com sucesso nos pedidos", Toast.LENGTH_SHORT).show()
                }
            }else {
                if (estaNoCarrinho  == 1){
                    carrinhoDAO.atualizaItemCarrinho(carrinho)
                    Toast.makeText(baseContext,"Item atualizado com sucesso ao carrinho", Toast.LENGTH_SHORT).show()

                }else{
                    carrinhoDAO.insertCarrinho(carrinho)
                    Toast.makeText(baseContext,"Item adicionado com sucesso ao carrinho", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            val dialogErro = DialogErro()
            dialogErro.Dialog(baseContext,"Ops!", "Algo deu errado","Ok",""){

            }
        }
    }

}
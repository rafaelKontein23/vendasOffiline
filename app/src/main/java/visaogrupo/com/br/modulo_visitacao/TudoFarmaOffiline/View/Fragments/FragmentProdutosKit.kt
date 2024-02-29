package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import visaogrupo.com.br.TudoFarmaOffiline.databinding.FragmentProdutosKitBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaValorTotalKitCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.CarrinhoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.KitTituloPreco
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataTexto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoKitDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.KitDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdpterTituloProdutoKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActCarrinhoKit

class FragmentProdutosKit (carrinhoVisible: carrinhoVisible) : Fragment(), AtualizaValorTotalKitCarrinho {
    private  lateinit var  binding: FragmentProdutosKitBinding

    lateinit var lojaSelecionada: Lojas
    lateinit var produtos: ProdutosDAO
    lateinit var clienteSelecionado: Clientes
    lateinit var   listaTitulo:MutableList<KitTituloPreco>
    lateinit  var login :Login
    val  carrinhoVisible = carrinhoVisible
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProdutosKitBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = binding.root

        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)
        val objetoSerializadoLogin = sharedPreferences?.getString("UserLogin", null)
        val gsonuserid = Gson()

        login =  gsonuserid.fromJson(objetoSerializadoLogin, Login::class.java)
        protudosIniciais()
        val valorMinimoFormat = String.format("%.2f",lojaSelecionada.MinimoValor)
        binding.valorMinimo.text  = "R$ " +valorMinimoFormat.replace(".",",")
        binding.lojasSelecionadas.text =lojaSelecionada.nome
        binding.textRazaosocialclienteSelecionado.text = clienteSelecionado.RazaoSocial
        binding.cnpjClienteSelecionado.text = FormataTexto.formataCnpj(clienteSelecionado.CNPJ)
        carrinhoVisible.carrinhoVisivel()

        binding.carrinhoProtudo.setOnClickListener {
             val intent = Intent(requireContext(),ActCarrinhoKit::class.java)
             intent.putExtra("carrinhoDetalhe",false)
             startActivity(intent)
        }
        binding.TotalCarrinho.setOnClickListener {
            val intent = Intent(requireContext(),ActCarrinhoKit::class.java)
            intent.putExtra("carrinhoDetalhe",false)
            startActivity(intent)
        }


        return view
    }

    fun protudosIniciais(){
       val queryTitulo = "SELECT  kits.kit_Nome , kits.Kit_codigo, CASE WHEN IFNULL(CarrinhoKit.kitcodigo,0 )>0 THEN 1 ELSE 0 END estaNoCaarinhoKit,CarrinhoKit.quantidade FROM TB_kits kits \n" +
               "INNER JOIN TB_KitxLoja KitxLoja ON KitxLoja.Kit_cod = kits.Kit_codigo  \n" +
               "INNER Join TB_KItXcliente KItXcliente ON KItXcliente.Kit_cod = kits.Kit_codigo\n" +
               "LEFT JOIN TB_CarrinhoKit CarrinhoKit  ON CarrinhoKit.loja_id = ${lojaSelecionada.loja_id} AND CarrinhoKit.kitCodigo = kits.Kit_codigo\n"+
               "WHERE KitxLoja.Loja_id = ${lojaSelecionada.loja_id} AND KItXcliente.CNPJ = '${clienteSelecionado.CNPJ}' "
        val kitDAO = KitDAO(requireContext())
        listaTitulo = kitDAO.listarItennkitTitulo(queryTitulo)

        for ((  index,kitTitulo) in listaTitulo.withIndex()){
            val  query = "SELECT kitProtudo.kit_id, kitProtudo.produto_codigo,kitProtudo.produto_nome," +
                    " kitProtudo.fabricante,kitProtudo.desconto,kitProtudo.quantidade, kitProtudo.imagem, kit_x_preco.pf, produtos.barra, imagens.imagembase64 \n " +
                    " FROM TB_KitxLoja kitxLoja \n" +
                    "INNER JOIN TB_Kits kits On kitxLoja.kit_cod = kits.kit_codigo \n" +
                    "INNER Join Tb_Kit_Produtos kitProtudo On kitProtudo.kit_id = kits.kit_codigo \n" +
                    "INNER Join TB_KItXcliente  KItXcliente On KItXcliente.kit_cod = kits.kit_codigo \n" +
                    "INNER JOIN TB_kit_x_preco  kit_x_preco ON kitProtudo.Produto_Codigo = kit_x_preco.Produto_codigo \n" +
                    "INNER Join TB_produtos produtos ON kitProtudo.produto_codigo = produtos.Produto_codigo "+
                    "LEFT JOIN TB_Imagens imagens ON imagens.barra = produtos.barra "+
                    "WHERE kitxLoja.Loja_id = ${lojaSelecionada.loja_id} And KItXcliente.cnpj = '${clienteSelecionado.CNPJ}' " +
                    "AND kit_x_preco.UF = '${clienteSelecionado.UF}' AND kit_x_preco.codlistaprecosync = ${clienteSelecionado.CODLISTAPRECOSYNC}"


           listaTitulo[index] = kitDAO.listaProdutos(query,kitTitulo)
        }
        val linearlayout = LinearLayoutManager(requireContext())
        val adpterTituloProdutoKit = AdpterTituloProdutoKit(listaTitulo, requireContext(),
            this,lojaSelecionada,clienteSelecionado,login.Usuario_id!!.toInt())
        binding.recyProtudoKit.layoutManager = linearlayout
        binding.recyProtudoKit.adapter = adpterTituloProdutoKit



    }

    override fun atualizaValor(carrinhoKit: CarrinhoKit) {

        if (carrinhoKit.quantidade == 0){
            val carrinhoKitDAO = CarrinhoKitDAO(requireContext())
            carrinhoKitDAO.excluirItem(carrinhoKit)
            binding.TotalCarrinho.text = "R$ " + "0,00"
            binding.quatidadeItens.text = "-"

        }else{
            val carrinhoKitDAO = CarrinhoKitDAO(requireContext())
            carrinhoKitDAO.verificaExiste(carrinhoKit)
            val (valorTotal,quantidade) = carrinhoKitDAO.PegarValor(carrinhoKit)
            val valorTotalFormat = String.format("%.2f",valorTotal)
            binding.TotalCarrinho.text = "R$ " + valorTotalFormat.replace(".",",")
            if (quantidade >= 999){
                binding.quatidadeItens.text = "999+"
            }else{
                binding.quatidadeItens.text = quantidade.toString()
            }
        }
   }

}
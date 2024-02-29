package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActCarrinhoKitBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoKitDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterCarrinhoKit

class ActCarrinhoKit : AppCompatActivity() {
    private  lateinit var binding : ActivityActCarrinhoKitBinding
    lateinit var lojaSelecionada: Lojas
    lateinit var produtos: ProdutosDAO
    lateinit var clienteSelecionado: Clientes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActCarrinhoKitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val gsonclientes = Gson()
        val objetoSerializado = sharedPreferences?.getString("LojaSelecionada", null)
        val objetoSerializadoCliente = sharedPreferences?.getString("ClienteSelecionado", null)
        lojaSelecionada =  gson.fromJson(objetoSerializado, Lojas::class.java)
        clienteSelecionado = gsonclientes.fromJson(objetoSerializadoCliente, Clientes::class.java)

        buscaItensCarrinhoKit()
        
    }
    fun buscaItensCarrinhoKit(){
        val queryBuscaItensCarrinhoKit = "SELECT * FROM TB_CarrinhoKit WHERE loja_id =${lojaSelecionada.loja_id} AND cliente_id =${clienteSelecionado.Empresa_id} "
        val carrinhoKitDAO = CarrinhoKitDAO(this)
        val listaCarrinhoKit = carrinhoKitDAO.buscaCarrinhoKit(queryBuscaItensCarrinhoKit)

         for((index,itemProtudo) in listaCarrinhoKit.withIndex()){
             val  queryItemCarrinhoKit = "SELECT ProdutoKit.*, imagens.imagembase64\n" +
                     "FROM TB_carrinhoProdutoKit ProdutoKit\n" +
                     "LEFT JOIN TB_Imagens imagens ON ProdutoKit.barra = imagens.barra\n" +
                     "WHERE ProdutoKit.loja_id = ${lojaSelecionada.loja_id} AND ProdutoKit.cliente_id = ${clienteSelecionado.Empresa_id} AND ProdutoKit.kitcodigo = ${itemProtudo.kitCodigo};\n"
             val listaKitProdutos = carrinhoKitDAO.buscaItensProdutoCarrinhoKit(queryItemCarrinhoKit,itemProtudo.kitCodigo)
             itemProtudo.listProdutoKit = listaKitProdutos
         }
        val adapterCarrinhoKIt = AdapterCarrinhoKit(listaCarrinhoKit, this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyKit.adapter = adapterCarrinhoKIt
        binding.recyKit.layoutManager = linearLayoutManager

    }
}
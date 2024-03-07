package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActCarrinhoKitBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Controler.Presenter.PresenterAtividades.CarrinhokitPresenter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ProdutosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterCarrinhoKit

class ActCarrinhoKit : AppCompatActivity() {
    private  lateinit var binding : ActivityActCarrinhoKitBinding
    lateinit var produtos: ProdutosDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActCarrinhoKitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.voltarCarrinho.setOnClickListener {
            onBackPressed()
        }

        val carrinhoKitPresenter = CarrinhokitPresenter()
        val listaCarrinhoKit = carrinhoKitPresenter.buscaItensCarrinhoKit(this)

        val adapterCarrinhoKIt = AdapterCarrinhoKit(listaCarrinhoKit, this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyKit.adapter = adapterCarrinhoKIt
        binding.recyKit.layoutManager = linearLayoutManager
        
    }
}
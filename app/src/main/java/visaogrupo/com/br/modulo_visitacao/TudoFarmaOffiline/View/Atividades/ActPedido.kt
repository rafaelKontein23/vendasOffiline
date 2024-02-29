package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_act_pedido.buscaPedido
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActPedidoBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Pedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidoDAO

class ActPedido : AppCompatActivity() , AtualizaCarrinho {
    private  lateinit var  binding: ActivityActPedidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciaTela()

    }

    override fun onBackPressed() {

        setResult(Activity.RESULT_OK)
        finish()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                iniciaTela()
            }
        }
    }

    override fun atualizaCarrinho() {
        binding.recyPedido.isVisible = false
        binding.semPedidos.isVisible = true
    }

    fun iniciaTela(){
        val pedidoDAO = PedidoDAO(this)
        val lisataPedidos = pedidoDAO.listaPedido()

        binding.recyPedido.isVisible = !lisataPedidos.isEmpty()
        binding.semPedidos.isVisible = lisataPedidos.isEmpty()
        val linearLayoutManager = LinearLayoutManager(this)
        val pedidoAdapterPedido =
            AdapterPedido(
                lisataPedidos,
                this, this, this,4
            )
        binding.recyPedido.layoutManager = linearLayoutManager
        binding.recyPedido.adapter = pedidoAdapterPedido

        binding.voltarPedidos.setOnClickListener{
            setResult(Activity.RESULT_OK)
            finish()
        }


        buscaPedido.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val busca=  s.toString()
                val  listaPedidosfiltro = mutableListOf <Pedido>()
                for (i in  lisataPedidos){
                    if (i.cnpj.contains(busca) || i.razaosocial.toLowerCase().contains(busca.toLowerCase()) || i.data.contains(busca)){
                        listaPedidosfiltro.add(i)
                    }
                }

                if (listaPedidosfiltro.isEmpty()){
                    binding.recyPedido.isVisible = false
                    binding.semFiltro.isVisible = true
                }else{
                    binding.recyPedido.isVisible = true
                    binding.semFiltro.isVisible =false
                    pedidoAdapterPedido.listaPedido = listaPedidosfiltro
                    pedidoAdapterPedido.notifyDataSetChanged()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}
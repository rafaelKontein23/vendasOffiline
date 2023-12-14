package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_act_pedido.buscaPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Pedido
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdapterPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidoDAO
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActPedidoBinding

class ActPedido : AppCompatActivity() {
    private  lateinit var  binding: ActivityActPedidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityActPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pedidoDAO = PedidoDAO(this)
        val lisataPedidos = pedidoDAO.listaPedido()
        val linearLayoutManager = LinearLayoutManager(this)
        val pedidoAdapterPedido =
            AdapterPedido(
                lisataPedidos,
                this
            )
        binding.recyPedido.layoutManager = linearLayoutManager
        binding.recyPedido.adapter = pedidoAdapterPedido
        binding.voltarPedidos.setOnClickListener{
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
package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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


    }
}
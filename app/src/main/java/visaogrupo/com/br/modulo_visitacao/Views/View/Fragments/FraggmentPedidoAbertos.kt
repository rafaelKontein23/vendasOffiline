package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_fraggment_pedido_abertos.semPedidos
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidoDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdapterPedido


class FraggmentPedidoAbertos : Fragment() , AtualizaCarrinho {

    lateinit var semPedidos:TextView;
    lateinit var recyAbertos:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = inflater.inflate(R.layout.fragment_fraggment_pedido_abertos, container, false)
        recyAbertos = view.findViewById<RecyclerView>(R.id.recyAbertos)
        semPedidos   = view.findViewById<TextView>(R.id.semPedidos)
        iniciaTela()

        return view
    }

    override fun atualizaCarrinho() {
       semPedidos.isVisible = true
       recyAbertos.isVisible = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                iniciaTela()
            }
        }
    }

    fun iniciaTela(){
        val pedidoDAO = PedidoDAO(requireContext())
        val lisataPedidos = pedidoDAO.listaPedido()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val pedidoAdapterPedido =
            AdapterPedido(
                lisataPedidos,
                requireContext(), this, requireActivity(),5
            )

        recyAbertos.layoutManager = linearLayoutManager
        recyAbertos.adapter = pedidoAdapterPedido
        semPedidos.isVisible = lisataPedidos.isEmpty()
    }
}
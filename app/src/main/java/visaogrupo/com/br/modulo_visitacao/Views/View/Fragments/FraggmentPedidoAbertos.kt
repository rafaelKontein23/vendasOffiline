package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidoDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdapterPedido


class FraggmentPedidoAbertos : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = inflater.inflate(R.layout.fragment_fraggment_pedido_abertos, container, false)
        val recyAbertos = view.findViewById<RecyclerView>(R.id.recyAbertos)
        val semPedidos   = view.findViewById<TextView>(R.id.semPedidos)

        val pedidoDAO = PedidoDAO(requireContext())
        val lisataPedidos = pedidoDAO.listaPedido()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val pedidoAdapterPedido =
            AdapterPedido(
                lisataPedidos,
                requireContext()
            )

        recyAbertos.layoutManager = linearLayoutManager
        recyAbertos.adapter = pedidoAdapterPedido
        semPedidos.isVisible = lisataPedidos.isEmpty()

        return view
    }


}
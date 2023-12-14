package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdpterPedidosFinalizado


class FragmentPedidosFechados(adpterPedidoFinalizado:AdpterPedidosFinalizado) : Fragment() {
    val AdpterPedidosFinalizado = adpterPedidoFinalizado


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view =inflater.inflate(R.layout.fragment_pedidos_fechados, container, false)

        val  recyclerPedido = view.findViewById<RecyclerView>(R.id.recyPedidoPendendes)
        val linearlayoutMeneger = LinearLayoutManager(requireContext())

        recyclerPedido.hasFixedSize()
        recyclerPedido.layoutManager = linearlayoutMeneger
        recyclerPedido.adapter = AdpterPedidosFinalizado
        return view
    }


}
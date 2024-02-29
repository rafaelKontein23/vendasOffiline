package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdpterPedidosFinalizado


class FragmentPedidosFechados(adpterPedidoFinalizado:AdpterPedidosFinalizado,listaPeidosFechados:MutableList<PedidoFinalizado> ) : Fragment() {
    val AdpterPedidosFinalizado = adpterPedidoFinalizado
    val listaPeidosFechados = listaPeidosFechados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view =inflater.inflate(R.layout.fragment_pedidos_fechados, container, false)

        val  recyclerPedido = view.findViewById<RecyclerView>(R.id.recyPedidoPendendes)
        val semPedidosFechados =view.findViewById<TextView>(R.id.semPedidosFechados)



        if (listaPeidosFechados.isEmpty()){
            recyclerPedido.isVisible = false
            semPedidosFechados.isVisible = true
        }else {
            recyclerPedido.isVisible = true
            semPedidosFechados.isVisible = false
            val linearlayoutMeneger = LinearLayoutManager(requireContext())
            recyclerPedido.hasFixedSize()
            recyclerPedido.layoutManager = linearlayoutMeneger
            recyclerPedido.adapter = AdpterPedidosFinalizado
        }

        return view
    }


}
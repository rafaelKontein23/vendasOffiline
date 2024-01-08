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
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaPedido
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdpterPedidosFinalizado

class FragmentPedidosPendendes( adpterPedidoFinalizado:AdpterPedidosFinalizado,listaPedidos:List<PedidoFinalizado>) : Fragment() {
    val AdpterPedidosFinalizado = adpterPedidoFinalizado
    val listaPedidos = listaPedidos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos_pendendes, container, false)
        val  recyclerPedido = view.findViewById<RecyclerView>(R.id.recyPedido)
        val semPedidos = view.findViewById<TextView>(R.id.semPedidos)
        if (listaPedidos.isEmpty()){
            semPedidos.isVisible = true
            recyclerPedido.isVisible = false

        }else{
            semPedidos.isVisible = false
            recyclerPedido.isVisible = true
            val linearlayoutMeneger = LinearLayoutManager(requireContext())

            recyclerPedido.hasFixedSize()
            recyclerPedido.layoutManager = linearlayoutMeneger
            recyclerPedido.adapter = AdpterPedidosFinalizado
        }



        return view
    }

}
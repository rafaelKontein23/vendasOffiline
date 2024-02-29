package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaPedido
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.IniciaPedidoDetalhes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.MostraLoad
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.VaiParaEnviados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterViewPagerPedidos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdpterPedidosFinalizado


class FragmentPedidos(mostraLoad: MostraLoad,carrinhoVisible: carrinhoVisible) : Fragment(), AtualizaPedido , VaiParaEnviados, IniciaPedidoDetalhes{
        val  content = this
        var  adapterViewPagerPedidos:AdapterViewPagerPedidos? = null
        var adpterPedidoFinalizado :AdpterPedidosFinalizado?= null
        var adpterPedidoFinalizadoEnviado :AdpterPedidosFinalizado?= null
        val mostraLoad = mostraLoad
        lateinit var  arrastaParaLado:ViewPager2
        val  carrinhoVisible = carrinhoVisible
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val  view = inflater.inflate(R.layout.fragment_pedidos, container, false)
        arrastaParaLado = view.findViewById<ViewPager2>(R.id.arrastaParaLado);
        val  viewFechado = view.findViewById<View>(R.id.viewFechado)
        val  viewAberto = view.findViewById<View>(R.id.viewAberto)
        val  abertos = view.findViewById<TextView>(R.id.abertos)
        val  fechado = view.findViewById<TextView>(R.id.fechado)
        val pedidosAbertosCarrinho = view.findViewById<TextView>(R.id.abertosPendentes)
        val  viewAbertosPedendes = view.findViewById<View>(R.id.viewAbertosPedendes)
        iniciaInterface()

        carrinhoVisible.carrinhoVisivel()
        abertos.setOnClickListener {
            arrastaParaLado.setCurrentItem(0,true)
            visivelSelecionado(abertos,fechado)
            viewFechado.isVisible = false
            viewAbertosPedendes.isVisible =false
            viewAberto.isVisible= true

        }

        fechado.setOnClickListener {
            arrastaParaLado.setCurrentItem(1,true)

            visivelSelecionado(fechado,abertos)
            viewFechado.isVisible = true
            viewAberto.isVisible= false
            viewAbertosPedendes.isVisible =false

        }
        pedidosAbertosCarrinho.setOnClickListener {
            arrastaParaLado.setCurrentItem(2,true)

            visivelSelecionado(pedidosAbertosCarrinho,abertos)
            fechado.setTextColor(Color.parseColor("#5483AB"))

            viewFechado.isVisible = false
            viewAberto.isVisible= false
            viewAbertosPedendes.isVisible =true

        }

        arrastaParaLado.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1){
                    visivelSelecionado(fechado,abertos)
                    viewFechado.isVisible = true
                    viewAberto.isVisible= false
                    viewAbertosPedendes.isVisible =false

                }else if (position == 2){
                    visivelSelecionado(pedidosAbertosCarrinho,abertos)
                    fechado.setTextColor(Color.parseColor("#5483AB"))

                    viewFechado.isVisible = false
                    viewAberto.isVisible= false
                    viewAbertosPedendes.isVisible =true
                }else{
                    visivelSelecionado(abertos,fechado)
                    viewFechado.isVisible = false
                    viewAberto.isVisible= true
                    viewAbertosPedendes.isVisible =false

                }
            }
        })



        return view
    }
   fun visivelSelecionado(textVisivel:TextView,textinvisivek:TextView){
       textVisivel.setTextColor(Color.parseColor("#004682"))

       textinvisivek.setTextColor(Color.parseColor("#5483AB"))
   }

    override fun atualizaPedidos() {
        CoroutineScope(Dispatchers.Main).launch {
            val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(requireContext())
            val  listaPedidos = pedidosFinalizadosDAO.listarPedidos(0)
            adpterPedidoFinalizado?.listaPedido = listaPedidos
            adpterPedidoFinalizado?.notifyDataSetChanged()


            val  pedidosFinalizadosDAOEnviado = PedidosFinalizadosDAO(requireContext())
            val  listaPedidosEnviado = pedidosFinalizadosDAOEnviado.listarPedidos(1)
            adpterPedidoFinalizadoEnviado?.listaPedido = listaPedidosEnviado
            adpterPedidoFinalizadoEnviado?.notifyDataSetChanged()

        }
    }

    override fun vaiparaEnviados() {
         val  pedidosFinalizadosDAOEnviado = PedidosFinalizadosDAO(requireContext())
         val  listaPedidosEnviado = pedidosFinalizadosDAOEnviado.listarPedidos(1)
         adpterPedidoFinalizadoEnviado?.listaPedido = listaPedidosEnviado
         adpterPedidoFinalizadoEnviado?.notifyDataSetChanged()
         arrastaParaLado.setCurrentItem(1)
    }

    fun iniciaInterface(){
        adapterViewPagerPedidos = AdapterViewPagerPedidos(getChildFragmentManager(), lifecycle)

        val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(requireContext())
        val  listaPedidos = pedidosFinalizadosDAO.listarPedidos(0)
        adpterPedidoFinalizado = AdpterPedidosFinalizado(listaPedidos,requireContext(), this, mostraLoad, true, this,0, this)

        val  pedidosFinalizadosDAOEnviados = PedidosFinalizadosDAO(requireContext())
        val  listaPedidosEnviados = pedidosFinalizadosDAOEnviados.listarPedidos(1)
        adpterPedidoFinalizadoEnviado = AdpterPedidosFinalizado(listaPedidosEnviados,requireContext(), this,mostraLoad,false,this,1, this)

        adapterViewPagerPedidos!!.addFragment(FragmentPedidosPendendes(adpterPedidoFinalizado!!,listaPedidos))
        adapterViewPagerPedidos!!.addFragment(FragmentPedidosFechados(adpterPedidoFinalizadoEnviado!!, listaPedidosEnviados))
        adapterViewPagerPedidos!!.addFragment(FraggmentPedidoAbertos())
        arrastaParaLado.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        arrastaParaLado.adapter = adapterViewPagerPedidos
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK ) {
                val  pedidosFinalizadosDAO = PedidosFinalizadosDAO(requireContext())
                val  listaPedidos = pedidosFinalizadosDAO.listarPedidos(0)
                adpterPedidoFinalizado?.listaPedido = listaPedidos
                adpterPedidoFinalizado?.notifyDataSetChanged()
            }
        }
    }

    override fun inicia(intent: Intent) {
       startActivityForResult(intent,3)

    }

}
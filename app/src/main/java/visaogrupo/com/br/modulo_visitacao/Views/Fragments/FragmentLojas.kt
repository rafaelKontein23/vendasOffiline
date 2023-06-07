package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.LojasDAO

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentLojas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_lojas, container, false)
        val listalojas = LojasDAO(requireContext())
        val querylojasClientes = "SELECT LojCli.empresa_id, Lojas.*" +
                "FROM TB_lojas Lojas" +
                "inner join TB_lojaporcliente LojCli on Lojas.Loja_id = LojCli.loja_id" +
                "WHERE LojCli.empresa_id = 77713"
        listalojas.listarlojas(requireContext(),1,querylojasClientes)
        return  view;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLojas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentLojas
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentProtudos
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.carrinhoVisible

class MudarFragment {

     fun openFragmentLojas(fragmentManager: FragmentManager, idframe:Int,trocarcorItem: TrocarcorItem,carrinhoVisible: carrinhoVisible) {
        val fragmentLojas = FragmentLojas(trocarcorItem,carrinhoVisible)
        fragmentManager.beginTransaction()
            .replace(idframe, fragmentLojas).addToBackStack(null)
            .commit()
    }

    fun openFragmentProtudos(fragmentManager: FragmentManager, idframe:Int,carrinhoVisible: carrinhoVisible) {
        val fragmentProtudos = FragmentProtudos(carrinhoVisible)
        fragmentManager.beginTransaction()
            .replace(idframe, fragmentProtudos).addToBackStack(null)
            .commit()
    }
}
package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentLojas

class MudarFragment {

     fun openFragmentWithFragmentManager(fragmentManager: FragmentManager, idframe:Int) {
        val fragmentLojas = FragmentLojas()
        fragmentManager.beginTransaction()
            .replace(idframe, fragmentLojas).addToBackStack(null)
            .commit()
    }
}
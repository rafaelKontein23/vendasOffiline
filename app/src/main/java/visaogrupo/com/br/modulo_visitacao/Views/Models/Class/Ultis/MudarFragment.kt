package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentLojas
import visaogrupo.com.br.modulo_visitacao.Views.View.Fragments.FragmentProtudos

class MudarFragment {

     fun openFragmentLojas(fragmentManager: FragmentManager, idframe:Int, trocarcorItem: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TrocarcorItem, carrinhoVisible: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible, atualizaCarrinho: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho) {
        val fragmentLojas = FragmentLojas(trocarcorItem,carrinhoVisible,atualizaCarrinho)
         val fragmentManager: FragmentManager = fragmentManager
         val fragmentTransaction: FragmentTransaction =
             fragmentManager.beginTransaction()
         fragmentTransaction.setCustomAnimations(
             android.R.anim.slide_in_left,
             android.R.anim.slide_out_right
         )

         fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentLojas, "h")
         fragmentTransaction.addToBackStack("h")
         fragmentTransaction.commit()
    }

    fun openFragmentProtudos(fragmentManager: FragmentManager, idframe:Int, carrinhoVisible: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible, atualizaCarrinho: visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho) {
        val fragmentProtudos = FragmentProtudos(carrinhoVisible,atualizaCarrinho)
        val fragmentManager: FragmentManager = fragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )

        fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentProtudos, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }
}
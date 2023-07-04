package visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentLojas
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentProtudos
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.carrinhoVisible

class MudarFragment {

     fun openFragmentLojas(fragmentManager: FragmentManager, idframe:Int,trocarcorItem: TrocarcorItem,carrinhoVisible: carrinhoVisible,atualizaCarrinho: AtualizaCarrinho) {
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

    fun openFragmentProtudos(fragmentManager: FragmentManager, idframe:Int,carrinhoVisible: carrinhoVisible,atualizaCarrinho:AtualizaCarrinho) {
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
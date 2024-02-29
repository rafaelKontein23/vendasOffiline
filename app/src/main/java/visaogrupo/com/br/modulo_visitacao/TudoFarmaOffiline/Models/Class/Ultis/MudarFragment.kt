package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentLojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProdutosKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProdutosLojaAB
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProtudos

class MudarFragment {

     fun openFragmentLojas(fragmentManager: FragmentManager, idframe:Int, trocarcorItem: TrocarcorItem, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho) {
        val fragmentLojas = FragmentLojas(trocarcorItem,carrinhoVisible,atualizaCarrinho)
         val fragmentManager: FragmentManager = fragmentManager
         val fragmentTransaction: FragmentTransaction =
             fragmentManager.beginTransaction()


         fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentLojas, "h")
         fragmentTransaction.addToBackStack("h")
         fragmentTransaction.commit()
    }

    fun openFragmentProtudos(fragmentManager: FragmentManager, idframe:Int, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho) {
        val fragmentProtudos = FragmentProtudos(carrinhoVisible,atualizaCarrinho)
        val fragmentManager: FragmentManager = fragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()


        fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentProtudos, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }

    fun openFragmentProtudosKit(fragmentManager: FragmentManager, idframe:Int, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho) {
        val fragmentProtudos = FragmentProdutosKit(carrinhoVisible)
        val fragmentManager: FragmentManager = fragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentProtudos, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }

    fun openFragmentProtudosAB(fragmentManager: FragmentManager, carrinhoVisible: carrinhoVisible) {
        val fragmentProtudosAB = FragmentProdutosLojaAB(carrinhoVisible)
        val fragmentManager: FragmentManager = fragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentProtudosAB, "h")
        fragmentTransaction.addToBackStack("h")
        fragmentTransaction.commit()
    }
}
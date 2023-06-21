package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.os.Handler
import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentAlerta

class Alertas {

    fun alerta(fragmentmeneger:FragmentManager){
        val fragmentAlerta = FragmentAlerta()
        val fragmentTransaction = fragmentmeneger.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.animatealertbaixo,R.anim.animsaialert)

        fragmentTransaction.replace(R.id.fragentalert, fragmentAlerta)

        fragmentTransaction.commit()
        try {
            Handler().postDelayed({
                try {
                    val removeTransaction = fragmentmeneger.beginTransaction()
                    removeTransaction.setCustomAnimations(R.anim.animsaialert, R.anim.animatealertbaixo)
                    removeTransaction.remove(fragmentAlerta)
                    removeTransaction.commit()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }, 3500)
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
}
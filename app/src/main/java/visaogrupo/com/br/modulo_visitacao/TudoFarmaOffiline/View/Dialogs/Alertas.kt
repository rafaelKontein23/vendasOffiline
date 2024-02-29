package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.os.Handler
import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentAlerta
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentCargas


class Alertas {

    fun alerta(fragmentmeneger:FragmentManager,textoalert:String, cortexto: String, imagem: Int, backcor: Int){
        val fragmentAlerta = FragmentAlerta(textoalert,cortexto,imagem,backcor)

        val fragmentTransaction = fragmentmeneger.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.animatealertbaixo,R.anim.animsaialert)

        fragmentTransaction.replace(R.id.fragentalert, fragmentAlerta)

        fragmentTransaction.commit()

        // Para fechar o alerta
        try {
            Handler().postDelayed({
                try {
                    val removeTransaction = fragmentmeneger.beginTransaction()
                    removeTransaction.setCustomAnimations(R.anim.animsaialert, R.anim.animatealertbaixo)
                    removeTransaction.remove(fragmentAlerta)
                    removeTransaction.commit()
                    FragmentCargas.alertvisible = false
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }, 4000)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}
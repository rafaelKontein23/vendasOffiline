package visaogrupo.com.br.modulo_visitacao.Views.Dialogs

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.fragment.app.FragmentManager
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentAlerta
import visaogrupo.com.br.modulo_visitacao.Views.Fragments.FragmentCargas

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
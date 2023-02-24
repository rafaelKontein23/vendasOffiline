package visaogrupo.com.br.modulo_visitacao.Views.Atividades.Atividades_Cargas_Lojas_Protudos.Cargas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_act_cargas.*
import visaogrupo.com.br.modulo_visitacao.R
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

class Act_Cargas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_cargas)

        icon_pedidos.setOnClickListener {
            Toast.makeText(baseContext,"adda",Toast.LENGTH_LONG).show()
            val layoutParams = view_transition.layoutParams as ConstraintLayout.LayoutParams

// Altere a restrição para o item desejado
            layoutParams.bottomToTop = icon_pedidos.id

// Defina as novas LayoutParams para o item
            view_transition.layoutParams = layoutParams

// Solicite a atualização do layout
            lay.requestLayout()
        }

    }
}
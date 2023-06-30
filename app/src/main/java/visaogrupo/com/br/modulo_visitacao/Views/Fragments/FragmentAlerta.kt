package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import visaogrupo.com.br.modulo_visitacao.R

class FragmentAlerta (textoalert:String, cortexto: String, imagem: Int, corbackground: String) : Fragment() {
    val  textoalert = textoalert
    val cortexto = cortexto
    val imagem = imagem
    val corbackground = corbackground
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alerta, container, false)
        val tituloalerta = view.findViewById<TextView>(R.id.tituloalerta)
        val backgroundalerta = view.findViewById<ConstraintLayout>(R.id.backgroundalerta)
        val iconalert = view.findViewById<ImageView>(R.id.iconalert)

        tituloalerta.text = textoalert
        tituloalerta.setTextColor(Color.parseColor(cortexto))
        val drawable = resources.getDrawable(imagem, null)
        iconalert.setImageDrawable(drawable)
        backgroundalerta.setBackgroundColor(Color.parseColor(corbackground))

        return view
    }
}
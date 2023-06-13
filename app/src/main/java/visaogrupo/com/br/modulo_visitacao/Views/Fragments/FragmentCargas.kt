package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cargas.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.CargaDiaria
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentCargasBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding
class FragmentCargas : Fragment() {

    lateinit var  login: Login

    private  lateinit var binding: FragmentCargasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCargasBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = binding.root
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("UserLogin", null)
        login =  gson.fromJson(objetoSerializado, Login::class.java)

        view.caragDiaria.setOnClickListener {
            val animatdor = animandoCarregando(view.carregandocargadiaria)
             atualizaviewAtualizando(view.caragDiaria,requireContext(),view.textcargaDiaria,view.infoTextCargDiaria)
             val cargadiaria = CargaDiaria()
             cargadiaria.fazCargaDiaria(requireContext(),login.Usuario_id.toString(),view.caragDiaria,view.textcargaDiaria,view.infoTextCargDiaria,view.imgcargadiaria,animatdor)
        }

        return  view
    }
    fun atualizaviewAtualizando (constrain :ConstraintLayout,context: Context,texttitulocarga:TextView,subtitulocarga:TextView){
        constrain.background = ContextCompat.getDrawable(context, R.drawable.bordas_atualizando)
        texttitulocarga.setTextColor(Color.parseColor("#004682"))
        subtitulocarga.setTextColor(Color.parseColor("#004682"))
        subtitulocarga.text ="Atualizando..."

    }

    fun animandoCarregando(carregandoimg:ImageView):ObjectAnimator{
        val animator = ObjectAnimator.ofFloat(carregandoimg, "rotation", 0f, 360f)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.start()
        return animator
    }

}

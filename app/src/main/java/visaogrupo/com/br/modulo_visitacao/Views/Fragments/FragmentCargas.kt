package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.coroutines.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.CargaDiaria
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCargas.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCargas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var  login: Login



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_cargas, container, false)
        val caragDiaria = view.findViewById<ConstraintLayout>(R.id.caragDiaria)
        val texttitulocargaDiaria = view.findViewById<TextView>(R.id.textcargaDiaria)
        val subtexttitulocargaDiaria = view.findViewById<TextView>(R.id.infoTextCargDiaria)
        val iconCargaDiaria = view.findViewById<ImageView>(R.id.imgcargadiaria)
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("UserLogin", null)
        login =  gson.fromJson(objetoSerializado, Login::class.java)





        caragDiaria.setOnClickListener {

             atualizaviewAtualizando(caragDiaria,requireContext(),texttitulocargaDiaria,subtexttitulocargaDiaria)
             val cargadiaria = CargaDiaria()
             cargadiaria.fazCargaDiaria(requireContext(),login.Usuario_id.toString(),caragDiaria,texttitulocargaDiaria,subtexttitulocargaDiaria,iconCargaDiaria)
        }

        return  view
    }
    fun atualizaviewAtualizando (constrain :ConstraintLayout,context: Context,texttitulocarga:TextView,subtitulocarga:TextView){
        constrain.background = ContextCompat.getDrawable(context, R.drawable.bordas_atualizando)
        texttitulocarga.setTextColor(Color.parseColor("#004682"))
        subtitulocarga.setTextColor(Color.parseColor("#004682"))
        subtitulocarga.text ="Atualizando..."

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCargas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}

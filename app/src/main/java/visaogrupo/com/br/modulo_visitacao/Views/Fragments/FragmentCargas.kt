package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cargas.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.TaskModeloPedido
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.TaskCargas.taskImagem
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.CargaDiaria
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TerminouCarga
import visaogrupo.com.br.modulo_visitacao.Views.Models.Login
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentCargasBinding

class FragmentCargas () : Fragment() ,TerminouCarga{

    lateinit var  login: Login
    var callback: MyCallback? = null
    private  lateinit var binding: FragmentCargasBinding
    companion object {
        var alertvisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCargasBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = binding.root
        val sharedPreferences =context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("UserLogin", null)
        login =  gson.fromJson(objetoSerializado, Login::class.java)
        val feitacarga = sharedPreferences?.getBoolean("cargafeita", false)



        if(feitacarga ==true){
            trocaCoritensCargaFeita(binding.imgCargaiImagem,binding.textcargaImagem,binding.infoTextCargaImagem)
            trocaCoritensCargaFeita(binding.imgCargamodelo,binding.textcargamodelo,binding.infoTextCargamodelo)
        }else{
            trocaCoritensCargaNaoFeita(binding.imgCargaiImagem,binding.textcargaImagem,binding.infoTextCargaImagem)
            trocaCoritensCargaNaoFeita(binding.imgCargamodelo,binding.textcargamodelo,binding.infoTextCargamodelo)
        }

        binding.nomeVendedor.text = login.Nome
        binding.emailUsuario.text = login.Email

        //clicks cargas
        binding.cargaImagem.setOnClickListener {
            val feitacarga1 = sharedPreferences?.getBoolean("cargafeita", false)
            if (feitacarga1 == false){
                if (!alertvisible){
                    alertvisible = true
                    val alertas = Alertas()
                    alertas.alerta(requireActivity().supportFragmentManager,"Por favor realize a carga diaria","#B89A00",R.drawable.atencao,"#FDF6D2")
                }
            }else{
                // fazer carga de imagem aqui
                val animatdor = animandoCarregando(view.carregandocargaimagem)
                atualizaviewAtualizando(view.cargaImagem,requireContext(),view.textcargaImagem,view.infoTextCargaImagem)
                val  taskImagem = taskImagem(requireContext())

                taskImagem.requestImagem(requireContext(),login.Usuario_id.toString(),view.cargaImagem,view.textcargaImagem,view.infoTextCargaImagem,view.imgCargaiImagem,animatdor,this)
            }
        }
        binding.cargaModelodePedido.setOnClickListener {

            if (feitacarga == false){
                if (!alertvisible){
                    alertvisible = true
                    val alertas = Alertas()
                    alertas.alerta(requireActivity().supportFragmentManager,"Por favor realize a carga diaria","#B89A00",R.drawable.atencao,"#FDF6D2")
                }

            }else{
                // fazer carga de modelo de pedido aqui
                val animatdor = animandoCarregando(view.carregandocargamodelo)
                atualizaviewAtualizando(view.cargaModelodePedido,requireContext(),view.textcargamodelo,view.infoTextCargamodelo)
                val taskModeloPedido = TaskModeloPedido()
                taskModeloPedido.requestModelo(requireContext(),login.Usuario_id.toString(),view.cargaModelodePedido,view.textcargamodelo,view.infoTextCargamodelo,view.imgCargamodelo,animatdor,this)

            }
        }
        binding.caragDiaria.setOnClickListener {
            binding.caragDiaria.isEnabled =false
            val animatdor = animandoCarregando(view.carregandocargadiaria)
            atualizaviewAtualizando(view.caragDiaria,requireContext(),view.textcargaDiaria,view.infoTextCargDiaria)
            val cargadiaria = CargaDiaria()
            cargadiaria.fazCargaDiaria(requireContext(),login.Usuario_id.toString(),view.caragDiaria,view.textcargaDiaria,view.infoTextCargDiaria,view.imgcargadiaria,animatdor,this)

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

    override fun terminouCarga() {
        trocaCoritensCargaFeita(binding.imgCargaiImagem,binding.textcargaImagem,binding.infoTextCargaImagem)
        trocaCoritensCargaFeita(binding.imgCargamodelo,binding.textcargamodelo,binding.infoTextCargamodelo)
        callback?.onActionDone()
        binding.caragDiaria.isEnabled = true


    }

    fun trocaCoritensCargaFeita(img:ImageView,texttitulo:TextView,descricao:TextView){
       img.background = ContextCompat.getDrawable(requireContext(),R.drawable.bordasimagenscargas)
       trocar_cor_iten(img,img.drawable,"#336B9B")
       texttitulo.setTextColor(Color.parseColor("#2A313C"))
       descricao.setTextColor(Color.parseColor("#2A313C"))
    }
    fun trocaCoritensCargaNaoFeita(img:ImageView,texttitulo:TextView,descricao:TextView){
        img.background = ContextCompat.getDrawable(requireContext(),R.drawable.bordas_carga_bloquiada)
        trocar_cor_iten(img,img.drawable,"#9FA3A8")
        texttitulo.setTextColor(Color.parseColor("#B3585E68"))
        descricao.setTextColor(Color.parseColor("#B3585E68"))
    }
    private fun trocar_cor_iten(icon:ImageView,drawable: Drawable,cor:String){
        val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(cor))
        icon.setImageDrawable(wrappedDrawable)
    }
    interface MyCallback {
        fun onActionDone()

    }
}

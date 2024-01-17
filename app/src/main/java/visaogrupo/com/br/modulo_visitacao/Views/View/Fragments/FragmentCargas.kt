package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cargas.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.CargaDiaria
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.TaskEstoqueSeparado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.task.TaskCargas.taskImagem
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.Views.View.Dialogs.DialogMudarAmbienteSenha
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentCargasBinding

class FragmentCargas () : Fragment() ,
    visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TerminouCarga {

    lateinit var  login: Login
    var callback: MyCallback? = null
    private  lateinit var binding: FragmentCargasBinding
    var trocaAmbienteCount =0
    companion object {
        var alertvisible = false
        var progresspush = 0
        // exibi push
        fun showNotification(context: Context, channelId: String, title: String, message: String) {
            val contentView = RemoteViews(context.packageName, R.layout.celula_notificacao_carga)
            contentView.setTextViewText(R.id.notification_title,"Atualizando")
            contentView.setProgressBar(R.id.notification_content,10, progresspush,false)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.tdf_azul) // Ícone da notificação
                .setContentTitle(title) // Título da notificação
                .setCustomBigContentView(contentView)
                .setContentText(message)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridade da notificação
                .setAutoCancel(true) // Fechar notificação automaticamente ao ser clicada

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, notificationBuilder.build())
        }

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


         // verifica se a carga ja esta feita
        if(feitacarga ==true){
            trocaCoritensCargaFeita(binding.imgCargaiImagem,binding.textcargaImagem,binding.infoTextCargaImagem)
            trocaCoritensCargaFeita(binding.imgEstoque,binding.textEstoque,binding.infoTextoEsque)
        }else{
            trocaCoritensCargaNaoFeita(binding.imgCargaiImagem,binding.textcargaImagem,binding.infoTextCargaImagem)
            trocaCoritensCargaNaoFeita(binding.imgEstoque,binding.textEstoque,binding.infoTextoEsque)
        }


        val verificaInternet = Verifica_Internet()
        binding.nomeVendedor.text = login.Nome
        binding.emailUsuario.text = login.Email

        // troca ambiente
        binding.trocaAmbiente.setOnClickListener {
            trocaAmbienteCount +=1
            if (trocaAmbienteCount ==5){
                trocaAmbienteCount =0
                val  dialog = DialogMudarAmbienteSenha()
                dialog.dialogSenha(requireContext())
            }
        }

        //clicks cargas
        binding.cargaImagem.setOnClickListener {
                if (verificaInternet.isOnline(requireContext())){
                    val feitacarga1 = sharedPreferences?.getBoolean("cargafeita", false)
                    if (feitacarga1 == false){
                        if (!alertvisible){
                            alertvisible = true
                            val alertas = Alertas()
                            alertas.alerta(requireActivity().supportFragmentManager,"Por favor realize a carga diaria","#B89A00",R.drawable.atencao,R.drawable.bordas_amerala_alert)
                        }
                    }else{
                        // fazer carga de imagem aqui
                        val animatdor = animandoCarregando(view.carregandocargaimagem)
                        atualizaviewAtualizando(view.cargaImagem,requireContext(),view.textcargaImagem,view.infoTextCargaImagem)
                        val  taskImagem = taskImagem(requireContext())
                        taskImagem.requestImagem(requireContext(),login.Usuario_id.toString(),view.cargaImagem,view.textcargaImagem,view.infoTextCargaImagem,view.imgCargaiImagem,animatdor,this)
                    }
                }else {
                    val  dialogErro = DialogErro()
                    dialogErro.Dialog(requireContext(),"Sem conexão", "Tente novamente mais tarde", "Ok",""){

                }
            }

        }
        binding.cargaEstoque.setOnClickListener {
            if (verificaInternet.isOnline(requireContext())){
                if (feitacarga == false){
                    if (!alertvisible){
                        alertvisible = true
                        val alertas = Alertas()
                        alertas.alerta(requireActivity().supportFragmentManager,"Por favor realize a carga diaria","#B89A00",R.drawable.atencao,R.drawable.bordas_amerala_alert)
                    }

                }else{
                    // fazer carga de modelo de pedido aqui
                    val animatdor = animandoCarregando(view.carregandoEstoque)
                    atualizaviewAtualizando(view.cargaEstoque,requireContext(),view.textEstoque,view.infoTextoEsque)
                    val taskEstoque = TaskEstoqueSeparado()
                    taskEstoque.requestEstoque(requireContext(),view.cargaEstoque,view.textEstoque,view.infoTextoEsque,view.imgEstoque,animatdor,this)

                }
            }else {
                val  dialogErro = DialogErro()
                dialogErro.Dialog(requireContext(),"Sem conexão", "Tente novamente mais tarde", "Ok",""){

                }
            }


        }
        binding.caragDiaria.setOnClickListener {
            if (verificaInternet.isOnline(requireContext())){
                creadordepush(requireContext())
                showNotification(requireContext(),"TESTE1","Titulo1","sff")
                binding.caragDiaria.isEnabled =false
                val animatdor = animandoCarregando(view.carregandocargadiaria)
                atualizaviewAtualizando(view.caragDiaria,requireContext(),view.textcargaDiaria,view.infoTextCargDiaria)
                val cargadiaria = CargaDiaria()
                cargadiaria.fazCargaDiaria(requireContext(),login.Usuario_id.toString(),view.caragDiaria,view.textcargaDiaria,view.infoTextCargDiaria,view.imgcargadiaria,animatdor,this)

            }else {
                val  dialogErro = DialogErro()
                dialogErro.Dialog(requireContext(),"Sem conexão", "Tente novamente mais tarde", "Ok",""){

                }
            }

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
        trocaCoritensCargaFeita(binding.imgEstoque,binding.textEstoque,binding.infoTextoEsque)
        callback?.onActionDone()
        val alerta = Alertas()
        alerta.alerta(requireActivity().supportFragmentManager,"Carga feita! Boas Vendas =)","#4DA310",R.drawable.certo,R.drawable.bordas_verde_alert)
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
    // call back na tela de carga principal
    interface MyCallback {
        fun onActionDone()
    }
    // cria push
    fun creadordepush(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("TESTE1", "DADAAKJNJ", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }
}

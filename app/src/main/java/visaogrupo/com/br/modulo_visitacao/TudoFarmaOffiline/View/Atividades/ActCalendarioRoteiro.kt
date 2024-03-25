package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_produtos_loja_a_b.linearLayout
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActCalendarioRoteiroBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.VisitaDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterVisitasOrdenar


class ActCalendarioRoteiro : AppCompatActivity() {
    lateinit var binding:ActivityActCalendarioRoteiroBinding
    lateinit var adapterCalendario:AdapterVisitasOrdenar
    var listaVisita = mutableListOf<Visitas>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActCalendarioRoteiroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterCalendario = AdapterVisitasOrdenar(listaVisita)
        val  linearLayoutManager = LinearLayoutManager(this)
        binding.recyVisitasMarcadas.layoutManager = linearLayoutManager
        binding.recyVisitasMarcadas.adapter = adapterCalendario

        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val halfScreenHeight = displayMetrics.heightPixels / 2
        val params = binding.constraintLayout7.getLayoutParams()

        params.height = halfScreenHeight + 50

        binding.constraintLayout7.setLayoutParams(params)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewDrop)
        bottomSheetBehavior.peekHeight = halfScreenHeight -50



        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


        binding.calendarView.setOnDateChangeListener(object :OnDateChangeListener{
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                 var mes  = month +1
                 val diaAtual = DataAtual()
                 val dia  = diaAtual.recuperaData()

                 var data = dayOfMonth.toString()+"/"+ "0" + mes.toString()+"/"+year.toString()
                 val visitasDAO = VisitaDAO(baseContext)
                 listaVisita.clear()

                 listaVisita =  visitasDAO.lista(false,data)
                 adapterCalendario.listaVisitas = listaVisita
                 adapterCalendario.notifyDataSetChanged()
                 if (dia.equals(data)){
                     binding.diaSelecionado.text = "Hoje"

                 }else{
                     binding.diaSelecionado.text = data

                 }
            }

        })
    }
}
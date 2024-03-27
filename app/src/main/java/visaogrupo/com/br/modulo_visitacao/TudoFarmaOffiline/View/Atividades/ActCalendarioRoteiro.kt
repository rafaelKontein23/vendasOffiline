package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_act_calendario_roteiro.diaSelecionado
import kotlinx.android.synthetic.main.activity_act_calendario_roteiro.view.recyVisitasMarcadas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActCalendarioRoteiroBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.menuVisitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Personalizacao.CustomSpinnerItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.MenuVisitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.VisitaDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterMenuCima
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterVisitasOrdenar
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import java.util.Calendar
import java.util.Collections


class ActCalendarioRoteiro : AppCompatActivity(),menuVisitas {
    lateinit var binding:ActivityActCalendarioRoteiroBinding
    lateinit var adapterCalendario:AdapterVisitasOrdenar
    var listaVisita = mutableListOf<Visitas>()
    var dataAtualiza =""
    var menuVisitas = MenuVisitas.naoVisitada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActCalendarioRoteiroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapterCalendario = AdapterVisitasOrdenar(listaVisita)
        val  linearLayoutManager = LinearLayoutManager(this)
        binding.recyVisitasMarcadas.layoutManager = linearLayoutManager
        binding.recyVisitasMarcadas.adapter = adapterCalendario

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        buscaInfos(year,month,dayOfMonth)

        binding.menuCima.setOnClickListener {

           binding.menuRecycler.isVisible = true
        }


        val listaItensMenu = listOf(
            CustomSpinnerItem(
                "Marca como não visitadas",
                R.drawable.calendar_x_icon
            ),
            CustomSpinnerItem(
                "Excluir visitas",
                R.drawable.excluir_icon
            ),
            CustomSpinnerItem(
                "Remarcar visitas",
                R.drawable.remarcar_icon
            ),
            CustomSpinnerItem(
                "Filtros",
                R.drawable.filtro_icon
            )
        )
        val adapterMenuCima     = AdapterMenuCima(listaItensMenu, baseContext,act = this, menuVisitas = this)
        val linearMeneger =  LinearLayoutManager(baseContext)
        binding.menuRecycler.layoutManager = linearMeneger
        binding.menuRecycler.adapter       = adapterMenuCima

        binding.aplicarFiltro.setOnClickListener {
            when(menuVisitas){
                MenuVisitas.filtros ->{
                }

                MenuVisitas.excluivisita ->{
                    val dialogErro = DialogErro()
                    MainScope().launch {
                        dialogErro.Dialog(this@ActCalendarioRoteiro,"Atenção", "As visitas selecionadas serão excluidas, deseja continuar?"
                            ,"Sim",
                            "Não"){
                            CoroutineScope(Dispatchers.IO).launch {
                                val listaVisitasFilter= mutableListOf<Visitas>()
                                for ((i,visista) in listaVisita.withIndex()){
                                    if (visista.selcionado){
                                        listaVisitasFilter.add(visista)
                                    }
                                }
                                MainScope().launch {
                                    adapterCalendario.notifyDataSetChanged()
                                    voltaConfigInicial()
                                }
                                val visitaDAO = VisitaDAO(this@ActCalendarioRoteiro)
                                visitaDAO.atualizaPositionEstatus(listaVisitasFilter,true,2)
                            }
                        }
                    }
                }

                MenuVisitas.naoVisitada ->{
                    val dialogErro = DialogErro()
                    MainScope().launch {
                        dialogErro.Dialog(this@ActCalendarioRoteiro,"Atenção", "As visitas selecionadas serão marcadas como não vistadas, deseja continuar?"
                            ,"Sim",
                            "Não"){
                            CoroutineScope(Dispatchers.IO).launch {
                                val listaVisitasFilter= mutableListOf<Visitas>()
                                for (i in listaVisita){
                                    if (i.selcionado){
                                        i.selcionado = false
                                        i.status = 2
                                        listaVisitasFilter.add(i)
                                    }
                                }
                                MainScope().launch {
                                    adapterCalendario.listaVisitas = listaVisita
                                    voltaConfigInicial()
                                }
                                val visitaDAO = VisitaDAO(this@ActCalendarioRoteiro)
                                visitaDAO.atualizaPositionEstatus(listaVisitasFilter,true,2)
                            }
                        }
                    }

                }

                MenuVisitas.remarcarVisitas->{


                }
                else->{

                }
            }
        }

        binding.limparfiltro.setOnClickListener {
            esconteItens(false)
            binding.menuRecycler.isVisible = false
            adapterCalendario.selecionar = false
            adapterCalendario.notifyDataSetChanged()
            binding.descricao.text = "Segure e arraste para reorganizar as visitas como preferir"
            binding.diaSelecionado.text = dataAtualiza

        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val halfScreenHeight = displayMetrics.heightPixels / 2
        val params = binding.constraintLayout7.getLayoutParams()
        params.height = halfScreenHeight
        binding.constraintLayout7.setLayoutParams(params)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewDrop)
        bottomSheetBehavior.peekHeight = halfScreenHeight

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        ) {
            private var isDragging = false
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                Collections.swap(listaVisita, fromPosition, toPosition)
                adapterCalendario.notifyItemMoved(fromPosition, toPosition)

                return true
            }
            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {

                    isDragging = true
                } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && isDragging) {
                    isDragging = false
                    val visitaDAO = VisitaDAO(baseContext)
                    visitaDAO.atualizaPositionEstatus(listaVisita)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyVisitasMarcadas);

        binding.calendarView.setOnDateChangeListener(object :OnDateChangeListener{
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                buscaInfos(year,month,dayOfMonth)
            }
        })
    }

    fun buscaInfos(  year: Int,
                     month: Int,
                     dayOfMonth: Int){
        CoroutineScope(Dispatchers.IO).launch {
            MainScope().launch {
                binding.recyVisitasMarcadas.recyVisitasMarcadas.isVisible = false
                binding.semvisitas.isVisible = false
                binding.progressVisitas.isVisible = true
                binding.descricao.isVisible = true
                esconteItens(false)
                binding.menuRecycler.isVisible = false
                adapterCalendario.selecionar = false
                adapterCalendario.notifyDataSetChanged()
                binding.descricao.text = "Segure e arraste para reorganizar as visitas como preferir"
                binding.diaSelecionado.text = dataAtualiza
            }
            var mes  = month +1
            val diaAtual = DataAtual()
            val dia  = diaAtual.recuperaData()

            var data = dayOfMonth.toString()+"/"+ "0" + mes.toString()+"/"+year.toString()
            val visitasDAO = VisitaDAO(baseContext)
            listaVisita.clear()
            dataAtualiza = dayOfMonth.toString()+"/"+   mes.toString()+"/"+year.toString()
            listaVisita =  visitasDAO.lista(false, dataAtualiza)
            MainScope().launch {
                if (listaVisita.isEmpty()){
                    binding.recyVisitasMarcadas.recyVisitasMarcadas.isVisible = false
                    binding.semvisitas.isVisible = true
                    binding.progressVisitas.isVisible = false
                    binding.descricao.isVisible = false
                    if (dia.equals(data)){
                        binding.diaSelecionado.text = "Hoje"

                    }else{
                        binding.diaSelecionado.text = data

                    }
                }else{
                    adapterCalendario.listaVisitas = listaVisita
                    adapterCalendario.selecionar = false
                    adapterCalendario.notifyDataSetChanged()
                    if (dia.equals(data)){
                        binding.diaSelecionado.text = "Hoje"

                    }else{
                        binding.diaSelecionado.text = data

                    }
                    binding.recyVisitasMarcadas.recyVisitasMarcadas.isVisible = true
                    binding.semvisitas.isVisible = false
                    binding.progressVisitas.isVisible = false
                    binding.descricao.isVisible = true
                }

            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null && binding.menuRecycler.isVisible) {

            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (binding.menuRecycler.isTouchInsideView(x, y)) {
                return super.dispatchTouchEvent(ev)
            } else {
                binding. menuRecycler.isVisible = false
                return true
            }
        }
        return super.dispatchTouchEvent(ev)

    }
    fun View.isTouchInsideView(x: Int, y: Int): Boolean {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return (x >= viewX && x <= viewX + this.width && y >= viewY && y <= viewY + this.height)
    }

    override fun visitasmenu(menuVisitass: MenuVisitas) {
        esconteItens(true)

        when(menuVisitass){
             MenuVisitas.filtros ->{
                 menuVisitas = MenuVisitas.filtros
             }

            MenuVisitas.excluivisita ->{
                menuVisitas = MenuVisitas.excluivisita

                binding.diaSelecionado.text = "Selecione os itens que deseja excluir visitadas"
                adapterCalendario.selecionar  = true
                adapterCalendario.notifyDataSetChanged()

            }

            MenuVisitas.naoVisitada ->{
                menuVisitas = MenuVisitas.naoVisitada

                binding.diaSelecionado.text = "Selecione os itens que deseja marcar como não visitados"
                adapterCalendario.selecionar  = true
                adapterCalendario.notifyDataSetChanged()
            }

            MenuVisitas.remarcarVisitas->{
                menuVisitas = MenuVisitas.remarcarVisitas


            }
            else->{

            }
        }
    }

    fun esconteItens(esconte:Boolean){
        binding.menuRecycler.isVisible = !esconte
        binding.descricao.text  = ""
        binding.menuCima.isVisible = !esconte
        binding.aplicarFiltro.isVisible = esconte
        binding.limparfiltro.isVisible = esconte
    }
    fun voltaConfigInicial(){
        binding.recyVisitasMarcadas.recyVisitasMarcadas.isVisible = true
        binding.semvisitas.isVisible = false
        binding.progressVisitas.isVisible = false
        binding.descricao.isVisible = true
        binding.menuCima.isVisible = true
        esconteItens(true)
        binding.menuRecycler.isVisible = false
        adapterCalendario.selecionar = false
        adapterCalendario.notifyDataSetChanged()
        binding.descricao.text = "Segure e arraste para reorganizar as visitas como preferir"
        binding.diaSelecionado.text = dataAtualiza
        binding.aplicarFiltro.isVisible = false
        binding.limparfiltro.isVisible = false

    }
}
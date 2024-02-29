package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaFiltrosProduto
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaListaFiltro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.LimparFiltrosProdutos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Filtros
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.FiltrosPricipaisFilt
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.FiltroDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.FiltroPrincipalDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterFiltroPrincipal

class DialogFiltro :AtualizaListaFiltro {
    val listaFiltroIDSelecionados = mutableListOf<Int>()
    var filtroOrdenar = "1"
    fun dialogFiltro(context: Context?,
                     atualizaFiltrosProduto: AtualizaFiltrosProduto,
                     limparFiltro:Boolean,
                     limparFiltrosProdutos: LimparFiltrosProdutos,
                     lojaId:Int){

        val dialog = android.app.Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_filtro)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialoAnimationEsquerParaDireita
        dialog.window!!.setGravity(Gravity.LEFT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val  xFiltros = dialog.findViewById<ImageView>(R.id.xFiltros)

        val  AaZ = dialog.findViewById<TextView>(R.id.AaZ)
        val  ZaA = dialog.findViewById<TextView>(R.id.ZaA)
        val  menorPreco = dialog.findViewById<TextView>(R.id.menorPreco)
        val  maiorPreco = dialog.findViewById<TextView>(R.id.maiorPreco)
        val recyclerViewFiltro = dialog.findViewById<RecyclerView>(R.id.recyclerViewFiltro)
        val progressBarFiltros = dialog.findViewById<ProgressBar>(R.id.progressBarFiltros)
        val  confirmarFiltro =dialog.findViewById<Button>(R.id.confirmarFiltro)
        val limparFiltroButton  = dialog.findViewById<Button>(R.id.limparfiltro)
        seleciona(AaZ,context)
        deselecionaItem(ZaA,context)
        deselecionaItem(menorPreco,context)
        deselecionaItem(maiorPreco,context)

        confirmarFiltro.setOnClickListener {
           dialog.dismiss()
           atualizaFiltrosProduto.filtraListaProdutos(listaFiltroIDSelecionados,filtroOrdenar)
        }
        limparFiltroButton.isVisible = limparFiltro

        if (limparFiltro){
            confirmarFiltro.setBackgroundResource(R.drawable.bordas_botaofiltroverde)
        }else{
            confirmarFiltro.setBackgroundResource(R.drawable.bordas_filtro_verde)

        }

        limparFiltroButton.setOnClickListener {
            dialog.dismiss()
            limparFiltrosProdutos.LimparFiltro()
        }
        CoroutineScope(Dispatchers.IO).launch {
           val filtroPrincipalDAO = FiltroPrincipalDAO(context)
           val filtroDao = FiltroDAO(context)
           val listaFiltroPrincipal  =filtroPrincipalDAO.listar(lojaId)
           val listaFiltro = filtroDao.listar(lojaId)
            var listaFiltrosPricipaisFilt =  mutableListOf<FiltrosPricipaisFilt>()


           for (i in listaFiltroPrincipal){
               val listaFiltrosConjunto = mutableListOf<Filtros>()
               for (k  in listaFiltro){
                   if (i.FiltroCategoria_id == k.FiltroCategoria_id){
                       listaFiltrosConjunto.add(k)
                   }
               }
               val filtroPrincipalFilt = FiltrosPricipaisFilt(i.Descricao,listaFiltrosConjunto)
               listaFiltrosPricipaisFilt.add(filtroPrincipalFilt)
           }
            MainScope().launch {
                val linearLayoutManager = LinearLayoutManager(context)
                val adapterFiltroPrincipal = AdapterFiltroPrincipal(listaFiltrosPricipaisFilt,context,this@DialogFiltro)
                recyclerViewFiltro.layoutManager = linearLayoutManager
                progressBarFiltros.isVisible = false
                recyclerViewFiltro.adapter = adapterFiltroPrincipal


            }
        }


        xFiltros.setOnClickListener {
            dialog.dismiss()
        }

        AaZ.setOnClickListener {
            seleciona(AaZ,context)
            deselecionaItem(ZaA,context)
            deselecionaItem(menorPreco,context)
            deselecionaItem(maiorPreco,context)
            filtroOrdenar ="1"
        }
        ZaA.setOnClickListener {
            seleciona(ZaA,context)
            deselecionaItem(AaZ,context)
            deselecionaItem(menorPreco,context)
            deselecionaItem(maiorPreco,context)
            filtroOrdenar ="Produtos.nome DESC"
        }
        menorPreco.setOnClickListener {
            seleciona(menorPreco,context)
            deselecionaItem(ZaA,context)
            deselecionaItem(AaZ,context)
            deselecionaItem(maiorPreco,context)
            filtroOrdenar ="Progressiva.pf ASC"
        }
        maiorPreco.setOnClickListener {
            seleciona(maiorPreco,context)
            deselecionaItem(ZaA,context)
            deselecionaItem(menorPreco,context)
            deselecionaItem(AaZ,context)
            filtroOrdenar ="Progressiva.pf DESC"

        }
    }
    fun deselecionaItem(text:TextView,context: Context){
        val drawable = text.compoundDrawablesRelative[0]
        val corDesejada = ContextCompat.getColor(context, R.color.deseleciona)
        DrawableCompat.setTint(drawable, corDesejada)
        text.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        text.setTextColor(Color.parseColor("#5E737880"))

    }

    fun seleciona(text:TextView,context:Context){
        val drawable = text.compoundDrawablesRelative[0]
        val corDesejada = ContextCompat.getColor(context, R.color.corazultext)
        DrawableCompat.setTint(drawable, corDesejada)
        text.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        text.setTextColor(Color.parseColor("#004682"))
    }

    override fun atualizaFiltro(filtroId: Int, remove:Boolean) {
        if (remove){
            for (i in 0 until listaFiltroIDSelecionados.size){
                if (listaFiltroIDSelecionados[i] == filtroId){
                    listaFiltroIDSelecionados.remove(listaFiltroIDSelecionados[i])
                    break
                }
            }

        }else{
            listaFiltroIDSelecionados.add(filtroId)

        }
    }

}